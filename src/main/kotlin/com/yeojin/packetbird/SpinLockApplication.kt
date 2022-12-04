package com.yeojin.packetbird

import java.util.concurrent.Executors
import java.util.concurrent.FutureTask

class SpinLock {
    companion object {
        @Volatile
        var locked = false
    }

    @Synchronized
    fun acquire() {
        while (locked) {
            // do nothing
        }
        locked = true
    }

    fun release() {
        locked = false
    }
}

var num = 0
val spinLock = SpinLock()
const val LOOP_COUNT = 1000000

fun thread1() {
    repeat(LOOP_COUNT) {
        spinLock.acquire()
        num++
        spinLock.release()
    }
}

fun thread2() {
    repeat(LOOP_COUNT) {
        spinLock.acquire()
        num--
        spinLock.release()
    }
}

val readWriteLock = Lock()

fun threadL1() {
    repeat(LOOP_COUNT) {
        readWriteLock.writeLock()
        readWriteLock.writeLock()
        num++
        readWriteLock.writeUnlock()
        readWriteLock.writeUnlock()
    }
}

fun threadL2() {
    repeat(LOOP_COUNT) {
        readWriteLock.writeLock()
        num--
        readWriteLock.writeUnlock()
    }
}

fun main() {
    val t1 = FutureTask<Int>(::threadL1, 0)
    val t2 = FutureTask<Int>(::threadL2, 1)

    val threadPool = Executors.newFixedThreadPool(2)
    threadPool.submit(t1)
    threadPool.submit(t2)

    while (true) {
        if (t1.isDone && t2.isDone) {
            println(num)
            break
        }
    }
}
