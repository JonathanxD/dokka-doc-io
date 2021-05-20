import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ossrhUsername: String by project
val ossrhPassword: String by project

plugins {
    kotlin("jvm") version "1.5.0"
    id("com.github.hierynomus.license") version "0.15.0"
    id("com.gradle.plugin-publish") version "0.14.0"
    id("org.jetbrains.dokka") version "1.4.32"
    application
    `java-gradle-plugin`
    `maven-publish`
    signing
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

tasks.withType<Jar> {
    archiveBaseName.set("dokka-doc-io")
}

val jar by tasks.named<Jar>("jar")

val javadocJar = tasks.create<Jar>("javadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.get().outputDirectory.get())
}

val sourcesJar = tasks.create<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
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

    mavenCoordinates {
        groupId = "com.github.jonathanxd"
        artifactId = "dokka-doc-io"
        version = "1.1.0"
    }
}

artifacts {
    archives(jar)
    archives(javadocJar)
    archives(sourcesJar)
}

signing {
    sign(configurations.archives.get())
}

publishing {
    publications.withType<MavenPublication> {
        groupId = "com.github.jonathanxd"
        artifactId = "dokka-doc-io"
        version = "1.1.0"

        signing.sign(this)
        artifact(javadocJar)
        artifact(sourcesJar)

        pom {
            name.set("Dokka Documentation Extension")
            description.set("Extension for Dokka to easily link javadoc.io and jitpack.io hosted documentations.")
            url.set("https://github.com/JonathanxD/dokka-doc-io")
            packaging = "jar"

            licenses {
                license {
                    name.set("The MIT License")
                    url.set("https://mit-license.org/")
                }
            }
            developers {
                developer {
                    name.set("Jonathan H. R. Lopes")
                    url.set("https://github.com/JonathanxD")
                    email.set("jhrldev@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/JonathanxD/dokka-doc-io.git")
                developerConnection.set("scm:git:ssh://github.com:JonathanxD/dokka-doc-io.git")
                url.set("https://github.com/JonathanxD/dokka-doc-io/tree/master")
            }
        }
    }

    repositories {
        maven {
            name = "sonatype"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
        maven {
            name = "snapshot"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }
        }
    }
}