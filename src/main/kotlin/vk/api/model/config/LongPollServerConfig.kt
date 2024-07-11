package org.example.vk.api.model.config

data class LongPollServerConfig(
    val key: String,
    val server: String,
    val initialTimestamp: String,
    var timestamp: String = initialTimestamp,
)