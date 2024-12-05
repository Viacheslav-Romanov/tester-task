package ws

import io.github.cdimascio.dotenv.dotenv
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit

object WebSocketClient: WebSocketListener() {
  private val wsUrl: String = "ws://${dotenv()["BASE_URL"]}/ws"
  var entries: ConcurrentLinkedQueue<String> = ConcurrentLinkedQueue()
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
    entries.add(text)
    println("MESSAGE: $text")
  }

  override fun onMessage(webSocket: okhttp3.WebSocket, bytes: ByteString) {
    println("MESSAGE: " + bytes.hex())
  }

  override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
    entries.clear()
    webSocket.close(1000, null)
    println("CLOSE: $code $reason")
  }

  override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
    entries.clear()
    if(t is java.io.EOFException) {
      println("Connection has been closed by a server side")
    } else {
      t.printStackTrace()
    }
  }

  fun start() {
    run()
  }

  fun awaitAndAssert(message: String) {
    var contains = false
    for (i in 1..5) {
      contains = entries.contains(message)
      if (contains) break
      sleep(100)
    }
    assert(contains) {
      "Message $message has not been arrived within the 500ms timeframe"
    }
  }

  fun awaitAndAssertNotContains(message: String) {
    var contains = true
    for (i in 1..5) {
      contains = entries.contains(message)
      if (!contains) break
      sleep(100)
    }
    assert(!contains) {
      "Message $message has not to be in the receiving queue but It's somehow there"
    }
  }
}