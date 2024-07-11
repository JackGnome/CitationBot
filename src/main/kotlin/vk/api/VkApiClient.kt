package org.example.vk.api

import java.net.http.HttpClient
import org.example.vk.api.model.config.LongPollServerConfig
import org.example.vk.api.model.config.VkApiConfig
import org.example.vk.api.model.query.BaseQuery
import org.example.vk.api.model.query.messages.SendMessageQuery

class VkApiClient {
    private var _config: VkApiConfig? = null
    var config: VkApiConfig
        get() = _config ?: throw RuntimeException("config can not be null")
        set(value) {
            _config = value
        }

    private var _httpClient: HttpClient? = null
    var httpClient: HttpClient
        get() = _httpClient ?: throw RuntimeException("No http client configured!")
        set(value) {
            _httpClient = value
        }

    private var _executor: QueryExecutor? = null
    var executor: QueryExecutor
        get() = _executor ?: throw RuntimeException("No executor")
        set(value) {
            _executor = value
        }

    private var _lpServerConfig: LongPollServerConfig? = null
    var lpServerConfig: LongPollServerConfig
        get() = _lpServerConfig ?: throw RuntimeException("No lpServerConfig")
        set(value) {
            _lpServerConfig = value
        }

    private var _handlersContainer: HandlersContainer? = null
    var handlersContainer: HandlersContainer
        get() = _handlersContainer ?: throw RuntimeException("No handlers container found")
        set(value) {
            _handlersContainer = value
        }


    fun sendMessage(userId: Int, text: String) {
        println("Sending message: $text for $userId")
        executor.execute(SendMessageQuery(userId.toString(), message = text))
    }

    fun execute(query: BaseQuery) {
        executor.execute(query)
    }
}