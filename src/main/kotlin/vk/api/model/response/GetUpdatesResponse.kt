package org.example.vk.api.model.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class GetUpdatesResponse(
    @SerializedName("ts")
    val timestamp: String,
    val updates: List<Update>
) {
    data class Update(
        @SerializedName("group_id")
        val groupId: Int,
        val type: String,
        @SerializedName("event_id")
        val eventId: String,
        val v: String,
        @SerializedName("object")
        val obj: JsonElement
    )
}