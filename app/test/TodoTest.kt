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

    @Test
    fun `Given 3 todos when get all then check 3 returned`() {
        WebSocketClient.start()

        todoClient.getAllTodos()
        todoClient.createNewTodo(Todo(1, "Test Todo 1", false))
        todoClient.createNewTodo(Todo(2, "Test Todo 2", false))
        todoClient.createNewTodo(Todo(3, "Test Todo 3", true))
        val todos = todoClient.getAllTodos()

        val expected = """
        [{"id":1,"text":"Test Todo 1","completed":false},{"id":2,"text":"Test Todo 2","completed":false},{"id":3,"text":"Test Todo 3","completed":true}]
        """.trim()
        assertEquals(expected, todos)
    }

    @Test
    fun `Given one todo when update todo then check it's updated`() {
        WebSocketClient.start()

        todoClient.getAllTodos()
        todoClient.createNewTodo(Todo(1, "Test Todo", false))
        val responseCode = todoClient.updateTodo(Todo(1, "Buy groceries and milk", true))
        val todos = todoClient.getAllTodos()

        assertEquals(200, responseCode)
        assertEquals("""[{"id":1,"text":"Buy groceries and milk","completed":true}]""", todos)
    }

    @Test
    fun `Given one todo when delete todo then check no todos`() {
        WebSocketClient.start()

        todoClient.getAllTodos()
        val todo = Todo(1, "Test Todo", false)
        todoClient.createNewTodo(todo)
        val responseCode = todoClient.deleteTodo(todo)
        val todos = todoClient.getAllTodos()

        assertEquals(204, responseCode)
        assertEquals("""[]""", todos)
    }

}
