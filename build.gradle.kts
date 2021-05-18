plugins {
    id("com.github.hierynomus.license") version "0.15.0"
    application
}

group = "com.github.jonathanxd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

}

tasks.withType<Jar>() {
    dependsOn(gradle.includedBuild("dokka-doc-io-gradle").task(":jar"))
}

tasks.withType<nl.javadude.gradle.plugins.license.License> {
    header = rootProject.file("LICENSE")
    strictCheck = true
}
