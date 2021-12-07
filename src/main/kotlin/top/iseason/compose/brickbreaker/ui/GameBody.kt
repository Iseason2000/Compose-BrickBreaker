package top.iseason.compose.brickbreaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import top.iseason.compose.brickbreaker.viewmodel.GameStatus.*
import top.iseason.compose.brickbreaker.viewmodel.GameViewModel

/**
 * 游戏主体
 */
@Composable
fun GameBody(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val gameState by viewModel.gameState.collectAsState()
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
    ) {
        if (gameState != READY) {
            GameScreen(viewModel, modifier)
            GameBar(viewModel, modifier = Modifier.offset(20.dp, 430.dp).fillMaxSize())
        }
    }
    gameDialog(viewModel)
}

/**
 * 对话框
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun gameDialog(viewModel: GameViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val gameState by viewModel.gameState.collectAsState()
    when (gameState) {
        READY -> {
            AlertDialog(
                modifier = Modifier.width(250.dp),
                onDismissRequest = {},
                title = {
                    Text(text = "BrickBreaker", style = MaterialTheme.typography.h5)
                },
                text = {
                    Column {
                        Text("游戏规则:")
                        Text("按 P 键暂停")
                        Text("空格发射小球")
                        Text("左右方向键移动挡板")
                        Text("打破砖块有概率掉落道具")
                        Text("挡板在移动时反弹小球能够改变小球方向")
                    }
                },
                buttons = {
                    Box(modifier = Modifier.padding(all = 8.dp)) {
                        Button(
                            onClick = {
                                viewModel.setGameLevel(1)
                                viewModel.setGameState(PLAYING)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("开始游戏", fontSize = 16.sp)
                        }
                    }

                }
            )
        }
        STOP -> {
            AlertDialog(
                modifier = Modifier.width(250.dp),
                onDismissRequest = { },
                title = {
                    Text(text = "BrickBreaker", style = MaterialTheme.typography.h5)
                },
                text = {
                    Column {
                        Text("游戏暂停中!")
                    }

                },
                buttons = {
                    Box(modifier = Modifier.padding(all = 8.dp)) {
                        Button(
                            onClick = {
                                viewModel.setGameState(PLAYING)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("继续游戏", fontSize = 16.sp)
                        }
                    }
                }
            )
        }
        LOST -> {
            AlertDialog(
                modifier = Modifier.width(250.dp).focusTarget(),
                onDismissRequest = {},
                title = {
                    Text(text = "BrickBreaker", style = MaterialTheme.typography.h5)
                },
                text = {
                    Text("游戏失败! 分数:${viewState.score}")
                },
                buttons = {
                    Box(modifier = Modifier.padding(all = 8.dp)) {
                        Button(
                            onClick = {
                                viewModel.setGameLevel(viewModel.level)
                                viewModel.setGameState(PLAYING)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("重新开始", fontSize = 16.sp)
                        }
                    }

                }
            )
        }
        WIN -> {
            AlertDialog(
                modifier = Modifier.width(250.dp).focusTarget(),
                onDismissRequest = {},
                title = {
                    Text(text = "BrickBreaker", style = MaterialTheme.typography.h5)
                },
                text = {
                    Text("恭喜过关! 关卡: ${viewModel.level} 分数:${viewState.score}")
                },
                buttons = {
                    Box(modifier = Modifier.padding(all = 8.dp)) {
                        Row(modifier = Modifier.align(Alignment.Center)) {
                            Button(
                                onClick = {
                                    viewModel.setGameLevel(viewModel.level)
                                    viewModel.setGameState(PLAYING)
                                },
                                modifier = Modifier.size(110.dp, 40.dp)
                            ) {
                                Text("重新开始", fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = {
                                    viewModel.setGameLevel(viewModel.level + 1)
                                    viewModel.setGameState(PLAYING)
                                },
                                modifier = Modifier.size(110.dp, 40.dp)
                            ) {
                                Text("下一关", fontSize = 16.sp)
                            }
                        }

                    }
                }
            )
        }
        PLAYING -> {}

    }
}

