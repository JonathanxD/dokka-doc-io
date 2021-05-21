import io.github.jonathanxd.dokkadocio.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.dokka") version "1.4.32"
    id("io.github.jonathanxd.dokka-doc-io")
    id("com.github.hierynomus.license") version "0.15.0"
    application
    maven
}

group = "io.github.jonathanxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.koresframework.Kores:Kores:4.0.2.base")
    implementation("com.github.koresframework:KoresProxy:2.6.1")
    implementation("com.google.guava:guava:30.1.1-jre")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClassName = "MainKt"
}

tasks.dokkaHtml.configure {
    outputDirectory.set(file("dokka/"))

    dokkaSourceSets {
        configureEach {
            sourceLink {
                localDirectory.set(file("src/main/kotlin"))
                remoteUrl.set(
                    URL(
                        "https://github.com/JonathanxD/dokka-doc-io/blob/master/test-doc-io/src/main/kotlin"
                    )
                )
                remoteLineSuffix.set("#L")
            }

            docs {
                link {
                    jitpackIo("com.github.koresframework:KoresProxy", detectVersion(), PACKAGE_LIST)
                    val guava = urlBuilder {
                        baseUrl = "https://guava.dev/releases/"
                        postfixPath = "/api/docs/" // This is the default value
                        elementListPath = ELEMENT_LIST
                    }

                    url {
                        dependency("com.google.guava:guava", detectVersion())
                        from(guava)
                    }
                }
            }
        }
    }
}

tasks.withType<nl.javadude.gradle.plugins.license.License> {
    header = rootProject.file("LICENSE")
    strictCheck = true
}