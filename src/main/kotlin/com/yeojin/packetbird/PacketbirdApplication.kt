package com.yeojin.packetbird

fun mainThread() {
    while (true) {
        println("Hello Thread! ${Thread.currentThread().name}")
    }
}

fun main() {
    val t = Thread(::mainThread)
    t.name = "Test Thread"
    t.isDaemon = true // t thread will die when main thread dies.
    t.start()
    t.join() // waiting for end of t

    println("Hello World!")
}
