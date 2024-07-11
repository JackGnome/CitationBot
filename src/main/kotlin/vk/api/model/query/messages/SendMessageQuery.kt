package org.example.vk.api.model.query.messages

import org.example.vk.api.model.query.BaseQuery
import org.example.vk.api.model.query.annotations.Field
import org.example.vk.api.model.query.annotations.RandomId

@RandomId
data class SendMessageQuery(
    @Field("user_id")
    val userId: String,
    val message: String,
) : BaseQuery() {
    override val method: String = "messages.send"
}