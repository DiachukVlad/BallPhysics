import kotlin.concurrent.thread

fun startPhysics() {
    val minSleep = 2

    thread {
        var lastTime = System.currentTimeMillis()
        Thread.sleep(minSleep.toLong())

        while (true) {
            val time = System.currentTimeMillis()
            val delta = time - lastTime
            val restTime = Math.max(0, minSleep-delta)

            Thread.sleep(restTime)
            lastTime = time+restTime

            tick((delta+restTime)/1000f)
        }
    }
}