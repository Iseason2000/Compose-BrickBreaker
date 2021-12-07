package top.iseason.compose.brickbreaker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import top.iseason.compose.brickbreaker.viewmodel.GameViewModel

/**
 * 游戏状态栏，显示分数、关卡、生命、日志
 */
@Composable
fun GameBar(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val viewState by viewModel.viewState.collectAsState()
    val log by viewModel.log.collectAsState()
    val margin = 10.dp
    Column(
        modifier = modifier
    ) {
        Text("分数: ${viewState.score}", color = Color.Gray)
        Spacer(modifier = Modifier.height(margin))
        Text("关卡: ${viewModel.level}", color = Color.Gray)
        Spacer(modifier = Modifier.height(margin))
        Text("生命: ${viewState.health}", color = Color.Gray)
        Spacer(modifier = Modifier.height(margin))
        Text("日志: $log", color = Color.Gray)
    }
}