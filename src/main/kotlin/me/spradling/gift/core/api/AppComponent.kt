package me.spradling.gift.core.api

import dagger.Component
import me.spradling.gift.core.api.modules.HealthHandlerModule

@Component(modules = [HealthHandlerModule::class])
interface AppComponent {
  fun inject() : RestVerticle
}