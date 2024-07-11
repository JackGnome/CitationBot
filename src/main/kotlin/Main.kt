package org.example

import java.net.http.HttpClient
import java.util.concurrent.Executors
import org.example.vk.api.HandlersContainer
import org.example.vk.api.QueryExecutor
import org.example.vk.api.VkApiClient
import org.example.vk.api.handler.UpdatesHandler
import org.example.vk.api.model.config.VkApiConfig
import org.example.vk.api.model.query.groups.GetLongPollServerQuery


fun main(args: Array<String>) {
    val accessToken = getEnvValue("VK_API_ACCESS_TOKEN")
    val version = getEnvValue("VK_API_VERSION")
    val groupId = getEnvValue("VK_GROUP_ID").toInt()
    val wait = getEnvValue("VK_API_WAIT")


    val vkApiClient = VkApiClient()
    val httpClient = HttpClient.newBuilder()
        .executor(Executors.newSingleThreadExecutor())
        .build()

    val config = VkApiConfig(
        accessToken = accessToken,
        version = version,
        groupId = groupId,
        wait = wait
    )

    val executor = QueryExecutor(
        config = config,
        httpClient = httpClient,
    )
    executor.vkApiClient = vkApiClient

    vkApiClient.config = config
    vkApiClient.executor = executor
    vkApiClient.httpClient = httpClient


    val handlersContainer = HandlersContainer(
        vkApiClient = vkApiClient,
    )

    val eventsHandler = UpdatesHandler()

    vkApiClient.handlersContainer = handlersContainer
    val query = GetLongPollServerQuery()
    executor.execute(query)
    eventsHandler.config = config
    eventsHandler.vkApiClient = vkApiClient
    eventsHandler.run()
}

fun getEnvValue(name: String): String {
    return System.getenv(name) ?: throw RuntimeException("Set env variable ${name}, please")
}
