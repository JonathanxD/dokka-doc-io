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
package com.github.jonathanxd.dokkadocio

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.DokkaPlugin
import org.jetbrains.dokka.gradle.GradleDokkaSourceSetBuilder
import java.net.URL

class DokkaDocIo: Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(DokkaPlugin::class.java)
    }
}

/**
 * Extension to specify additional documentation for dependencies with documentation hosted in [jitpack.io](https://jitpack.io)
 * and [javadoc.io](https://javadoc.io).
 */
fun GradleDokkaSourceSetBuilder.docIo(f: DocIoSourceSetBuilder.() -> Unit) =
    f(DocIoSourceSetBuilder(this))

/**
 * Extension to specify additional documentation for dependencies with documentation hosted in [jitpack.io](https://jitpack.io)
 * and [javadoc.io](https://javadoc.io).
 */
fun GradleDokkaSourceSetBuilder.linkDocs(f: DocIoSourceSetBuilder.() -> Unit) =
    f(DocIoSourceSetBuilder(this))

class DocIoSourceSetBuilder(private val builder: GradleDokkaSourceSetBuilder) {
    val PACKAGE_LIST = "package-list"
    val ELEMENT_LIST = "package-list"

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
     * Links to a documentation hosted in [javadoc.io](https://javadoc.io).
     *
     * Usage example:
     *
     * ```kotlin
     * javadocIo("com.github.jonathanxd:textlexer:1.7")
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     */
    fun javadocIo(notation: String, elementListPath: String = PACKAGE_LIST) =
        notation.split(":").let {
            javadocIo(it[0], it[1], if (it.size > 2) it[2] else null, elementListPath)
        }

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
     * Links to a documentation hosted in [jitpack.io](https://jitpack.io/docs/#javadoc-publishing).
     *
     * Usage example:
     *
     * ```kotlin
     * jitpackIo("com.github.koresframework:KoresProxy:2.6.1")
     * ```
     *
     * If the version is omitted, the latest documentation will be linked.
     *
     * @param elementListPath The path to the page which lists the packages that is documented. The most common
     * path is `package-list`, however, some documentations uses `element-list`. In the cases where the documentation
     * does not use the default one (which is `package-list`) you must provide the accordingly path.
     */
    fun jitpackIo(notation: String, elementListPath: String = PACKAGE_LIST) =
        notation.split(":").let {
            jitpackIo(it[0], it[1], if (it.size > 2) it[2] else null, elementListPath)
        }

}