package me.spradling.gift.core.api

import dagger.Component
import me.spradling.gift.core.api.modules.ConfigurationModule
import me.spradling.gift.core.api.modules.StorageModule
import me.spradling.gift.core.api.modules.VertxModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
  VertxModule::class,
  ConfigurationModule::class,
  StorageModule::class
])
interface AppComponent {
  fun application() : Application
}