import org.junit.jupiter.api.Test
import ws.WebSocketClient
import kotlin.test.assertEquals

class TodoTest: BaseTest() {
    @Test
    fun `Given zero todos when create todo then check it's added`() {
        val responseCode = todoClient.createTodo(Todo(3, "Test Todo", false))
        val todos = todoClient.getAllTodos()

        assertEquals(201, responseCode)
        assertEquals("""[{"id":3,"text":"Test Todo","completed":false}]""", todos)
    }

    @Test
    fun `Given 3 todos when get all then check 3 returned`() {
        todoClient.createTodo(Todo(1, "Test Todo 1", false))
        todoClient.createTodo(Todo(2, "Test Todo 2", false))
        todoClient.createTodo(Todo(3, "Test Todo 3", true))
        val todos = todoClient.getAllTodos()

        val expected = """
        [{"id":1,"text":"Test Todo 1","completed":false},{"id":2,"text":"Test Todo 2","completed":false},{"id":3,"text":"Test Todo 3","completed":true}]
        """.trim()
        assertEquals(expected, todos)

        WebSocketClient.awaitAndAssert("""{"data":{"completed":false,"id":1,"text":"Test Todo 1"},"type":"new_todo"}""")
        WebSocketClient.awaitAndAssert("""{"data":{"completed":false,"id":2,"text":"Test Todo 2"},"type":"new_todo"}""")
        WebSocketClient.awaitAndAssert("""{"data":{"completed":true,"id":3,"text":"Test Todo 3"},"type":"new_todo"}""")
    }

    @Test
    fun `Given 5 todos when get with limit 3 and offset 0 then first 3 returned`() {
        todoClient.createNTodos(5)
        val todos = todoClient.getNTodos(0, 3)

        val expected = """
        [{"id":1,"text":"Test Todo #1","completed":false},{"id":2,"text":"Test Todo #2","completed":true},{"id":3,"text":"Test Todo #3","completed":false}]
        """.trim()

        assertEquals(expected, todos)
    }

    @Test
    fun `Given 5 todos when get with limit 3 and offset 2 then last 3 returned`() {
        todoClient.createNTodos(5)
        val todos = todoClient.getNTodos(2, 3)

        val expected = """
        [{"id":3,"text":"Test Todo #3","completed":false},{"id":4,"text":"Test Todo #4","completed":true},{"id":5,"text":"Test Todo #5","completed":false}]
        """.trim()

        assertEquals(expected, todos)
    }

    @Test
    fun `Given one todo when update todo then check it's updated`() {
        todoClient.createTodo(Todo(1, "Test Todo", false))
        val responseCode = todoClient.updateTodo(Todo(1, "Buy groceries and milk", true))
        val todos = todoClient.getAllTodos()

        assertEquals(200, responseCode)
        assertEquals("""[{"id":1,"text":"Buy groceries and milk","completed":true}]""", todos)
    }
}
