package ws

import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

object WebSocketClient: WebSocketListener() {
  private val wsUrl: String = "ws://${dotenv()["BASE_URL"]}/ws"
  private fun run() {
    val client = OkHttpClient.Builder()
        .readTimeout(0,  TimeUnit.MILLISECONDS)
        .build()

    val request = Request.Builder()
      .url(wsUrl)
      .build()
    client.newWebSocket(request, this)

    // Trigger shutdown of the dispatcher's executor so this process exits immediately.
    client.dispatcher.executorService.shutdown()
    sleep(1000)
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
    if(t is java.io.EOFException) {
      println("Connection has been closed by a server side")
    } else {
      t.printStackTrace()
    }
  }

  fun start() {
    run()
  }
}