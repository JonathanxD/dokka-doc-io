/**
 * dokka-doc-io - Extension functions for Kotlin Dokka to support jitpack.io and javadoc.io
 * Copyright 2021 JonathanxD <jhrldev@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.jonathanxd.dokkadocio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder
import java.net.URL

class DokkaDocIo : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(DokkaPlugin::class.java)
    }
}

/**
 * Documentation specification for specific project.
 *
 * ## Usage example:
 *
 * ```kotlin
 * tasks.dokkaHtml.configure {
 *     outputDirectory.set(file("dokka/"))
 *     dokkaSourceSets {
 *         configureEach {
 *             docs {
 *                 links {
 *                     jitpackIo("com.github.koresframework:KoresProxy:2.6.1", ELEMENT_LIST)
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 * @since 1.2
 */
fun Project.docs(f: Docs.() -> Unit) = f(Docs(project))

/**
 * Documentation specification for specific project.
 *
 * ## Usage example:
 *
 * ```kotlin
 * tasks.dokkaHtml.configure {
 *     outputDirectory.set(file("dokka/"))
 *     dokkaSourceSets {
 *         configureEach {
 *             docs {
 *                 links {
 *                     jitpackIo("com.github.koresframework:KoresProxy:2.6.1", ELEMENT_LIST)
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 * @since 1.2
 */
class Docs(private val project: Project) {
    /**
     * Extension to specify additional documentation for dependencies with documentation hosted in [jitpack.io](https://jitpack.io)
     * and [javadoc.io](https://javadoc.io).
     */
    fun GradleDokkaSourceSetBuilder.link(f: DocIoSourceSetBuilder.() -> Unit) =
        f(DocIoSourceSetBuilder(project, this))
}

class DocIoSourceSetBuilder(private val project: Project, private val builder: GradleDokkaSourceSetBuilder) {
    val PACKAGE_LIST = "package-list"
    val ELEMENT_LIST = "element-list"

    /**
     * Links to a documentation hosted in [javadoc.io](https://javadoc.io).
     *
     * Usage example:
     *
     * ```kotlin
     * javadocIo(group = "com.github.jonathanxd", name = "textlexer", version = "1.7")
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     */
    fun javadocIo(group: String, name: String, version: String?, elementListPath: String = PACKAGE_LIST) {
        val baseUrl = "https://javadoc.io/doc/${group}/${name}/${version ?: "latest"}"

        builder.externalDocumentationLink(URL("$baseUrl/"), URL("$baseUrl/$elementListPath"))
    }

    /**
     * Links to a documentation hosted in [javadoc.io](https://javadoc.io) automatically detecting the version from
     * declared `dependencies`.
     *
     * Usage example:
     *
     * ```kotlin
     * javadocIo(group = "com.github.jonathanxd", name = "textlexer", version = detectVersion())
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     * @since 1.2.0
     */
    fun javadocIo(group: String, name: String, detectVersion: DetectVersion, elementListPath: String = PACKAGE_LIST) =
        jitpackIo(group, name, detectVersion.detect(project, group, name), elementListPath)

    /**
     * Links to a documentation hosted in [javadoc.io](https://javadoc.io) automatically detecting the version from
     * declared `dependencies`.
     *
     * Usage example:
     *
     * ```kotlin
     * javadocIo("com.github.jonathanxd:textlexer", detectVersion())
     * ```
     *
     * The version will be resolved from dependencies declared in the `dependencies` DSL, if [detectVersion] is set.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     * @since 1.2.0
     */
    fun javadocIo(notation: String, detectVersion: DetectVersion? = null, elementListPath: String = PACKAGE_LIST) =
        notation.split(":").let {
            javadocIo(
                it[0],
                it[1],
                if (detectVersion != null) detectVersion.detect(
                    project,
                    it[0],
                    it[1]
                ) else if (it.size > 2) it[2] else null,
                elementListPath
            )
        }

    /**
     * Links to a documentation hosted in [javadoc.io](https://javadoc.io).
     *
     * Usage example:
     *
     * ```kotlin
     * javadocIo("com.github.jonathanxd:textlexer", DetectVersion)
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     * @since 1.2.0
     */
    fun javadocIo(notation: String, elementListPath: String = PACKAGE_LIST) =
        javadocIo(notation, detectVersion = null, elementListPath = elementListPath)

