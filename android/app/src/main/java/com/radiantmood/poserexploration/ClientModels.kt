package com.radiantmood.poserexploration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun PoseNode.toClientNode(): ClientNode =
    when (this.type) {
        PoseType.Column -> ColumnNode(this)
        PoseType.Row -> RowNode(this)
        PoseType.Text -> TextNode(this)
        PoseType.Spacer -> SpacerNode(this)
        PoseType.Button -> ButtonNode(this)
    }

fun List<PoseNode>.toClientNodes(): List<ClientNode> = map { it.toClientNode() }

/**
 * TODOs:
 * - Button clicks
 * - eliminate string key magic knowledge
 * - hoisted, mutable states?
 */

sealed class ClientNode(poseNode: PoseNode) {
    val attr: PoseAttribute? = poseNode.attr
    val parentScopedAttrs: Map<String, PoseAttribute>? = poseNode.parentScopedAttrs
    open val params: Map<String, String>? = poseNode.params
    val children: List<ClientNode>? = poseNode.children?.toClientNodes()

    @Composable
    abstract fun compose(parentMods: Map<String, Modifier>)

    open fun createModifier(modifier: Modifier, attribute: PoseAttribute?, parentMods: Map<String, Modifier>): Modifier {
        val newMod = when (attribute?.type) {
            PoseAttributeType.PaddingAll -> Modifier.padding(all = attribute.params["all"]!!.toInt().dp)
            PoseAttributeType.Height -> Modifier.height(height = attribute.params["height"]!!.toInt().dp)
            PoseAttributeType.ParentMod -> parentMods[attribute.params["parentKey"]]!!
            else -> Modifier
        }
        val compositeMod = modifier.then(newMod)
        return attribute?.then?.let {
            createModifier(compositeMod, it, parentMods)
        } ?: compositeMod
    }
}

class ColumnNode(poseNode: PoseNode) : ClientNode(poseNode) {
    @Composable
    override fun compose(parentMods: Map<String, Modifier>) {
        Column(createModifier(Modifier, attr, parentMods)) {
            children?.forEach {
                it.compose(emptyMap())
            }
        }
    }
}

class RowNode(poseNode: PoseNode) : ClientNode(poseNode) {
    val verticalAlignment: Alignment.Vertical =
        params?.get("verticalAlignment")?.let {
            when (it) {
                "CenterVertically" -> Alignment.CenterVertically
                else -> Alignment.Top
            }
        } ?: Alignment.Top

    @Composable
    override fun compose(parentMods: Map<String, Modifier>) {
        Row(
            modifier = createModifier(Modifier, attr, parentMods),
            verticalAlignment = verticalAlignment,
        ) {
            children?.forEach {
                it.compose(createParentMods(it))
            }
        }
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun RowScope.createParentMods(clientNode: ClientNode): Map<String, Modifier> = buildMap {
    clientNode.parentScopedAttrs?.forEach { entry ->
        val mod = when (entry.value.type) {
            PoseAttributeType.Weight -> Modifier.weight(weight = entry.value.params["weight"]!!.toFloat())
            else -> Modifier
        }
        put(entry.key, mod)
    }
}

class TextNode(poseNode: PoseNode) : ClientNode(poseNode) {
    override val params: Map<String, String> = poseNode.params!!
    private val text = params["text"]!!
    private val styleMerge: TextStyle? =
        params["FontWeight"]?.let {
            when (it) {
                "Bold" -> {
                    TextStyle(fontWeight = FontWeight.Bold)
                }
                else -> null
            }
        }

    private val fontSize: TextUnit = params["fontSize"]?.toInt()?.sp ?: TextUnit.Unspecified

    @Composable
    override fun compose(parentMods: Map<String, Modifier>) {
        val style = LocalTextStyle.current.merge(styleMerge)
        Text(
            text = text,
            modifier = createModifier(Modifier, attr, parentMods),
            style = style,
            fontSize = fontSize,
        )
    }
}

class SpacerNode(poseNode: PoseNode) : ClientNode(poseNode) {
    @Composable
    override fun compose(parentMods: Map<String, Modifier>) {
        Spacer(modifier = createModifier(Modifier, attr, parentMods))
    }
}

class ButtonNode(poseNode: PoseNode) : ClientNode(poseNode) {
    @Composable
    override fun compose(parentMods: Map<String, Modifier>) {
        Button(onClick = { /*TODO*/ }) {
            children?.forEach {
                it.compose(createParentMods(it))
            }
        }
    }
}