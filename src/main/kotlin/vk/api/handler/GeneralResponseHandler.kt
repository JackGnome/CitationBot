package org.example.vk.api.handler

import org.example.vk.api.VkApiClient

open class GeneralResponseHandler {
    private var _vkApiClient: VkApiClient? = null
    var vkApiClient: VkApiClient
        get() = _vkApiClient ?: throw RuntimeException("VkApiClient not initialized")
        set(value) {
            _vkApiClient = value
        }
}