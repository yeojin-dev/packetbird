package com.yeojin.packetbird

import java.util.concurrent.Executors

fun mainThread() {
    while (true) {
        println("Hello Thread! ${Thread.currentThread().name}")
    }
}

fun main(args: Array<String>) {
    val threadAmount = args[0].toInt()
    val executor = Executors.newFixedThreadPool(threadAmount)
    val worker = Runnable { mainThread() }
    executor.execute(worker)

    println("Hello World!")
}
