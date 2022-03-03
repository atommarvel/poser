package com.radiantmood.poserexploration

import kotlinx.serialization.Serializable

@Serializable
data class Response(val poses: Map<String, PoseNode>? = null)

@Serializable
data class PoseNode(
    val type: PoseType,
    val attr: PoseAttribute? = null,
    val parentScopedAttrs: Map<String, PoseAttribute>? = null,
    val params: Map<String, String>? = null,
    val children: List<PoseNode>? = null,
)

@Serializable
data class PoseAttribute(
    val type: PoseAttributeType,
    val params: Map<String, String>,
    val then: PoseAttribute? = null,
)

enum class PoseAttributeType {
    // AnyScoped
    PaddingAll, Height, ParentMod,

    // RowScoped
    Weight
}

enum class PoseType {
    Column, Row, Text, Spacer, Button,
}