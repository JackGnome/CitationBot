package org.example.vk.api.model.query.annotations

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ResponseHandler(
    val responseType: KClass<*>
)