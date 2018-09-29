package me.spradling.gift.core.api.models.exceptions

import me.spradling.gift.core.api.models.errors.ErrorDetails

class ResourceNotFoundException : GiftCommitException(ErrorDetails.RESOURCE_NOT_FOUND)