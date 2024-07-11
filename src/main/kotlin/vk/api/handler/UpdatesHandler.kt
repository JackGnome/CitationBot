package org.example.vk.api.handler

import com.google.gson.Gson
import org.example.vk.api.model.config.VkApiConfig
import org.example.vk.api.model.event.EventType
import org.example.vk.api.model.query.annotations.ResponseHandler
import org.example.vk.api.model.query.longpolling.GetUpdatesQuery
import org.example.vk.api.model.response.GetUpdatesResponse

class UpdatesHandler : GeneralResponseHandler() {
    var config: VkApiConfig? = null

    fun run() {
        if (config == null) {
            throw RuntimeException("Config is null")
        }

        while (true) {
            val timestamp = vkApiClient.lpServerConfig.timestamp
            val query = GetUpdatesQuery(
                timestamp = timestamp,
                wait = config?.wait.toString(),
                groupId = config?.groupId.toString(),
            )
            vkApiClient.execute(query)
        }
    }

    @ResponseHandler(GetUpdatesResponse::class)
    fun handle(response: GetUpdatesResponse) {
        val eventsPayloads = vkApiClient.handlersContainer.eventsPayloads
        val eventHandler = vkApiClient.handlersContainer.eventHandlers
        vkApiClient.lpServerConfig.timestamp = response.timestamp
        response.updates.forEach {
            val eventType = EventType.valueOf(it.type.uppercase())
            val payload = Gson().fromJson(it.obj, eventsPayloads[eventType])
            eventHandler[eventType]?.forEach { it(payload) }
        }
    }
}