package com.yeojin.packetbird

const val EMPTY_FLAG: Int = 0x00000000
const val WRITE_MASK: Int = 0x7FFF0000
const val READ_MASK: Int = 0x0000FFFF
const val MAX_SPIN_COUNT = 5000

class Lock {
    private var flag = EMPTY_FLAG
    private var writeCount = 0
    fun writeLock() {
        val lockThreadId = (flag and WRITE_MASK) shr 16
        val currentThreadId = Thread.currentThread().id.toInt()
        if (lockThreadId == currentThreadId) {
            writeCount++
            return
        }

        while (true) {
            val desired = (Thread.currentThread().id.toInt() shl 16) and WRITE_MASK
            repeat(MAX_SPIN_COUNT) {
                synchronized(this) {
                    if (flag == EMPTY_FLAG) {
                        flag = desired
                        writeCount++
                        return@writeLock
                    }
                }
            }
            Thread.yield()
        }
    }

    @Synchronized
    fun writeUnlock() {
        if (--writeCount == 0) {
            flag = EMPTY_FLAG
        }
    }

    fun readLock() {
        while (true) {
            repeat(MAX_SPIN_COUNT) {
                synchronized(this) {
                    if ((flag and READ_MASK) == 0) {
                        flag++
                        return@readLock
                    }
                }
            }
            Thread.yield()
        }
    }

    @Synchronized
    fun readUnlock() {
        flag--
    }
}
