# Dokka Documentation Extension

**!!! Waiting plugins.gradle.org approval !!!**

Provides extension for [Kotlin Dokka](https://github.com/Kotlin/dokka) to link with documentations hosted in [javadoc.io](https://javadoc.io) and [jitpack.io](https://jitpack.io).

## Usage

First, you need to apply dokka plugin, as explained in [Dokka Guide](https://kotlin.github.io/dokka/), after than, you need to apply this plugin:

```groovy
plugins {
  id "com.github.jonathanxd.dokka-doc-io" version "1.1.0"
}
```

#### For Kotlin DSL

```kotlin
plugins {
    id("com.github.jonathanxd.dokka-doc-io") version "1.1.0"
}
```

### Defining documentations

The documentation link definition looks like the dependency declaration notation:

```kotlin
tasks.dokkaHtml.configure {
    dokkaSourceSets {
        configureEach {
            linkDocs {
                javadocIo("com.github.jonathanxd:textlexer:1.7")
                jitpackIo("com.github.koresframework:KoresProxy:2.6.1")
            }
        }
    }
}
```

For documentations that uses `element-list` instead of `package-list`, use the following syntax:

```kotlin
tasks.dokkaHtml.configure {
    dokkaSourceSets {
        configureEach {
            linkDocs {
                javadocIo("com.github.jonathanxd:textlexer:1.7", ELEMENT_LIST)
                jitpackIo("com.github.koresframework:KoresProxy:2.6.1", ELEMENT_LIST)
            }
        }
    }
}
```

You could also specify your own `package-list` file:

```kotlin
tasks.dokkaHtml.configure {
    dokkaSourceSets {
        configureEach {
            linkDocs {
                javadocIo("com.github.jonathanxd:textlexer:1.7", "my-package-list")
                jitpackIo("com.github.koresframework:KoresProxy:2.6.1", "my-package-list")
            }
        }
    }
}
```

## Experimental versions

To use experimental versions, you need to declare the plugin in the `buildscript` using `jitpack.io` as a *maven repository*, for example:

```kotlin
buildscript {
    repositories {
        maven(url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.github.JonathanxD.dokka-doc-io:dokka-doc-io-gradle:-SNAPSHOT") {
            isChanging = true
        }
    }
}

plugins {
    kotlin("jvm") version "1.5.0"
    id("org.jetbrains.dokka") version "1.4.32"
}

apply(plugin = "com.github.jonathanxd.dokka-doc-io")
```