package me.spradling.gift.core.api.modules

import dagger.Module
import dagger.Provides
import me.spradling.gift.core.api.routes.HealthHandler

@Module
class HealthHandlerModule {

  @Provides
  fun providesHealthHandler() : HealthHandler {
    return HealthHandler()
  }
}