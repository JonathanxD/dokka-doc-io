import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.0"
    id("com.github.hierynomus.license") version "0.15.0"
    id("com.gradle.plugin-publish") version "0.14.0"
    application
    `java-gradle-plugin`
    maven
}

group = "com.github.jonathanxd"
version = "1.1.0"

repositories {
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
}

dependencies {
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.4.32")
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

gradlePlugin {
    plugins {
        create("dokkaDocIo") {
            id = "com.github.jonathanxd.dokka-doc-io"
            implementationClass = "com.github.jonathanxd.dokkadocio.DokkaDocIo"
        }
    }
}

tasks.withType<nl.javadude.gradle.plugins.license.License> {
    header = rootProject.file("LICENSE")
    strictCheck = true
}

pluginBundle {
    website = "https://github.com/JonathanxD/dokka-doc-io"
    vcsUrl = "https://github.com/JonathanxD/dokka-doc-io"
    description = "Extension functions for Kotlin dokka to link with jitpack.io and javadoc.io"
    tags = listOf("documentation", "dokka-extension", "jitpack.io", "javadoc.io")

    plugins {
        getByName("dokkaDocIo") {
            displayName = "Kotlin Dokka Documentation Extension"
        }
    }
}