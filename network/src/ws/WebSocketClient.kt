package ws

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit

object WebSocketClient: WebSocketListener() {
    private fun run() {
      val client = OkHttpClient.Builder()
          .readTimeout(0,  TimeUnit.MILLISECONDS)
          .build()

      val request = Request.Builder()
        .url("ws://localhost:8080/ws")
        .build()
      client.newWebSocket(request, this)

      // Trigger shutdown of the dispatcher's executor so this process exits immediately.
      client.dispatcher.executorService.shutdown()
    }

    override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
    }

    override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
      println("MESSAGE: $text")
    }

    override fun onMessage(webSocket: okhttp3.WebSocket, bytes: ByteString) {
      println("MESSAGE: " + bytes.hex())
    }

    override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
      webSocket.close(1000, null)
      println("CLOSE: $code $reason")
    }

    override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
      t.printStackTrace()
    }

    fun start() {
      run()
    }
}