    /**
     * Links to a documentation hosted in [jitpack.io](https://jitpack.io/docs/#javadoc-publishing).
     *
     * Usage example:
     *
     * ```kotlin
     * jitpackIo(group = "com.github.koresframework", name = "KoresProxy", version = "2.6.1")
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     */
    fun jitpackIo(group: String, name: String, version: String?, elementListPath: String = PACKAGE_LIST) {
        val baseUrl = "https://jitpack.io/${group.replace(".", "/")}/${name}/${version ?: "latest"}/javadoc"

        builder.externalDocumentationLink(URL("$baseUrl/"), URL("$baseUrl/$elementListPath"))
    }

    /**
     * Links to a documentation hosted in [jitpack.io](https://jitpack.io/docs/#javadoc-publishing) automatically
     * detecting the version from declared `dependencies`.
     *
     * Usage example:
     *
     * ```kotlin
     * jitpackIo(group = "com.github.koresframework", name = "KoresProxy", version = DetectVersion)
     * ```
     *
     * The version will be resolved from dependencies declared in the `dependencies` DSL.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     * @since 1.2.0
     */
    fun jitpackIo(group: String, name: String, detectVersion: DetectVersion, elementListPath: String = PACKAGE_LIST) =
        jitpackIo(group, name, detectVersion.detect(project, group, name), elementListPath)

    /**
     * Links to a documentation hosted in [jitpack.io](https://jitpack.io/docs/#javadoc-publishing).
     *
     * Usage example:
     *
     * ```kotlin
     * jitpackIo("com.github.koresframework:KoresProxy:2.6.1", PACKAGE_LIST)
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     */
    fun jitpackIo(notation: String, elementListPath: String = PACKAGE_LIST) =
        jitpackIo(notation, detectVersion = null, elementListPath = elementListPath)


    /**
     * Links to a documentation hosted in [jitpack.io](https://jitpack.io/docs/#javadoc-publishing) automatically
     * detecting the version from declared `dependencies`.
     *
     * Usage example:
     *
     * ```kotlin
     * jitpackIo("com.github.koresframework:KoresProxy", detectVersion())
     * ```
     *
     * The version will be resolved from dependencies declared in the `dependencies` DSL, if [detectVersion] is set.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     * @since 1.2.0
     */
    fun jitpackIo(notation: String, detectVersion: DetectVersion? = null, elementListPath: String = PACKAGE_LIST) =
        notation.split(":").let {
            jitpackIo(
                it[0],
                it[1],
                if (detectVersion != null) detectVersion.detect(
                    project,
                    it[0],
                    it[1]
                ) else if (it.size > 2) it[2] else null,
                elementListPath
            )
        }

    /**
     * "Creates a builder" to share with different declarations of [url] that shares the same `host` commons.
     *
     * Usage example:
     *
     * ```kotlin
     * val mySiteDocs = urlBuilder {
     *     baseUrl = "https://docs.mysite.com/versions" // The site URL and prefix path for documentation
     *     postfixPath = "/api/docs" // The postfix path of documentation, appended after the version. This is the default path.
     *     elementListPath = ELEMENT_LIST // for javadoc generated by newer versions of javadoc tool.
     * }
     *
     * url {
     *     from(mySiteDocs)
     *     dependency("com.mysite.group:sample-project", detectVersion()) // Detects the dependency version from `dependencies` DSL.
     * }
     * ```
     *
     * You could use [DocumentationUrlBuilder.prefixPath] when you need to append a path to [DocumentationUrlBuilder.baseUrl]:
     *
     * ```kotlin
     * val mySiteDocs = urlBuilder {
     *     baseUrl = "https://docs.mysite.com/versions" // The site URL and prefix path for documentation
     *     postfixPath = "/api/docs" // The postfix path of documentation, appended after the version. `/api/docs` is the default path.
     *     elementListPath = ELEMENT_LIST // for javadoc generated by newer versions of javadoc tool.
     * }
     *
     * url {
     *     from(mySiteDocs)
     *     dependency("com.mysite.group:sample-project", detectVersion()) // Detects the dependency version from `dependencies` DSL.
     *     prefixPath = "$dependencyGroupPath/$dependencyName"
     * }
     * ```
     *
     * And, since `urlBuilder` just returns the same provided [builder], you could place the `from` after `dependency` and
     * have some kind of "placeholders" inside the shareds commons builder:
     *
     * ```kotlin
     * val mySiteDocs = urlBuilder {
     *     baseUrl = "https://docs.mysite.com/versions" // The site URL and prefix path for documentation
     *     prefixPath = "$dependencyGroupPath/$dependencyName"
     *     postfixPath = "/api/docs" // The postfix path of documentation, appended after the version. `/api/docs` is the default path.
     *     elementListPath = ELEMENT_LIST // for javadoc generated by newer versions of javadoc tool.
     * }
     *
     * url {
     *     dependency("com.mysite.group:sample-project", detectVersion()) // Detects the dependency version from `dependencies` DSL.
     *     from(mySiteDocs)
     * }
     * ```
     *
     * @since 1.2
     */
    fun urlBuilder(builder: DocumentationUrlBuilder.() -> Unit): DocumentationUrlBuilder.() -> Unit {
        return builder
    }


