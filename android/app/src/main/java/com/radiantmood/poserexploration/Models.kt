package com.radiantmood.poserexploration

import kotlinx.serialization.Serializable

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

fun exampleNode() = PoseNode(
    type = PoseType.Column,
    children = listOfTextPoseNodes(5)
)

fun responseExample(): Response = Response(
    poses = mapOf(
        "example" to PoseNode(
            type = PoseType.Column,
            children = listOfTextPoseNodes(5)
        )
    )
)

@OptIn(ExperimentalStdlibApi::class)
fun listOfTextPoseNodes(amount: Int) = buildList {
    repeat(amount) {
        add(
            PoseNode(
                type = PoseType.Text,
                params = mapOf("text" to "Hello World $it!")
            )
        )
    }
}