# Azure Core OkHttp HTTP plugin library for Java

Azure Core OkHttp HTTP client is a plugin for the `azure-core` HTTP client API.

## Getting started

### Prerequisites

- Java Development Kit (JDK) with version 8 or above

### Include the package

[//]: # ({x-version-update-start;com.azure:azure-core-http-okhttp;current})
```xml
<dependency>
    <groupId>com.azure</groupId>
    <artifactId>azure-core-http-okhttp</artifactId>
    <version>1.3.1</version>
</dependency>
```
[//]: # ({x-version-update-end})

## Key concepts

## Examples

The following sections provide several code snippets covering some of the most common client configuration scenarios.

- [Create a Simple Client](#create-a-simple-client)
- [Create a Client with Proxy](#create-a-client-with-proxy)

### Create a Simple Client

Create an OkHttp client using a connection timeout of 60 seconds and a read timeout of 120 seconds.

<!-- embedme ./src/samples/java/com/azure/core/http/okhttp/ReadmeSamples.java#L23-L23 -->
```java
HttpClient client = new OkHttpAsyncHttpClientBuilder().build();
```

### Create a Client with Proxy

Create an OkHttp client that is using a proxy.

<!-- embedme ./src/samples/java/com/azure/core/http/okhttp/ReadmeSamples.java#L30-L32 -->
```java
HttpClient client = new OkHttpAsyncHttpClientBuilder()
    .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<proxy-host>", 8888)))
    .build();
```

## Troubleshooting

### Enabling Logging

Azure SDKs for Java offer a consistent logging story to help aid in troubleshooting application errors and expedite
their resolution. The logs produced will capture the flow of an application before reaching the terminal state to help
locate the root issue. View the [logging][logging] wiki for guidance about enabling logging.

## Next steps

## Contributing

If you would like to become an active contributor to this project please follow the instructions provided in [Microsoft
Azure Projects Contribution Guidelines](http://azure.github.io/guidelines.html).

1. Fork it
1. Create your feature branch (`git checkout -b my-new-feature`)
1. Commit your changes (`git commit -am 'Add some feature'`)
1. Push to the branch (`git push origin my-new-feature`)
1. Create new Pull Request

<!-- Links -->
[logging]: https://github.com/Azure/azure-sdk-for-java/wiki/Logging-with-Azure-SDK

![Impressions](https://azure-sdk-impressions.azurewebsites.net/api/impressions/azure-sdk-for-java%2Fsdk%2Fcore%2Fazure-core-http-okhttp%2FREADME.png)
