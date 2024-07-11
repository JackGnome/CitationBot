package org.example.vk.api.handler

import org.example.vk.api.model.config.LongPollServerConfig
import org.example.vk.api.model.query.annotations.ResponseHandler
import org.example.vk.api.model.response.GetLongPollServerResponse


class GetLongPollServerResponseHandler : GeneralResponseHandler() {

    @ResponseHandler(GetLongPollServerResponse::class)
    fun handleResponse(response: GetLongPollServerResponse) {
        vkApiClient.lpServerConfig = LongPollServerConfig(
            key = response.key,
            server = response.server,
            initialTimestamp = response.ts,
        )
    }
}