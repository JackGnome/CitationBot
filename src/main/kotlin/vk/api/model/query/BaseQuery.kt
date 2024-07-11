package org.example.vk.api.model.query

import org.example.vk.api.model.query.annotations.SkippedField

abstract class BaseQuery {

    @SkippedField
    val otherParams = mutableListOf<Pair<String, String>>()

    @SkippedField
    abstract val method: String

    fun addParam(field: String, value: String) {
        otherParams.add(field to value)
    }
}
