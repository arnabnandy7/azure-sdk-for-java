// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.messaging.servicebus;

import com.azure.core.util.IterableStream;
import com.azure.core.util.logging.ClientLogger;
import com.azure.messaging.servicebus.administration.models.DeadLetterOptions;
import com.azure.messaging.servicebus.models.ReceiveMode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.publisher.TestPublisher;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link ServiceBusReceiverClient}.
 */
class ServiceBusReceiverClientTest {
    private static final String NAMESPACE = "test-namespace";
    private static final String ENTITY_PATH = "test-entity-path";
    private static final String LOCK_TOKEN = UUID.randomUUID().toString();

    private static final Duration OPERATION_TIMEOUT = Duration.ofSeconds(5);

    private final ClientLogger logger = new ClientLogger(ServiceBusReceiverClientTest.class);

    private ServiceBusReceiverClient client;

    @Mock
    private ServiceBusReceiverAsyncClient asyncClient;
    @Mock
    private Map<String, Object> propertiesToModify;
    @Mock
    private ServiceBusTransactionContext transactionContext;
    @Mock
    private ServiceBusReceivedMessage message;
    @Mock
    private Consumer<Throwable> onErrorConsumer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        when(asyncClient.getEntityPath()).thenReturn(ENTITY_PATH);
        when(asyncClient.getFullyQualifiedNamespace()).thenReturn(NAMESPACE);
        when(asyncClient.getReceiverOptions()).thenReturn(new ReceiverOptions(ReceiveMode.PEEK_LOCK, 1));

