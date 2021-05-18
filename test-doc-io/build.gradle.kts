import com.github.jonathanxd.dokkadocio.docIo
import com.github.jonathanxd.dokkadocio.linkDocs
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.dokka") version "1.4.32"
    id("com.github.jonathanxd.dokka-doc-io")
    id("com.github.hierynomus.license") version "0.15.0"
    application
}

group = "com.github.jonathanxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("com.github.koresframework.Kores:Kores:4.0.2.base")
    implementation("com.github.koresframework:KoresProxy:2.6.1")
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    enabled = false
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

            linkDocs {
                jitpackIo("com.github.koresframework:KoresProxy:2.6.1", ELEMENT_LIST)
            }
        }
    }
}

tasks.withType<nl.javadude.gradle.plugins.license.License> {
    header = rootProject.file("LICENSE")
    strictCheck = true
}