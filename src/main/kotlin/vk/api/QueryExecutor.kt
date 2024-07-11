package org.example.vk.api

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse
import org.example.vk.api.model.config.VkApiConfig
import org.example.vk.api.model.query.BaseQuery
import org.example.vk.api.model.query.annotations.GroupId
import org.example.vk.api.model.query.annotations.LongPollingServer
import org.example.vk.api.model.query.annotations.RandomId
import org.example.vk.api.model.query.annotations.Response
import org.example.vk.api.model.query.annotations.Wrapped
import org.example.vk.api.model.response.ResponseWrapper
import org.example.vk.api.utils.QuerySerializer

class QueryExecutor(
    private val config: VkApiConfig,
    private val httpClient: HttpClient
) {
    private val serializer = QuerySerializer()
    private val json = Gson()

    private var _vkApiClient: VkApiClient? = null
    var vkApiClient: VkApiClient
        get() = _vkApiClient ?: throw RuntimeException("VkApiClient not initialized")
        set(value) {_vkApiClient = value}

    fun execute(query: BaseQuery) {
        val handlers = vkApiClient.handlersContainer.responseHandlers
        var baseUrl = config.baseUrl
        if(query.javaClass.isAnnotationPresent(LongPollingServer::class.java)) {
            baseUrl = vkApiClient.lpServerConfig.server
        }

        val params = buildParamsString(query)
        val request = createRequest(baseUrl, params, query.method)
        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        if (query.javaClass.isAnnotationPresent(Response::class.java)) {
            val responseType = query.javaClass.getAnnotation(Response::class.java).type.java
            val payload = getResponseFromJson(response.body(), responseType)

            handlers[responseType.simpleName]?.forEach { handler ->
                handler.invoke(payload)
            }
        }
    }

    private fun getResponseFromJson(response: String, responseType: Class<*>): Any {
        if (responseType.annotations.contains(Wrapped())) {
            return json.fromJson(response, ResponseWrapper::class.java).let {
                json.fromJson(it.response, responseType)
            }
        }

        return json.fromJson(response, responseType)
    }

    private fun buildParamsString(query: BaseQuery): String {
        if (query.javaClass.isAnnotationPresent(RandomId::class.java)) {
            query.addParam("random_id", (0..Int.MAX_VALUE).random().toString())
        }
        if (query.javaClass.isAnnotationPresent(GroupId::class.java)) {
            query.addParam("group_id", config.groupId.toString())
        }
        if (query.javaClass.isAnnotationPresent(LongPollingServer::class.java)) {
            val key = vkApiClient.lpServerConfig.key
            query.addParam("key", key)
        } else {
            query.addParam("access_token", config.accessToken)
        }

        query.addParam("v", config.version)

        return serializer.serialize(query)
    }

    private fun createRequest(baseUrl: String, params: String, method: String): HttpRequest {
        val url = if(method.trim().isEmpty()) URI(baseUrl) else URI("${baseUrl}/${method}")

        return HttpRequest.newBuilder()
            .uri(url)
            .POST(BodyPublishers.ofString(params))
            .build()
    }
}