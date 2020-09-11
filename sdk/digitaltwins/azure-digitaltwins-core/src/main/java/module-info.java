// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

module come.azure.digitaltwins.core {
    requires transitive com.azure.core;
    requires com.fasterxml.jackson.annotation;
    requires com.azure.http.netty;

    opens com.azure.digitaltwins.core to come.azure.core, com.azure.http.netty;
    opens com.azure.digitaltwins.core.models to com.fasterxml.jackson.annotation, come.azure.core;
    opens com.azure.digitaltwins.core.util to com.fasterxml.jackson.annotation, come.azure.core;
    opens com.azure.digitaltwins.core.serialization to com.fasterxml.jackson.annotation, come.azure.core;
    opens com.azure.digitaltwins.core.implementation to com.fasterxml.jackson.annotation, come.azure.core;
    opens com.azure.digitaltwins.core.implementation.models to com.fasterxml.jackson.annotation, com.azure.core;
    opens com.azure.digitaltwins.core.implementation.serializer to com.fasterxml.jackson.annotation, com.azure.core;
    opens com.azure.digitaltwins.core.implementation.converters to com.fasterxml.jackson.annotation, com.azure.core;

    exports com.azure.digitaltwins.core;
    exports com.azure.digitaltwins.core.models;
    exports com.azure.digitaltwins.core.serialization;
    exports com.azure.digitaltwins.core.util;
}