    /**
     * Links to a documentation hosted in a custom url.
     *
     * Usage example:
     *
     * ```kotlin
     * url {
     *     baseUrl = "https://docs.mysite.com/versions" // The site URL and prefix path for documentation
     *     dependency("com.mysite.group:sample-project", DetectVersion) // Detects the dependency version from `dependencies` DSL.
     *     postfixPath = "/api/docs" // The postfix path of documentation, appended after the version. `/api/docs` is the default path.
     *     elementListPath = ELEMENT_LIST // for javadoc generated by newer versions of javadoc tool.
     * }
     * ```
     *
     * You could also use the `group` and `name` resolved by the `dependency` function in your base path:
     *
     * ```kotlin
     * url {
     *     dependency("com.mysite.group:sample-project", detectVersion())
     *     baseUrl = "https://docs.mysite.com/versions/$dependencyGroupPath/$dependencyName" // Must be after `dependency`
     *     postfixPath = "/api/docs"
     *     elementListPath = ELEMENT_LIST
     * }
     * ```
     *
     * @since 1.2
     */
    fun url(builder: DocumentationUrlBuilder.() -> Unit): DocumentationUrlBuilder {
        val documentationUrlBuilder = DocumentationUrlBuilder(this.project, this.builder)
        builder(documentationUrlBuilder)
        documentationUrlBuilder.build()

        return documentationUrlBuilder
    }

    /**
     * Detect version from [project] `dependencies` DSL.
     */
    fun detectVersion() = DetectVersion
}

/**
 * Builder links to documentation hosted in custom url.
 *
 * Usage example:
 *
 * ```kotlin
 * url {
 *     baseUrl = "https://docs.mysite.com/versions" // The site URL and prefix path for documentation
 *     dependency("com.mysite.group:sample-project", DetectVersion) // Detects the dependency from `dependencies` DSL.
 *     postfixPath = "/api/docs" // The postfix path of documentation, appended after the version. `/api/docs` is the default path.
 *     elementListPath = ELEMENT_LIST // for javadoc generated by newer versions of javadoc tool.
 * }
 * ```
 *
 * You could also use the `group` and `name` resolved by the `dependency` function in your base path:
 *
 * ```kotlin
 * url {
 *     dependency("com.mysite.group:sample-project", DetectVersion)
 *     baseUrl = "https://docs.mysite.com/$dependencyGroupPath/$dependencyName/versions" // Must be after `dependency`
 *     postfixPath = "/api/docs"
 *     elementListPath = ELEMENT_LIST
 * }
 * ```
 *
 * @since 1.2
 */
class DocumentationUrlBuilder(private val project: Project, private val builder: GradleDokkaSourceSetBuilder) {
    val PACKAGE_LIST = "package-list"
    val ELEMENT_LIST = "element-list"

    /**
     * The host and prefix path of the documentation host site. For example: `https://docs.mysite.com/`.
     *
     * @since 1.2
     */
    lateinit var baseUrl: String

    /**
     * The prefix path appended after [baseUrl] and before [dependencyVersion]. Can be used to specify
     * the documentation path when sharing commons using [urlBuilder][DocIoSourceSetBuilder.urlBuilder].
     *
     * @since 1.2
     */
    var prefixPath: String? = null

    /**
     * The group of the dependency that this declaration is linking the documentation. For example: `com.mysite.docs`.
     *
     * This value can be configured using [dependency].
     *
     * @since 1.2
     */
    var dependencyGroup: String? = null

    /**
     * The name of the dependency that this declaration is linking the documentation. For example: `sample-project`.
     *
     * This value can be configured using [dependency].
     *
     * @since 1.2
     */
    var dependencyName: String? = null

