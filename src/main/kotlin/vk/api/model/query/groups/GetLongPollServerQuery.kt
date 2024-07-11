package org.example.vk.api.model.query.groups

import org.example.vk.api.model.response.GetLongPollServerResponse
import org.example.vk.api.model.query.BaseQuery
import org.example.vk.api.model.query.annotations.GroupId
import org.example.vk.api.model.query.annotations.Response

@GroupId
@Response(type= GetLongPollServerResponse::class)
class GetLongPollServerQuery: BaseQuery() {
    override val method: String = "groups.getLongPollServer"
}