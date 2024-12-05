import http.TodoClient
import org.junit.After
import org.junit.Before
import utils.Docker
import ws.WebSocketClient

open class BaseTest {
    val todoClient = TodoClient()
    private val docker = Docker()

    @Before
    fun setup() {
        docker.start()
        WebSocketClient.start()
    }

    @After
    fun teardown() {
        docker.stop()
    }
}