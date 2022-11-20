package com.yeojin.packetbird

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

@Volatile // read data from memory(not cpu cache)
var STOP = false

const val MAIN_THREAD_RUNNING_TIME_MILLS = 100L

fun mainThread() {
    println("Thread starts!")

    // The code below is dangerous! STOP isn't in sync with main thread. So situations below is possible.
    // 1. While statement runs after STOP is set in true.
    // 2. An optimizer ignores STOP condition.
    while (!STOP) {
        println("Hello Thread! ${Thread.currentThread().name}")
    }
    println("Thread ends!")
    // Task does not end until the state is changed
}

fun main(args: Array<String>) {
    val threadAmount = args[0].toInt()
    val executor = Executors.newFixedThreadPool(threadAmount)
    val task1 = FutureTask<Int>(::mainThread, 1)
    val task2 = FutureTask<Int>(::mainThread, 2)

    executor.submit(task1)
    executor.submit(task2)

    Thread.sleep(MAIN_THREAD_RUNNING_TIME_MILLS)

    // bug!
    STOP = true
    println("STOP from main")

    while (true) {
        println("Checking...")
        if (task1.isDone && task2.isDone) {
            println("Result.. task1 ${task1.get()} | task2 ${task2.get()}")
            break
        }
    }

    println("Hello World!")
}
