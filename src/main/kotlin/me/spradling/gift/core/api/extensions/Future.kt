package me.spradling.gift.core.api.extensions

import io.vertx.core.Future
import java.util.concurrent.CountDownLatch

fun <T> Future<T>.unwrap(): T {
  this.wait()

  if (this.failed()) {
    throw this.cause()
  }

  return this.result()
}

fun <T> Future<T>.wait(): Future<T> {
  val countDownLatch = CountDownLatch(1)

  this.setHandler { _ ->
    countDownLatch.countDown()
  }

  countDownLatch.await()

  return this
}