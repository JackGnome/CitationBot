package org.example.vk.api.model.query.annotations

import org.example.vk.api.model.event.EventType


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventHandler(
    val eventType: EventType
)
