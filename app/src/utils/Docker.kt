package utils

import java.lang.Thread.sleep

class Docker {
    private var dockerProcessStart: Process? = null

    fun start() {
        val pb = ProcessBuilder("../start-docker.sh")
        dockerProcessStart = pb.start()
        sleep(3000)
    }

    fun stop() {
        val pb = ProcessBuilder("../stop-docker.sh")
        val dockerProcessStop = pb.start()
        dockerProcessStop?.waitFor()
        dockerProcessStart?.waitFor()
        dockerProcessStart?.destroy()
    }
}