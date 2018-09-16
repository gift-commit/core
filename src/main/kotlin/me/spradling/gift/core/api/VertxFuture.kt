package me.spradling.gift.core.api

import io.vertx.core.Future
import java.util.concurrent.TimeUnit
import java.util.concurrent.CountDownLatch
import java.time.Duration

class VertxFuture {

  companion object {
    @Throws(Throwable::class)
    fun <T> unwrap(future: Future<T>): T {
      return unwrap(future, Duration.ZERO)
    }

    @Throws(Throwable::class)
    fun <T> unwrap(future: Future<T>, duration: Duration): T {
      await(future, duration)

      if (future.failed()) {
        throw future.cause()
      }

      return future.result()
    }

    @Throws(InterruptedException::class)
    private fun <T> await(future: Future<T>, duration: Duration): Future<T> {
      val countDownLatch = CountDownLatch(1)

      future.setHandler{ r -> countDownLatch.countDown() }

      if (Duration.ZERO.equals(duration)) {
        countDownLatch.await()
      } else {
        countDownLatch.await(duration.toMillis(), TimeUnit.MILLISECONDS)
      }

      return future
    }
  }
}