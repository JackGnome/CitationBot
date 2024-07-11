package org.example.vk.api.model.event.payload

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import org.example.vk.api.model.event.EventType
import org.example.vk.api.model.query.annotations.EventPayload

@EventPayload(type = EventType.MESSAGE_NEW)
data class NewMessageEventPayload(
    val message: Message,
    @SerializedName("client_info")
    val clientInfo: ClientInfo,
) {
    data class Message(
        val date: Int,
        @SerializedName("from_id")
        val fromId: Int,
        val id: Int,
        val out: Int,
        val version: Int,
        val attachments: List<JsonElement>,
        @SerializedName("conversation_message_id")
        val conversationMessageId: Int,
        @SerializedName("fwd_messages")
        val fwdMessages: List<JsonElement>,
        val important: Boolean,
        @SerializedName("is_hidden")
        val isHidden: Boolean,
        @SerializedName("peer_id")
        val peerId: Int,
        @SerializedName("random_id")
        val randomId: Int,
        val text: String,
    )

    data class ClientInfo(
        @SerializedName("button_actions")
        val buttonActions: List<String>,
        val keyboard: Boolean,
        @SerializedName("inline_keyboard")
        val inlineKeyboard: Boolean,
        val carousel: Boolean,
        @SerializedName("lang_id")
        val langId: String,
    )
}