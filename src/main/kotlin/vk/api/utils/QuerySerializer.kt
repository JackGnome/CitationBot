package org.example.vk.api.utils

import org.example.vk.api.model.query.BaseQuery
import org.example.vk.api.model.query.annotations.Field
import org.example.vk.api.model.query.annotations.SkippedField

class QuerySerializer {

    fun serialize(query: BaseQuery): String {
        return query.javaClass.declaredFields
            .filter {
                !it.isAnnotationPresent(SkippedField::class.java)
            }
            .map {
                var fieldName = it.name
                if (it.isAnnotationPresent(Field::class.java)) {
                    fieldName = it.getAnnotation(Field::class.java).name
                }

                it.isAccessible = true
                val value = it.get(query).toString()
                "$fieldName=$value"
            }
            .plus(query.otherParams.map {
                "${it.first}=${it.second}"
            })
            .joinToString("&")
    }
}