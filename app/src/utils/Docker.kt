package utils

import java.lang.Thread.sleep
import java.lang.ProcessBuilder.*

class Docker {
    private var dockerProcessStart: Process? = null

    fun start() {
        val pb = ProcessBuilder("/bin/sh", "-c", START_DOCKER_COMMAND)
            .inheritIO()
        dockerProcessStart = pb.start()
        sleep(3000)
    }

    fun stop() {
        val pb = ProcessBuilder("/bin/sh", "-c", STOP_DOCKER_COMMAND)
            .inheritIO()
        val dockerProcessStop = pb.start()
        dockerProcessStop?.waitFor()
        dockerProcessStart?.waitFor()
    }

    companion object {
        const val START_DOCKER_COMMAND = "../start-docker.sh"
        const val STOP_DOCKER_COMMAND = "../stop-docker.sh"
    }
}