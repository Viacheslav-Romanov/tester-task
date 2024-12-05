package utils

import java.lang.Thread.sleep
import java.lang.ProcessBuilder.*

class Docker {
    private var dockerProcessStart: Process? = null

    fun start() {
        dockerProcessStart = startProcess(START_DOCKER_COMMAND)
        sleep(3000)
    }

    fun stop() {
        val dockerProcessStop = startProcess(STOP_DOCKER_COMMAND)
        dockerProcessStop.waitFor()
        dockerProcessStart?.waitFor()
    }

    private fun startProcess(command: String): Process {
        val pb = ProcessBuilder("/bin/sh", "-c", command)
            .inheritIO()
        val process = pb.start()
        return process
    }

    companion object {
        const val START_DOCKER_COMMAND = "../start-docker.sh"
        const val STOP_DOCKER_COMMAND = "../stop-docker.sh"
    }
}