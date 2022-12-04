package com.yeojin.packetbird

class ServerCore {

    companion object {
        private val threadName = ThreadLocal<String>()

        fun whoAmI() {
            threadName.set("My name is ${Thread.currentThread().id}")

            Thread.sleep(1000);

            println(threadName.get())
        }
    }

}
