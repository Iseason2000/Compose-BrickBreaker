// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import top.iseason.compose.brickbreaker.model.BrickState
import top.iseason.compose.brickbreaker.ui.GameScreen
import top.iseason.compose.brickbreaker.ui.HEIGHT
import top.iseason.compose.brickbreaker.ui.WIDTH
import top.iseason.compose.brickbreaker.ui.theme.MyTheme
import top.iseason.compose.brickbreaker.viewmodel.GameViewModel

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "BrickBreaker",
        icon = painterResource("icon.jpg"),
    ) {
        MyTheme(darkTheme = false) {
            val viewModel = remember { GameViewModel(WIDTH.toInt(), HEIGHT.toInt()) }
            with(viewModel) {
                setState(viewState.value.apply {
                    val mutableListOf = mutableListOf<BrickState>()
                    for (i in 0..4) {
                        for (n in 0..3) {
                            mutableListOf.add(BrickState(Offset(i * 100F, n * 80F)))
                        }
                    }
                    bricks = mutableListOf
                })
            }
            GameScreen(
                viewModel, modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}