    /**
     * The version of dependency that this declaration is linking the documentation/The documentation version.
     * For example: `1.0.0`.
     *
     * When set to `null`, the `latest` value is used.
     *
     * This value can be configured using [dependency].
     *
     * @since 1.2
     */
    var dependencyVersion: String? = null

    /**
     * The postfix path of the documentation url. For example: `api/docs`.
     *
     * This value is appended at the end of the url, after the [dependencyVersion].
     *
     * @since 1.2
     */
    var postfixPath: String = "/api/docs"

    /**
     * The path to the list of packages that this documentation hosts. Commonly [`package-list`][PACKAGE_LIST],
     * but newer versions of javadoc tools generates a file named [`element-list`][ELEMENT_LIST].
     */
    var elementListPath: String = PACKAGE_LIST

    /**
     * The path of the dependency group. This is the same value as [dependencyGroup] but replacing `.` (dots)
     * with `/` (slash). This is used to construct the documentation url.
     *
     * @since 1.2
     */
    val dependencyGroupPath: String?
        get() = this.dependencyGroup?.replace(".", "/")

    /**
     * Derives the definitions of [documentationUrlBuilder], can be used to share a common documentation host between
     * different declarations.
     *
     * @since 1.2
     */
    fun from(documentationUrlBuilder: DocumentationUrlBuilder.() -> Unit) {
        documentationUrlBuilder(this)
    }

    /**
     * Defines the dependency group, name and version to be used to construct the documentation url.
     *
     * ## Example
     *
     * ```kotlin
     * url {
     *     dependency(group = "com.mysite.group", name = "sample-project", version = "1.0.0")
     * }
     * ```
     *
     * @since 1.2
     */
    fun dependency(group: String, name: String, version: String? = null) {
        this.dependencyGroup = group
        this.dependencyName = name
        this.dependencyVersion = version
    }

    /**
     * Defines the dependency group, name and version to be used to construct the documentation url.
     *
     * The version is detected from the project `dependencies` DSL.
     *
     * ## Example
     *
     * ```kotlin
     * url {
     *     dependency("com.mysite.group:sample-project", detectVersion())
     * }
     * ```
     *
     * @since 1.2
     */
    fun dependency(notation: String, detectVersion: DetectVersion? = null) {
        val parts = notation.split(":")
        dependency(
            parts[0],
            parts[1],
            when {
                detectVersion != null -> detectVersion.detect(project, parts[0], parts[1])
                parts.size > 2 -> parts[2]
                else -> null
            }
        )
    }

    /**
     * Defines the dependency group, name and version to be used to construct the documentation url.
     *
     * The version is detected from the project `dependencies` DSL.
     *
     * ## Example
     *
     * ```kotlin
     * url {
     *     dependency(group = "com.mysite.group", name = "sample-project", detectVersion())
     * }
     * ```
     *
     * @since 1.2
     */
    fun dependency(group: String, name: String, detectVersion: DetectVersion) =
        dependency(group, name, detectVersion.detect(this.project, group, name))

    /**
     * Detect version from [project] `dependencies` DSL.
     *
     * @since 1.2
     */
    fun detectVersion() = DetectVersion

    internal fun build() {
        val baseUrl = "${this.baseUrl.fixUrl()}${prefixPath?.prependSlash() ?: ""}/${this.dependencyVersion ?: "latest"}/${postfixPath.fixPostfixPath()}"
        builder.externalDocumentationLink(URL("$baseUrl/"), URL("$baseUrl/$elementListPath"))
    }
}

internal fun String.prependSlash() =
    (if (this.startsWith("/")) this
    else "/$this").fixUrl()


internal fun String.fixUrl() =
    if (this.endsWith("/")) this.substring(0 until this.lastIndex)
    else this

internal fun String.fixPostfixPath() =
    (if (this.startsWith("/")) this.substring(1..this.lastIndex)
    else this).fixUrl()

/**
 * Detect version from [Project] `dependencies` DSL.
 *
 * @since 1.2
 */
object DetectVersion {
    /**
     * Detect version from [Project] `dependencies` DSL.
     *
     * TODO: Detect different versions in different configurations, like `test` and `main`.
     *
     * @since 1.2
     */
    internal fun detect(project: Project, group: String, name: String): String? {
        for (cfg in project.configurations) {
            return cfg.dependencies.firstOrNull {
                it.group == group && it.name == name
            }?.version ?: continue
        }

        return null
    }
}