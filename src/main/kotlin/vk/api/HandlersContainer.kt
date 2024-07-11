package org.example.vk.api

import org.example.vk.api.handler.GeneralResponseHandler
import org.example.vk.api.model.event.EventType
import org.example.vk.api.model.event.GeneralEventHandler
import org.example.vk.api.model.query.annotations.EventHandler
import org.example.vk.api.model.query.annotations.EventPayload
import org.example.vk.api.model.query.annotations.ResponseHandler
import org.example.vk.api.utils.findClasses
import org.example.vk.api.utils.setField


class HandlersContainer(
    private val vkApiClient: VkApiClient
) {
    var eventHandlers: Map<EventType, List<(payload: Any) -> Unit>> = mutableMapOf()
    var eventsPayloads: Map<EventType, Class<*>> = mutableMapOf()
    var responseHandlers: Map<String, List<(response: Any) -> Unit>> = mutableMapOf()

    init {
        eventHandlers = findClasses("org.example", GeneralEventHandler::class.java)
            .asSequence()
            .map { initHandlerInstance(it) }
            .map { instance ->
                instance.javaClass.methods
                    .filter { it.isAnnotationPresent(EventHandler::class.java) }
                    .map { method ->
                        val handler: (payload: Any) -> Unit = { payload: Any ->
                            method.invoke(instance, payload)
                        }
                        val eventType = method.getAnnotation(EventHandler::class.java).eventType
                        eventType to handler
                    }
            }.flatten()
            .groupBy { it.first }
            .mapValues { p -> p.value.map { it.second } }

        eventsPayloads = findClasses("org.example", annotatedBy = listOf(EventPayload::class.java))
            .map {
                it.getAnnotation(EventPayload::class.java).type to it
            }
            .groupBy { it.first }
            .mapValues { it.value.first().second }

        responseHandlers = findClasses("org.example", GeneralResponseHandler::class.java)
            .asSequence()
            .map { initHandlerInstance(it) }
            .map { instance ->
                instance::class.java.methods.map { method ->
                    val handler: (response: Any) -> Unit = { response: Any ->
                        method.invoke(instance, response)
                    }

                    handler to method
                }
            }
            .flatMap { it.toList() }
            .filter { it.second.isAnnotationPresent(ResponseHandler::class.java) }
            .map {
                (it.second.getAnnotation(ResponseHandler::class.java).responseType.simpleName ?: "") to it.first
            }
            .groupBy { it.first }
            .mapValues { p -> p.value.map { it.second } }


    }

    private fun initHandlerInstance(clazz: Class<*>): Any {
        val instance = clazz.getDeclaredConstructor().newInstance()
        setField(instance, VkApiClient::class.java, vkApiClient as Any)
        return instance
    }
}