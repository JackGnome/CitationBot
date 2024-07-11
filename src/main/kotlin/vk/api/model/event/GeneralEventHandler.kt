package org.example.vk.api.model.event

import org.example.vk.api.VkApiClient

open class GeneralEventHandler {
    private var _vkApiClient: VkApiClient? = null
    var vkApiClient: VkApiClient
        set(value) {
            _vkApiClient = value
        }
        get() = _vkApiClient ?: throw RuntimeException("VkApiClient is null!")
}