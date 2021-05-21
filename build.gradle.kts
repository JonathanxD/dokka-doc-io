plugins {
    kotlin("jvm") version "1.5.0"
    id("com.github.hierynomus.license") version "0.15.0"
    application
    maven
}

group = "io.github.jonathanxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar>() {
    dependsOn(gradle.includedBuild("dokka-doc-io-gradle").task(":jar"))
}

tasks.named("install") {
    dependsOn(gradle.includedBuild("dokka-doc-io-gradle").task(":install"))
}

tasks.create("mavenPublish") {
    dependsOn(gradle.includedBuild("dokka-doc-io-gradle").task(":publishPluginMavenPublicationToSonatypeRepository"))
}

tasks.create("gradlePublish") {
    dependsOn(gradle.includedBuild("dokka-doc-io-gradle").task(":publishPlugins"))
}

tasks.withType<nl.javadude.gradle.plugins.license.License> {
    header = rootProject.file("LICENSE")
    strictCheck = true
}