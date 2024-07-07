import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import todoapp_composemultiplatform.composeapp.generated.resources.Res
import todoapp_composemultiplatform.composeapp.generated.resources.compose_multiplatform

val lightRedColors = Color(color = 0xFFF57D88)
val darkRedColors = Color(color = 0xFF77000B)

@Composable
@Preview
fun App() {
    val lightColors = lightColorScheme(
        primary = lightRedColors,
        onPrimary = darkRedColors,
        background = lightRedColors,
        onBackground = darkRedColors
    )
    val darkColors = darkColorScheme(
        primary = lightRedColors,
        onPrimary = darkRedColors,
        background = lightRedColors,
        onBackground = darkRedColors
    )

    val color by mutableStateOf(
        if (isSystemInDarkTheme()) darkColors else lightColors
    )

    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}