package com.radiantmood.poserexploration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radiantmood.poserexploration.ui.theme.PoserExplorationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pose = goalAttempt().toClientNode()
        setContent {
            PoserExplorationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Column {
                        pose.compose(emptyMap())
                        Spacer(modifier = Modifier
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(Color.Red))
                        Goal()
                    }
                }
            }
        }
    }
}

fun exampleNode() = PoseNode(
    type = PoseType.Column,
    children = listOfTextPoseNodes(5)
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

// TODO: make this way less painful to write
fun goalAttempt(): PoseNode = PoseNode(
    type = PoseType.Column,
    attr = PoseAttribute(
        type = PoseAttributeType.PaddingAll,
        params = mapOf("all" to "16")
    ),
    children = listOf(
        PoseNode(
            type = PoseType.Text,
            params = mapOf(
                "text" to "Lorem ipsum dolorum",
                "FontWeight" to "Bold",
                "fontSize" to "24"
            )
        ),
        PoseNode(
            type = PoseType.Spacer,
            attr = PoseAttribute(
                type = PoseAttributeType.Height,
                params = mapOf("height" to "8")
            )
        ),
        PoseNode(
            type = PoseType.Text,
            params = mapOf(
                "text" to "Lorem ipsum dolorum lorem ipsum",
            )
        ),
        PoseNode(
            type = PoseType.Spacer,
            attr = PoseAttribute(
                type = PoseAttributeType.Height,
                params = mapOf("height" to "8")
            )
        ),
        PoseNode(
            type = PoseType.Row,
            params = mapOf("verticalAlignment" to "CenterVertically"),
            children = listOf(
                PoseNode(
                    type = PoseType.Spacer,
                    attr = PoseAttribute(
                        type = PoseAttributeType.ParentMod,
                        params = mapOf("parentKey" to "weight"),
                    ),
                    parentScopedAttrs = mapOf(
                        "weight" to PoseAttribute(
                            type = PoseAttributeType.Weight,
                            params = mapOf("weight" to "1"),
                        )
                    )
                ),
                PoseNode(
                    type = PoseType.Text,
                    params = mapOf(
                        "text" to "Not Now",
                    ),
                    attr = PoseAttribute(
                        type = PoseAttributeType.PaddingAll,
                        params = mapOf("all" to "16")
                    )
                ),
                PoseNode(
                    type = PoseType.Button,
                    children = listOf(
                        PoseNode(
                            type = PoseType.Text,
                            params = mapOf(
                                "text" to "Take Action",
                            )
                        ),
                    )
                )
            )
        )
    )
)

@Composable
fun Goal() {
    Column(Modifier.padding(16.dp)) {
        Text(text = "Lorem ipsum dolorum", style = TextStyle.Default.copy(fontWeight = FontWeight.Bold), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Lorem ipsum dolorum lorem ipsum")
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.weight(1f))
            Text("Not Now", modifier = Modifier.padding(16.dp))
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Take Action")
            }
        }
    }
}

