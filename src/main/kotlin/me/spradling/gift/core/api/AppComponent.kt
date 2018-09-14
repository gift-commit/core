package me.spradling.gift.core.api

import dagger.Component

@Component
interface AppComponent {
  fun inject() : RestVerticle
}