import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import presenter.screen.home.HomeScreen

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

    val colors by mutableStateOf(
        if (isSystemInDarkTheme()) darkColors else lightColors
    )

    MaterialTheme(colorScheme = colors) {
        Navigator(screen = HomeScreen()) {
            SlideTransition(it)
        }
    }
}