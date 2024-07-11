package org.example.vk.api.model.response

import org.example.vk.api.model.query.annotations.Wrapped

@Wrapped
data class GetLongPollServerResponse(
    val key: String,
    val server: String,
    val ts: String,
)