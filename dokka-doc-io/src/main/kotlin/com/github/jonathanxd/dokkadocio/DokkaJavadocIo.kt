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

fun GradleDokkaSourceSetBuilder.docIo(f: DocIoSourceSetBuilder.() -> Unit) =
    f(DocIoSourceSetBuilder(this))

class DocIoSourceSetBuilder(private val builder: GradleDokkaSourceSetBuilder) {
    fun javadocIo(group: String, name: String, version: String?, elementList: Boolean = false) {
        val baseUrl = "https://javadoc.io/doc/${group}/${name}/${version ?: "latest"}"

        if (elementList) {
            builder.externalDocumentationLink(URL("$baseUrl/"), URL("$baseUrl/element-list"))
        } else {
            builder.externalDocumentationLink(URL("$baseUrl/"))
        }
    }

    fun javadocIo(notation: String, elementList: Boolean = false) =
        notation.split(":").let {
            javadocIo(it[0], it[1], if (it.size > 2) it[2] else null, elementList)
        }

    fun jitpackIo(group: String, name: String, version: String?, elementList: Boolean = false) {
        val baseUrl = "https://jitpack.io/${group.replace(".", "/")}/${name}/${version ?: "latest"}/javadoc"

        if (elementList) {
            builder.externalDocumentationLink(URL("$baseUrl/"), URL("$baseUrl/element-list"))
        } else {
            builder.externalDocumentationLink(URL("$baseUrl/"))
        }
    }

    fun jitpackIo(notation: String, elementList: Boolean = false) =
        notation.split(":").let {
            jitpackIo(it[0], it[1], if (it.size > 2) it[2] else null, elementList)
        }

}