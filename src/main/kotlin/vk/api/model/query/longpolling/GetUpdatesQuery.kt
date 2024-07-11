package org.example.vk.api.model.query.longpolling

import org.example.vk.api.model.query.BaseQuery
import org.example.vk.api.model.query.annotations.Field
import org.example.vk.api.model.query.annotations.Key
import org.example.vk.api.model.query.annotations.LongPollingServer
import org.example.vk.api.model.query.annotations.Response
import org.example.vk.api.model.query.annotations.SkippedField
import org.example.vk.api.model.response.GetUpdatesResponse

@Key
@LongPollingServer
@Response(type = GetUpdatesResponse::class)
data class GetUpdatesQuery(
    @Field("ts")
    val timestamp: String,
    val wait: String,

    @SkippedField
    val groupId: String,
) : BaseQuery() {
     val act = "a_check"

    override val method: String = ""
}