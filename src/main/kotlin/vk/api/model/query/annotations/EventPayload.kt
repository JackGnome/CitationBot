package org.example.vk.api.model.query.annotations

import org.example.vk.api.model.event.EventType

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EventPayload(
    val type: EventType
) {
}