        client = new ServiceBusReceiverClient(asyncClient, OPERATION_TIMEOUT);
    }

    @AfterEach
    void teardown() {
        Mockito.framework().clearInlineMocks();
    }

    @Test
    void nullConstructor() {
        assertThrows(NullPointerException.class, () -> new ServiceBusReceiverClient(null, OPERATION_TIMEOUT));
        assertThrows(NullPointerException.class, () -> new ServiceBusReceiverClient(asyncClient, null));
    }

    @Test
    void properties() {
        assertEquals(NAMESPACE, client.getFullyQualifiedNamespace());
        assertEquals(ENTITY_PATH, client.getEntityPath());
    }

    @Test
    void abandonMessageWithTransaction() {
        // Arrange
        when(asyncClient.abandon(eq(message), isNull(), eq(transactionContext))).thenReturn(Mono.empty());

        // Act
        client.abandon(message, null, transactionContext);

        // Assert
        verify(asyncClient).abandon(eq(message), isNull(), eq(transactionContext));
    }

    @Test
    void abandonMessage() {
        // Arrange
        when(asyncClient.abandon(eq(message))).thenReturn(Mono.empty());

        // Act
        client.abandon(message);

        // Assert
        verify(asyncClient).abandon(eq(message));
    }

    @Test
    void abandonMessageWithProperties() {
        // Arrange
        when(asyncClient.abandon(eq(message), eq(propertiesToModify))).thenReturn(Mono.empty());

        // Act
        client.abandon(message, propertiesToModify);

        // Assert
        verify(asyncClient).abandon(eq(message), eq(propertiesToModify));
    }

    /**
     * Verifies that we can auto-renew a message lock.
     */
    @Test
    void autoRenewMessageLock() {
        // Arrange
        final Duration maxDuration = Duration.ofSeconds(8);
        final TestPublisher<Void> publisher = TestPublisher.create();

        when(message.getLockToken()).thenReturn(LOCK_TOKEN);

        doAnswer(answer -> {
            fail("On error should not have been invoked.");
            return null;
        }).when(onErrorConsumer).accept(any());
        when(asyncClient.renewMessageLock(message, maxDuration)).thenReturn(publisher.mono());

        // Act
        client.renewMessageLock(message, maxDuration, onErrorConsumer);

        // Assert
        verify(asyncClient).renewMessageLock(message, maxDuration);
    }

    /**
     * Verifies that we can auto-renew a message lock and it calls the error consumer.
     */
    @Test
    void autoRenewMessageLockFails() {
        // Arrange
        final Duration maxDuration = Duration.ofSeconds(8);
        final TestPublisher<Void> publisher = TestPublisher.create();
        final Throwable testError = new IllegalAccessException("Some exception");

        when(message.getLockToken()).thenReturn(LOCK_TOKEN);

        when(asyncClient.renewMessageLock(message, maxDuration)).thenReturn(publisher.mono());

        client.renewMessageLock(message, maxDuration, onErrorConsumer);

        // Act
        publisher.error(testError);

        // Assert
        verify(asyncClient).renewMessageLock(message, maxDuration);
        verify(onErrorConsumer).accept(testError);
    }

    /**
     * Verifies that we can auto-renew a message lock and it will not fail with an NPE when we have a null onError.
     */
    @Test
    void autoRenewMessageLockFailsNull() {
        // Arrange
        final Duration maxDuration = Duration.ofSeconds(8);
        final TestPublisher<Void> publisher = TestPublisher.create();
        final Throwable testError = new IllegalAccessException("Some exception");

        when(asyncClient.renewMessageLock(message, maxDuration)).thenReturn(publisher.mono());

        client.renewMessageLock(message, maxDuration, null);

        // Act
        publisher.error(testError);

        // Assert
        verify(asyncClient).renewMessageLock(message, maxDuration);
        verify(onErrorConsumer, never()).accept(testError);
    }

    /**
     * Verifies that we can auto-renew a session lock.
     */
    @Test
    void autoRenewSessionLock() {
        // Arrange
        final Duration maxDuration = Duration.ofSeconds(8);
        final TestPublisher<Void> publisher = TestPublisher.create();

        doAnswer(answer -> {
            fail("On error should not have been invoked.");
            return null;
        }).when(onErrorConsumer).accept(any());
        when(asyncClient.renewSessionLock(LOCK_TOKEN, maxDuration)).thenReturn(publisher.mono());

        // Act
        client.renewSessionLock(LOCK_TOKEN, maxDuration, onErrorConsumer);

        // Assert
        verify(asyncClient).renewSessionLock(LOCK_TOKEN, maxDuration);
    }

    /**
     * Verifies that we can auto-renew a session lock and it calls the error consumer.
     */
    @Test
    void autoRenewSessionLockFails() {
        // Arrange
        final Duration maxDuration = Duration.ofSeconds(8);
        final TestPublisher<Void> publisher = TestPublisher.create();
        final Throwable testError = new IllegalAccessException("Some exception");

        when(asyncClient.renewSessionLock(LOCK_TOKEN, maxDuration)).thenReturn(publisher.mono());

        client.renewSessionLock(LOCK_TOKEN, maxDuration, onErrorConsumer);

        // Act
        publisher.error(testError);

        // Assert
        verify(asyncClient).renewSessionLock(LOCK_TOKEN, maxDuration);
        verify(onErrorConsumer).accept(testError);
    }

    /**
     * Verifies that we can auto-renew a message lock and it will not fail with an NPE when we have a null onError.
     */
    @Test
    void autoRenewSessionLockFailsNull() {
        // Arrange
        final Duration maxDuration = Duration.ofSeconds(8);
        final TestPublisher<Void> publisher = TestPublisher.create();
        final Throwable testError = new IllegalAccessException("Some exception");

        when(asyncClient.renewSessionLock(LOCK_TOKEN, maxDuration)).thenReturn(publisher.mono());

        client.renewSessionLock(LOCK_TOKEN, maxDuration, null);

        // Act
        publisher.error(testError);

        // Assert
        verify(asyncClient).renewSessionLock(LOCK_TOKEN, maxDuration);
        verify(onErrorConsumer, never()).accept(testError);
    }

    @Test
    void completeMessageWithTransaction() {
        // Arrange
        when(asyncClient.complete(eq(message), eq(transactionContext))).thenReturn(Mono.empty());

        // Act
        client.complete(message, transactionContext);

        // Assert
        verify(asyncClient).complete(eq(message), eq(transactionContext));
    }

    @Test
    void completeMessage() {
        // Arrange
        when(asyncClient.complete(eq(message))).thenReturn(Mono.empty());

        // Act
        client.complete(message);

        // Assert
        verify(asyncClient).complete(eq(message));
    }

    @Test
    void deferMessage() {
        // Arrange
        when(asyncClient.defer(eq(message))).thenReturn(Mono.empty());

        // Act
        client.defer(message);

        // Assert
        verify(asyncClient).defer(eq(message));
    }

    @Test
    void deferMessageWithPropertiesWithTransaction() {
        // Arrange
        when(asyncClient.defer(eq(message), eq(propertiesToModify), eq(transactionContext))).thenReturn(Mono.empty());

        // Act
        client.defer(message, propertiesToModify, transactionContext);

        // Assert
        verify(asyncClient).defer(eq(message), eq(propertiesToModify), eq(transactionContext));
    }

    @Test
    void deferMessageWithProperties() {
        // Arrange
        when(asyncClient.defer(eq(message), eq(propertiesToModify))).thenReturn(Mono.empty());

        // Act
        client.defer(message, propertiesToModify);

        // Assert
        verify(asyncClient).defer(eq(message), eq(propertiesToModify));
    }

    @Test
    void deadLetterMessage() {
        // Arrange
        when(asyncClient.deadLetter(eq(message))).thenReturn(Mono.empty());

        // Act
        client.deadLetter(message);

        // Assert
        verify(asyncClient).deadLetter(eq(message));
    }

    @Test
    void deadLetterMessageWithOptionsWithTransaction() {
        // Arrange
        final DeadLetterOptions options = new DeadLetterOptions()
            .setDeadLetterErrorDescription("foo")
            .setDeadLetterReason("bar")
            .setPropertiesToModify(propertiesToModify);

        when(asyncClient.deadLetter(eq(message), any(DeadLetterOptions.class), any(ServiceBusTransactionContext.class)))
            .thenReturn(Mono.empty());

        // Act
        client.deadLetter(message, options, transactionContext);

        // Assert
        verify(asyncClient).deadLetter(eq(message), eq(options), eq(transactionContext));
    }

    @Test
    void deadLetterMessageWithOptions() {
        // Arrange
        final DeadLetterOptions options = new DeadLetterOptions()
            .setDeadLetterErrorDescription("foo")
            .setDeadLetterReason("bar")
            .setPropertiesToModify(propertiesToModify);

        when(asyncClient.deadLetter(eq(message), any(DeadLetterOptions.class)))
            .thenReturn(Mono.empty());

        // Act
        client.deadLetter(message, options);

        // Assert
        verify(asyncClient).deadLetter(eq(message), eq(options));
    }

    @Test
    void getSessionState() {
        // Arrange
        final String sessionId = "a-session-id";
        final byte[] contents = new byte[]{10, 111, 23};
        when(asyncClient.getSessionState(sessionId)).thenReturn(Mono.just(contents));

        // Act
        final byte[] actual = client.getSessionState(sessionId);

        // Assert
        assertEquals(contents, actual);
    }

    @Test
    void getSessionStateNull() {
        // Arrange
        final String sessionId = "a-session-id";
        when(asyncClient.getSessionState(sessionId)).thenReturn(Mono.empty());

        // Act
        final byte[] actual = client.getSessionState(sessionId);

        // Assert
        assertNull(actual);
    }

    @Test
    void peekMessage() {
        // Arrange
        final ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        when(asyncClient.peekMessage()).thenReturn(Mono.just(message));

        // Act
        final ServiceBusReceivedMessage actual = client.peekMessage();

        // Assert
        assertEquals(message, actual);
    }

    @Test
    void peekMessageFromSequence() {
        // Arrange
        final long sequenceNumber = 154;
        final ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        when(asyncClient.peekMessageAt(sequenceNumber)).thenReturn(Mono.just(message));

        // Act
        final ServiceBusReceivedMessage actual = client.peekMessageAt(sequenceNumber);

        // Assert
        assertEquals(message, actual);
    }

    /**
     * Verifies that all requested messages are returned when we can satisfy them all.
     */
    @Test
    void peekBatchMessagesMax() {
        // Arrange
        final int maxMessages = 10;
        Flux<ServiceBusReceivedMessage> messages = Flux.create(sink -> {
            final AtomicInteger emittedMessages = new AtomicInteger();

            sink.onRequest(number -> {
                logger.info("Requesting {} messages.", number);
                if (emittedMessages.get() >= maxMessages) {
                    logger.info("Completing sink.");
                    sink.complete();
                    return;
                }

                for (int i = 0; i < number; i++) {
                    sink.next(mock(ServiceBusReceivedMessage.class));

                    final int emit = emittedMessages.incrementAndGet();
                    if (emit >= maxMessages) {
                        logger.info("Completing sink.");
                        sink.complete();
                        break;
                    }
                }
            });
        });

        when(asyncClient.peekMessages(maxMessages)).thenReturn(messages);

        // Act
        final IterableStream<ServiceBusReceivedMessage> actual = client.peekMessages(maxMessages);

        // Assert
        assertNotNull(actual);

        final List<ServiceBusReceivedMessage> collected = actual.stream().collect(Collectors.toList());
        assertEquals(maxMessages, collected.size());
    }

    /**
     * Verifies that the messages completes when time has elapsed.
     */
    @Test
    void peekBatchMessagesLessThan() {
        // Arrange
        final int maxMessages = 10;
        final int returnedMessages = 7;
        Flux<ServiceBusReceivedMessage> messages = Flux.create(sink -> {
            final AtomicInteger emittedMessages = new AtomicInteger();

            sink.onRequest(number -> {
                logger.info("Requesting {} messages.", number);
                if (emittedMessages.get() >= returnedMessages) {
                    logger.info("Completing sink. Max: {}", returnedMessages);
                    sink.complete();
                    return;
                }

                for (int i = 0; i < number; i++) {
                    sink.next(mock(ServiceBusReceivedMessage.class));

                    final int emit = emittedMessages.incrementAndGet();
                    if (emit >= returnedMessages) {
                        logger.info("Completing sink.", returnedMessages);
                        sink.complete();
                        break;
                    }
                }
            });
        });

        when(asyncClient.peekMessages(maxMessages)).thenReturn(messages);

        // Act
        final IterableStream<ServiceBusReceivedMessage> actual = client.peekMessages(maxMessages);

        // Assert
        assertNotNull(actual);

        final long collected = actual.stream().count();
        assertEquals(returnedMessages, collected);
    }

    /**
     * Verifies that all requested messages are returned when we can satisfy them all.
     */
    @Test
    void peekBatchMessagesMaxSequenceNumber() {
        // Arrange
        final int maxMessages = 10;
        final long sequenceNumber = 100;
        final Flux<ServiceBusReceivedMessage> messages = Flux.create(sink -> sink.onRequest(number -> {
            for (int i = 0; i < maxMessages; i++) {
                sink.next(mock(ServiceBusReceivedMessage.class));
            }

            sink.complete();
        }));
        when(asyncClient.peekMessagesAt(maxMessages, sequenceNumber)).thenReturn(messages);

        // Act
        final IterableStream<ServiceBusReceivedMessage> actual = client.peekMessagesAt(maxMessages, sequenceNumber);

        // Assert
        assertNotNull(actual);

        final List<ServiceBusReceivedMessage> collected = actual.stream().collect(Collectors.toList());
        assertEquals(maxMessages, collected.size());
    }

    /**
     * Verifies we cannot pass null value for maxWaitTime while receiving.
     */
    @Test
    void receiveMessageNullWaitTime() {
        // Arrange
        final int maxMessages = 10;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> client.receiveMessages(maxMessages, null));
    }

    /**
     * Verifies we cannot pass negative value for maxWaitTime while receiving.
     */
    @Test
    void receiveMessageNegativeWaitTime() {
        // Arrange
        final int maxMessages = 10;
        Duration negativeReceiveWaitTime = Duration.ofSeconds(-10);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> client.receiveMessages(maxMessages, negativeReceiveWaitTime));
    }

    /**
     * Verifies that all requested messages are returned when we can satisfy them all.
     */
    @Test
    void receiveMessagesWithUserSpecifiedTimeout() {
        // Arrange
        final int maxMessages = 10;
        final int numberToEmit = 5;
        final Duration receiveTimeout = Duration.ofSeconds(2);
        final AtomicInteger emittedMessages = new AtomicInteger();
        Flux<ServiceBusReceivedMessageContext> messageSink = Flux.create(sink -> {
            sink.onRequest(e -> {
                if (emittedMessages.get() >= numberToEmit) {
                    logger.info("Cannot emit more. Reached max already. Emitted: {}. Max: {}",
                        emittedMessages.get(), numberToEmit);
                    return;
                }

                for (int i = 0; i < numberToEmit; i++) {
                    ServiceBusReceivedMessageContext context = new ServiceBusReceivedMessageContext(
                        mock(ServiceBusReceivedMessage.class));
                    sink.next(context);

                    final int emit = emittedMessages.incrementAndGet();
                    if (emit >= numberToEmit) {
                        logger.info("Cannot emit more. Reached max already. Emitted: {}. Max: {}", emit, maxMessages);
                        break;
                    }
                }
            });

            sink.onCancel(() -> {
                logger.info("Cancelled. Completing sink.");
                sink.complete();
            });
        });
        when(asyncClient.receiveMessages()).thenReturn(messageSink);

        // Act
        final IterableStream<ServiceBusReceivedMessageContext> actual = client.receiveMessages(maxMessages, receiveTimeout);

        // Assert
        assertNotNull(actual);

        final long collected = actual.stream().count();
        assertEquals(numberToEmit, collected);
    }

    /**
     * Verifies that all requested messages are returned when we can satisfy them all.
     */
    @Test
    void receiveMessagesMax() {
        // Arrange
        final int maxMessages = 10;
        final int numberToEmit = maxMessages + 5;
        Flux<ServiceBusReceivedMessageContext> messageSink = Flux.create(sink -> {
            sink.onRequest(e -> {
                final AtomicInteger emittedMessages = new AtomicInteger();
                if (emittedMessages.get() >= numberToEmit) {
                    logger.info("Cannot emit more. Reached max already. Emitted: {}. Max: {}",
                        emittedMessages.get(), numberToEmit);
                    return;
                }

                for (int i = 0; i < numberToEmit; i++) {
                    sink.next(new ServiceBusReceivedMessageContext(mock(ServiceBusReceivedMessage.class)));

                    final int emit = emittedMessages.incrementAndGet();
                    if (emit >= numberToEmit) {
                        logger.info("Cannot emit more. Reached max already. Emitted: {}. Max: {}", emit, maxMessages);
                        break;
                    }
                }
            });

            sink.onCancel(() -> {
                logger.info("Cancelled. Completing sink.");
                sink.complete();
            });
        });

        when(asyncClient.receiveMessages()).thenReturn(messageSink);

        // Act
        final IterableStream<ServiceBusReceivedMessageContext> actual = client.receiveMessages(maxMessages);

        // Assert
        assertNotNull(actual);

        final long collected = actual.stream().count();
        assertEquals(maxMessages, collected);
    }

    /**
     * Verifies that all requested messages are returned when we can satisfy them all.
     */
    @Test
    void receiveMessagesTimeout() {
        // Arrange
        final int maxMessages = 10;
        final int numberToEmit = 5;

        final AtomicInteger emittedMessages = new AtomicInteger();
        Flux<ServiceBusReceivedMessageContext> messageSink = Flux.create(sink -> {
            sink.onRequest(e -> {
                if (emittedMessages.get() >= numberToEmit) {
                    logger.info("Cannot emit more. Reached max already. Emitted: {}. Max: {}",
                        emittedMessages.get(), numberToEmit);
                    return;
                }

                for (int i = 0; i < numberToEmit; i++) {
                    sink.next(new ServiceBusReceivedMessageContext(mock(ServiceBusReceivedMessage.class)));

                    final int emit = emittedMessages.incrementAndGet();
                    if (emit >= numberToEmit) {
                        logger.info("Cannot emit more. Reached max already. Emitted: {}. Max: {}", emit, maxMessages);
                        break;
                    }
                }
            });

            sink.onCancel(() -> {
                logger.info("Cancelled. Completing sink.");
                sink.complete();
            });
        });
        when(asyncClient.receiveMessages()).thenReturn(messageSink);

        // Act
        final IterableStream<ServiceBusReceivedMessageContext> actual = client.receiveMessages(maxMessages);

        // Assert
        assertNotNull(actual);

        final long collected = actual.stream().count();
        assertEquals(numberToEmit, collected);
    }

    @Test
    void receiveDeferredMessage() {
        // Arrange
        final long sequenceNumber = 231412;
        final ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        when(asyncClient.receiveDeferredMessage(anyLong())).thenReturn(Mono.just(message));

        // Act
        final ServiceBusReceivedMessage actual = client.receiveDeferredMessage(sequenceNumber);

        // Assert
        assertEquals(message, actual);

        verify(asyncClient).receiveDeferredMessage(sequenceNumber);
    }

    @Test
    void receiveDeferredMessageBatch() {
        // Arrange
        final long sequenceNumber = 154;
        final long sequenceNumber2 = 13124;
        final ServiceBusReceivedMessage message = mock(ServiceBusReceivedMessage.class);
        final ServiceBusReceivedMessage message2 = mock(ServiceBusReceivedMessage.class);
        when(asyncClient.receiveDeferredMessages(any())).thenReturn(Flux.just(message, message2));
        List<Long> collection = Arrays.asList(sequenceNumber, sequenceNumber2);

        // Act
        final IterableStream<ServiceBusReceivedMessage> actual = client.receiveDeferredMessageBatch(collection);

        // Assert
        assertNotNull(actual);

        final List<ServiceBusReceivedMessage> collected = actual.stream().collect(Collectors.toList());
        assertEquals(2, collected.size());
        assertEquals(message, collected.get(0));
        assertEquals(message2, collected.get(1));
    }

    @Test
    void renewMessageLock() {
        // Arrange
        final OffsetDateTime response = Instant.ofEpochSecond(1585259339).atOffset(ZoneOffset.UTC);
        when(asyncClient.renewMessageLock(message)).thenReturn(Mono.just(response));

        // Act
        final OffsetDateTime actual = client.renewMessageLock(message);

        // Assert
        assertEquals(response, actual);
    }

    @Test
    void renewSessionLock() {
        // Arrange
        final String sessionId = "a-session-id";
        final OffsetDateTime response = Instant.ofEpochSecond(1585259339).atOffset(ZoneOffset.UTC);
        when(asyncClient.renewSessionLock(sessionId)).thenReturn(Mono.just(response));

        // Act
        final OffsetDateTime actual = client.renewSessionLock(sessionId);

        // Assert
        assertEquals(response, actual);
    }

    @Test
    void setSessionState() {
        // Arrange
        final String sessionId = "a-session-id";
        final byte[] contents = new byte[]{10, 111, 23};
        when(asyncClient.setSessionState(sessionId, contents)).thenReturn(Mono.empty());

        // Act
        client.setSessionState(sessionId, contents);

        // Assert
        verify(asyncClient).setSessionState(sessionId, contents);
    }
}
