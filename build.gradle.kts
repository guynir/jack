// Disable @Incubation warnings.
@file:Suppress("UnstableApiUsage")

plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit Jupiter test framework
            useJUnitJupiter("5.11.0")
        }
    }
}

// Select Java 17 as target toolchain requirements for tasks that require a tool from the toolchain
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
