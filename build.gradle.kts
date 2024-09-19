// Disable @Incubation warnings.
@file:Suppress("UnstableApiUsage")

import com.adarshr.gradle.testlogger.theme.ThemeType
import java.util.*

plugins {
    java
    `maven-publish`
    id("com.adarshr.test-logger") version "4.0.0"
}

object GITHUB {
    const val USERNAME_VAR = "GITHUB_USERNAME"
    const val TOKEN_VAR = "GITHUB_TOKEN"
}

/**
 * Fetch a property denoted by _key_.
 *
 * The function will first attempt to extract the property from Gradle's property repository.
 * If either the property does not exist or is empty, an environment variable lookup is used as fallback.
 *
 * @param key Property key.
 * @param defaultValue Optionally, a default value to use, if value is either undefined or empty. Defaults to `null`.
 * @return Either property value or *defaultValue* if undefined.
 */
fun getProperty(key: String, defaultValue: String? = null): String? {
    var result: String? = System.getProperty(key, defaultValue)
    if (result.isNullOrEmpty()) {
        // Property is not defined by Gradle's properties system. Try environment variable.
        result = System.getenv(key)
        if (result != null && result.isEmpty()) {
            // Environment variable is either undefined or contains empty data. Use fallback value.
            result = defaultValue
        }
    }

    return result
}


// Select Java 17 as target toolchain requirements for tasks that require a tool from the toolchain
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withJavadocJar()
    withSourcesJar()
}

version = "1.0.0-SNAPSHOT"
group = "jack"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.assertj:assertj-core:3.26.3")
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

testlogger {
    theme  = ThemeType.MOCHA
}

//
// Validates that GitHub credentials are available via the environment.
//
tasks.register("validatePublishingCredentials") {
    doFirst {
        val missingProperties: MutableList<String> = LinkedList()

        if (getProperty(GITHUB.USERNAME_VAR).isNullOrEmpty()) {
            missingProperties.add(GITHUB.USERNAME_VAR)
        }

        if (getProperty(GITHUB.TOKEN_VAR).isNullOrEmpty()) {
            missingProperties.add(GITHUB.TOKEN_VAR)
        }

        if (missingProperties.isNotEmpty()) {
            val tokensPart = missingProperties.joinToString(" and ", transform = { "'${it}'" })
            throw GradleException("GitHub credentials error: Missing $tokensPart properties.")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/guynir/jack")
            credentials {
                username = getProperty(GITHUB.USERNAME_VAR)
                password = getProperty(GITHUB.TOKEN_VAR)
            }
        }
    }

    publications {
        register<MavenPublication>("jack") {
            from(components["java"])
        }
    }

}

tasks.withType<PublishToMavenRepository> {
    dependsOn("validatePublishingCredentials")
}