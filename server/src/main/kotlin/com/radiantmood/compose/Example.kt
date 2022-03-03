package com.radiantmood.compose

import com.radiantmood.compose.models.PoseNode
import com.radiantmood.compose.models.PoseType
import com.radiantmood.compose.models.Response

fun responseExample(): Response = Response(
    poses = mapOf(
        "example" to PoseNode(
            type = PoseType.Column,
            children = listOfTextPoseNodes(5)
        )
    )
)

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