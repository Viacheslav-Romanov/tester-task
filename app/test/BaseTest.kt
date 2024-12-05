import http.TodoClient
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import utils.Docker
import ws.WebSocketClient

open class BaseTest {
    val todoClient = TodoClient()
    private val docker = Docker()

    @BeforeEach
    fun setup() {
        docker.start()
        WebSocketClient.start()
    }

    @AfterEach
    fun teardown() {
        docker.stop()
    }
}