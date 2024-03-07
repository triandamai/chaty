@file:Suppress("UnstableApiUsage")


/*
 * Copyright © 2023 trian.app.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("jvm")
    //should place at the bottom plugins to avoid warning dagger not recognized by any processor
    `maven-publish`
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
java {
    withJavadocJar()
    withSourcesJar()
}

group = "app.trian.mvi.processor"
version = "1.0"
dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.0-1.0.9")
    implementation("com.squareup:kotlinpoet-ksp:1.14.2")
}


afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["java"])
                groupId = "app.trian.mvi.processor"
                version = "1.0"
                artifactId = "processor"
                versionMapping {
                    usage("java-api"){
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime"){
                        fromResolutionResult()
                    }
                }
            }
        }
    }
}