import org.junit.Test
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

    @Test
    fun `Given one todo when delete todo then check no todos`() {
        val todo = Todo(1, "Test Todo", false)
        todoClient.createTodo(todo)
        val responseCode = todoClient.deleteTodo(todo)
        val todos = todoClient.getAllTodos()

        assertEquals(204, responseCode)
        assertEquals("""[]""", todos)
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
}
