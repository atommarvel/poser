package com.radiantmood.compose.models

import kotlinx.serialization.Serializable

// TODO: DSL?

@Serializable
data class Response(val poses: Map<String, PoseNode>? = null)

@Serializable
data class PoseNode(
    val type: PoseType,
    val attr: PoseAttribute? = null,
    val params: Map<String, String>? = null,
    val children: List<PoseNode>? = null,
)

@Serializable
data class PoseAttribute(
    val type: PoseAttributeType,
    val params: Map<String, String>,
    val then: PoseAttribute? = null,
)

enum class PoseAttributeType

enum class PoseType {
    Column, Row, Text,
}
