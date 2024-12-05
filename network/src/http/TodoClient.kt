package http

import Todo
import io.github.cdimascio.dotenv.dotenv
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class TodoClient {
    private val client = OkHttpClient()
    private val baseUrl: String = "http://${dotenv()["BASE_URL"]}/todos"

    fun getAllTodos(): String {
        val request = Request.Builder()
            .url(baseUrl)
            .build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        println("GET: $body")
        return body ?: ""
    }

    fun getNTodos(offset: Int, limit: Int): String {
        val request = Request.Builder()
            .url("$baseUrl?offset=$offset&limit=$limit")
            .build()
        val response = client.newCall(request).execute()
        val body = response.body?.string()
        println("GET: $body")
        return body ?: ""
    }

    fun createTodo(todo: Todo): Int {
        val body = todo.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(baseUrl)
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        println("POST: " + response.code)
        return response.code
    }

    fun createTodo(todo: String): Int {
        val body = todo.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(baseUrl)
            .post(body)
            .build()
        val response = client.newCall(request).execute()
        println("POST: " + response.code)
        return response.code
    }

    fun createNTodos(num: Int) {
        for (i in 1..num) {
            val todo: Todo = Todo(i, "Test Todo #$i", i%2 == 0)
            val body = todo.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(baseUrl)
                .post(body)
                .build()
            val response = client.newCall(request).execute()
            assert(response.code == 201)
        }
    }

    fun updateTodo(todo: Todo): Int {
        val body = todo.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$baseUrl/${todo.id}")
            .put(body)
            .build()
        val response = client.newCall(request).execute()
        println("PUT: " + response.code)
        return response.code
    }

    fun deleteTodo(todo: Todo, withAuth: Boolean = true): Int {
        val request = Request.Builder()
            .url("$baseUrl/${todo.id}")
            .apply {
                if (withAuth)
                    addHeader("Authorization", "Basic YWRtaW46YWRtaW4=")
            }
            .delete()
            .build()
        val response = client.newCall(request).execute()
        println("DELETE: " + response.code)
        return response.code
    }
}