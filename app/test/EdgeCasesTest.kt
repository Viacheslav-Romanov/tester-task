import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import ws.WebSocketClient

class EdgeCasesTest: BaseTest() {
    @Test
    fun `Given completed todo when mark as incomplete check state is updated`() {
        todoClient.createTodo(Todo(1, "Test Todo", true))
        val responseCode = todoClient.updateTodo(Todo(1, "Test Todo", false))
        val todos = todoClient.getAllTodos()

        assertEquals(200, responseCode)
        assertEquals("""[{"id":1,"text":"Test Todo","completed":false}]""", todos)
    }

    @Test
    fun `Given multiple todos when delete then specific todo is removed`() {
        todoClient.createTodo(Todo(1, "Test Todo 1", false))
        todoClient.createTodo(Todo(2, "Test Todo 2", false))
        todoClient.deleteTodo(Todo(1, "Test Todo 1", false))
        val todos = todoClient.getAllTodos()

        assertEquals("""[{"id":2,"text":"Test Todo 2","completed":false}]""", todos)
    }

    @Test
    @Disabled("Supposed to return 400 but returns 401. Might be a bug.")
    fun `Given todos when update with invalid data then check it's failed`() {
        todoClient.createTodo(Todo(1, "Test Todo", false))
        val invalidTodo = """{"id":1,"text":"","completed":"invalid"}"""
        val responseCode = todoClient.updateTodo(1, invalidTodo)

        assertEquals(400, responseCode)
    }

    @Test
    fun `Given todos when duplicate id is used for creation then check it fails`() {
        todoClient.createTodo(Todo(1, "Test Todo 1", false))
        val responseCode = todoClient.createTodo(Todo(1, "Test Todo 2", true))
        val todos = todoClient.getAllTodos()

        assertEquals(400, responseCode) // Conflict
        assertEquals("""[{"id":1,"text":"Test Todo 1","completed":false}]""", todos)
    }

    @Test
    fun `Given none when create and delete todo then adjust websocket notifications`() {
        todoClient.createTodo(Todo(1, "Test Todo", false))
        WebSocketClient.awaitAndAssert("""{"data":{"completed":false,"id":1,"text":"Test Todo"},"type":"new_todo"}""")
        todoClient.deleteTodo(Todo(1, "Test Todo", false))
        WebSocketClient.awaitAndAssertNotContains("""{"data":{"id":1},"type":"delete_todo"}""")
    }

    @Test
    fun `Given one todo when delete todo then check no todos`() {
        val todo = Todo(1, "Test Todo", false)
        todoClient.createTodo(todo)
        val responseCode = todoClient.deleteTodo(todo)
        val todos = todoClient.getAllTodos()

        assertEquals(204, responseCode)
        assertEquals("[]", todos)
    }

    @Test
    fun `Given one todo when delete without auth then unauthorized`() {
        val todo = Todo(1, "Test Todo", false)
        todoClient.createTodo(todo)
        val responseCode = todoClient.deleteTodo(todo, false)
        val todos = todoClient.getAllTodos()

        assertEquals(401, responseCode)
        assertEquals("""[{"id":1,"text":"Test Todo","completed":false}]""", todos)
    }

    @Test
    fun `Given zero todos when create an invalid todo then check it's failed`() {
        val invalidTodo = """
        {
            "id": "invalid",
            "text": "",
            "completed": "not-a-boolean"
        }
        """.trimIndent()
        val responseCode = todoClient.createTodo(invalidTodo)
        val todos = todoClient.getAllTodos()

        assertEquals(400, responseCode)
        assertEquals("[]", todos)
    }

    @Test
    fun `Given an ivalid pagination when get with limit 0 and offset -1 then an error occurs`() {
        todoClient.createNTodos(1)
        val todos = todoClient.getNTodos(-1, 0)

        assertEquals("Invalid query string", todos)
    }

    @Test
    fun `Given zero todos when update a non-existent todo then an error 'Not Found' occurs`() {
        val responseCode = todoClient.updateTodo(Todo(1, "Buy groceries and milk", true))

        assertEquals(404, responseCode)
    }

    @Test
    fun `Given one todo when delete all todos then check it fails with method not allowed`() {
        val todo = Todo(1, "Test Todo", false)
        todoClient.createTodo(todo)
        val responseCode = todoClient.deleteAllTodos()
        val todos = todoClient.getAllTodos()

        assertEquals(405, responseCode)
        assertEquals("""[{"id":1,"text":"Test Todo","completed":false}]""", todos)
    }
}