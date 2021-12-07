/*
 * *
 *  * Created with IntelliJ IDEA.
 *  * Description: 使用 Jetbrains Compose for Desktop 制作的打砖块游戏
 *  * @Author: Iseason
 *  * DateTime: 2021-12-7
 *
 */


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

