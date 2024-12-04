package utils

import java.lang.Thread.sleep

class Docker {
    private var dockerProcess: Process? = null

    fun start() {
        val pb = ProcessBuilder("../start-docker.sh")
        dockerProcess = pb.start()
        sleep(500)
    }

    fun stop() {
        val pb = ProcessBuilder("../stop-docker.sh")
        pb.start()
        dockerProcess?.waitFor()
        dockerProcess?.destroy()
    }
}