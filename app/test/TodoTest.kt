import http.TodoClient
import org.junit.After
import org.junit.Before
import org.junit.Test
import utils.Docker
import ws.WebSocketClient
import kotlin.test.assertEquals

class TodoTest {
    private val todoClient = TodoClient()
    private val docker = Docker()

    @Before
    fun setup() {
        docker.start()
    }

    @After
    fun teardown() {
        docker.stop()
    }

    @Test
    fun `Given zero todos when create todo then check it's added`() {
        WebSocketClient.start()

        todoClient.getAllTodos()
        val responseCode = todoClient.createNewTodo(Todo(3, "Test Todo", false))
        val todos = todoClient.getAllTodos()

        assertEquals(201, responseCode)
        assertEquals("""[{"id":3,"text":"Test Todo","completed":false}]""", todos)
    }
}
