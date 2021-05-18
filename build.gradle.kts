plugins {
    kotlin("jvm") version "1.5.0"
    id("com.github.hierynomus.license") version "0.15.0"
    application
}

group = "com.github.jonathanxd"
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
    enabled = false
}

tasks.withType<Jar>() {
    dependsOn(gradle.includedBuild("dokka-doc-io-gradle").task(":jar"))
}

tasks.withType<nl.javadude.gradle.plugins.license.License> {
    header = rootProject.file("LICENSE")
    strictCheck = true
}
