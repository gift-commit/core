package me.spradling.gift.core.api.modules

import dagger.Module
import dagger.Provides
import me.spradling.gift.core.api.database.GiftCommitStorageClient
import me.spradling.gift.core.api.models.configuration.GiftCommitConfiguration

@Module(includes = [ConfigurationModule::class])
class StorageModule {

  @Provides
  fun providesStorageClient(configuration: GiftCommitConfiguration) : GiftCommitStorageClient {
    return configuration.storageConfiguration.getSelectedClient()
  }
}