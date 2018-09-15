package me.spradling.gift.core.api.modules

import dagger.Module
import dagger.Provides
import io.vertx.core.Vertx
import javax.inject.Singleton

@Module
class VertxModule {

  @Provides
  @Singleton
  fun providesVertx() : Vertx {
    return Vertx.vertx()
  }
}