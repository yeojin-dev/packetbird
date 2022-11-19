package com.yeojin.packetbird

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

const val REPEAT_NUMBER = 10

fun mainThread() {
    repeat(REPEAT_NUMBER) {
        println("Hello Thread! ${Thread.currentThread().name}")
    }
}

fun main(args: Array<String>) {
    val threadAmount = args[0].toInt()
    val executor = Executors.newFixedThreadPool(threadAmount)
    val task1 = FutureTask<Int>(::mainThread, 1)
    val task2 = FutureTask<Int>(::mainThread, 2)

    executor.submit(task1)
    executor.submit(task2)

    while (true) {
        println("Checking...")
        if (task1.isDone && task2.isDone) {
            println("Result.. task1 ${task1.get()} | task2 ${task2.get()}")
            break
        }
    }

    println("Hello World!")
}
