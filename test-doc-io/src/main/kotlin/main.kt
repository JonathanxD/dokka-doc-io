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

import com.github.jonathanxd.koresproxy.KoresProxy
import com.github.jonathanxd.koresproxy.ProxyData
import com.github.jonathanxd.koresproxy.gen.DirectInvocationCustom
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.ThreadLocalRandom

object MyProxy {
    val PROXY: Indy = KoresProxy.newProxyInstance {
        it.classLoader(MyProxy::class.java.classLoader)
            .interfaces(Indy::class.java)
            .custom(listOf(DirectInvocationCustom.Instance(How())))
    }
}

interface Indy {
    fun rng(): Int
}

class How {
    fun rng(): Int = ThreadLocalRandom.current().nextInt(0, 100)
}

/**
 * Documented
 */
fun proxyData(o: Any): ProxyData = KoresProxy.getProxyData(o)

fun cache(): Cache<String, Int> = CacheBuilder.newBuilder().build()

fun main(args: Array<String>) {
    val indy = MyProxy.PROXY
    val data = proxyData(indy)
    println("Random: ${indy.rng()}")
    println("Data: $data")
}

