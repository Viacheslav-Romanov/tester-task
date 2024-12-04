import utils.Docker

fun main() {
    val docker = Docker()
    println("Starting app...")
    docker.start()
    println("Stoping app...")
    docker.stop()
}