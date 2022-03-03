package com.radiantmood.poserexploration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

fun PoseNode.toClientNode(): ClientNode =
    when(this.type) {
        PoseType.Column -> ColumnNode(this)
        PoseType.Row -> RowNode(this)
        PoseType.Text -> TextNode(this)
    }

fun List<PoseNode>.toClientNodes(): List<ClientNode> = map { it.toClientNode() }

// TODO: hoisted, mutable states
sealed class ClientNode(poseNode: PoseNode) {
    val attr: PoseAttribute? = poseNode.attr
    val params: Map<String, String>? = poseNode.params
    val children: List<ClientNode>? = poseNode.children?.toClientNodes()

    @Composable
    abstract fun compose()
}

class ColumnNode(poseNode: PoseNode): ClientNode(poseNode) {

    @Composable
    override fun compose() {
        Column {
            children?.forEach {
                it.compose()
            }
        }
    }
}

class RowNode(poseNode: PoseNode): ClientNode(poseNode) {

    @Composable
    override fun compose() {
        Row {
            children?.forEach {
                it.compose()
            }
        }
    }
}

class TextNode(poseNode: PoseNode): ClientNode(poseNode) {
    private val text = params!!["text"]!!

    @Composable
    override fun compose() {
        Text(text)
    }
}