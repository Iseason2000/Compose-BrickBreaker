import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import top.iseason.compose.brickbreaker.ui.HEIGHT
import top.iseason.compose.brickbreaker.ui.WIDTH
import top.iseason.compose.brickbreaker.ui.theme.GameBody
import top.iseason.compose.brickbreaker.ui.theme.MyTheme
import top.iseason.compose.brickbreaker.viewmodel.GameViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BrickBreaker",
        icon = painterResource("icon.jpg"),
    ) {
        MyTheme {
            val viewModel = remember { GameViewModel(WIDTH.toInt(), HEIGHT.toInt()) }
            GameBody(
                viewModel, modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

