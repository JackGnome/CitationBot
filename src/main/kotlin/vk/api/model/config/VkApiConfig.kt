package org.example.vk.api.model.config

data class VkApiConfig(
    val accessToken: String,
    val version: String,
    val groupId: Int,
    val baseUrl: String = "https://api.vk.com/method",
    val wait: String,
)