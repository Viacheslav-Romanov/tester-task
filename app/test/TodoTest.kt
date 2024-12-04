import http.TodoClient
import org.junit.Test
import java.lang.Thread.sleep
import ws.WebSocketClient

class TodoTest {
    private val todoClient = TodoClient()

    @Test
    fun `Given zero todos when create todo then check its added`() {
        WebSocketClient.start()
        todoClient.getAllTodos()
        todoClient.createNewTodo(Todo(3, "Test Todo", false))
        todoClient.getAllTodos()
//        assertTrue(true)
        sleep(1000)
    }
}
