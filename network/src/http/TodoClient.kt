package http

import Todo
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class TodoClient {
    private val client = OkHttpClient()

    fun getAllTodos(): String {
        val request = Request.Builder()
            .url("http://localhost:8080/todos")
            .build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        println("GET: $body")
        return body ?: ""
    }

    fun createNewTodo(todo: Todo) {
        val body = todo.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://localhost:8080/todos")
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        println("POST: " + response.body?.string())
    }
}