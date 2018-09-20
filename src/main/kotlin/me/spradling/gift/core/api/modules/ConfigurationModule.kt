package me.spradling.gift.core.api.modules

import dagger.Module
import dagger.Provides
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.ConfigRetrieverOptions
import me.spradling.gift.core.api.VertxFuture
import me.spradling.gift.core.api.models.configuration.GiftCommitConfiguration
import javax.inject.Singleton

@Module(includes = [VertxModule::class])
class ConfigurationModule {

  @Singleton
  @Provides
  fun providesGiftCommitConfiguration(vertx : Vertx) : GiftCommitConfiguration {

    val fileStore = ConfigStoreOptions().setType("file")
                                        .setConfig(JsonObject(mapOf("path" to "configuration/default.json")))
    val fileSecretStore = ConfigStoreOptions().setType("file")
                                              .setConfig(JsonObject(mapOf("path" to ".build/secrets/decrypted/secrets.json")))

    val options = ConfigRetrieverOptions(stores = listOf(fileStore, fileSecretStore))
    val retriever = ConfigRetriever.create(vertx, options)

    val configFuture = ConfigRetriever.getConfigAsFuture(retriever)

    var jsonObject = VertxFuture.unwrap(configFuture)
    return jsonObject.mapTo(GiftCommitConfiguration::class.java)
  }
}