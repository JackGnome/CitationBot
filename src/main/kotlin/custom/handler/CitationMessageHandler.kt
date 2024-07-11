package org.example.custom.handler

import org.example.vk.api.model.event.EventType
import org.example.vk.api.model.event.GeneralEventHandler
import org.example.vk.api.model.event.payload.NewMessageEventPayload
import org.example.vk.api.model.query.annotations.EventHandler


class CitationMessageHandler : GeneralEventHandler() {

    @EventHandler(eventType = EventType.MESSAGE_NEW)
    fun handle(payload: NewMessageEventPayload) {
        var citation = "Вы ничего не сказали :("
        if (payload.message.text.isNotBlank()) {
            citation = "Вы сказали: ${payload.message.text}"
        }

        vkApiClient.sendMessage(payload.message.fromId, citation)
    }
}