package top.iseason.compose.brickbreaker.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import top.iseason.compose.brickbreaker.model.BallState
import top.iseason.compose.brickbreaker.model.BoardState
import top.iseason.compose.brickbreaker.model.BrickState
import top.iseason.compose.brickbreaker.model.PropState
import top.iseason.compose.brickbreaker.viewmodel.BallAction
import top.iseason.compose.brickbreaker.viewmodel.BoardAction
import top.iseason.compose.brickbreaker.viewmodel.GameStatus
import top.iseason.compose.brickbreaker.viewmodel.GameViewModel

const val WIDTH = 500F
const val HEIGHT = 500F
const val FPS = 60F

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GameScreen(viewModel: GameViewModel, modifier: Modifier = Modifier) {
    val viewState by viewModel.viewState.collectAsState()
    val requester = remember { FocusRequester() }
    viewModel.resetBoardAndBall()
    LaunchedEffect(Unit) {
        requester.requestFocus()
        while (isActive) {
            delay(1000L / FPS.toLong())
            with(viewModel) {
                boardAction(BoardAction.Tick)
                ballAction(BallAction.Tick)
            }
        }
    }
    Box(
        modifier = modifier
            .onKeyEvent {
                if (viewModel.viewState.value.gameStatus != GameStatus.PLAYING) return@onKeyEvent false
                when {
                    (it.key == Key.DirectionRight && it.type == KeyEventType.KeyDown) -> {
                        viewModel.boardAction(BoardAction.MoveRight)
                        true
                    }
                    (it.key == Key.DirectionLeft && it.type == KeyEventType.KeyDown) -> {
                        viewModel.boardAction(BoardAction.MoveLeft)
                        true
                    }
                    (it.key == Key.Spacebar && it.type == KeyEventType.KeyDown) -> {
                        viewModel.boardAction(BoardAction.LaunchBall)
                        true
                    }
                    (it.type == KeyEventType.KeyUp) -> {
                        viewModel.boardAction(BoardAction.MoveStop)
                        true
                    }
                    else -> false
                }
            }
            .focusRequester(requester)
            .focusable()
            .defaultMinSize(WIDTH.dp, HEIGHT.dp)
            .size(WIDTH.dp, HEIGHT.dp)
            .background(MaterialTheme.colors.background)
            .padding(10.dp)
            .border(2.dp, Color.Black)
    )
    {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawBoard(viewState.boardState)
            drawBricks(viewState.bricks)
            drawProps(viewState.props)
            drawBalls(viewState.ballStates)
        }
    }
}

val DrawScope.wRate: Float
    get() = size.width / WIDTH
val DrawScope.hRate: Float
    get() = size.height / HEIGHT

//绘制挡板
fun DrawScope.drawBoard(boardState: BoardState = BoardState()) {
    val location = boardState.location
    drawRoundRect(
        boardState.color,
        Offset(location.x * wRate, location.y * hRate),
        Size(boardState.length * wRate, boardState.width * hRate),
        CornerRadius(5f, 5f)
    )
}

fun DrawScope.drawBricks(bricks: List<BrickState?>) {
    bricks.forEach {
        if (it == null) return
        drawBrick(it)
    }
}

fun DrawScope.drawBrick(brickState: BrickState) {
    val percentage = brickState.health.toFloat() / brickState.maxHealth.toFloat()
    val location = Offset(brickState.location.x * wRate, brickState.location.y * hRate)
    val size = Size(brickState.width * wRate, brickState.height * hRate)
    val alpha = when {
        percentage < 0.4f -> 0.4F
        percentage < 0.7f -> 0.7F
        else -> 1.0F
    }
    drawRoundRect(
        brickState.color,
        location,
        size,
        CornerRadius(10f, 10f),
        alpha = alpha
    )
}

fun DrawScope.drawBall(ballState: BallState = BallState()) {
    drawCircle(
        ballState.color,
        ballState.size * hRate,
        Offset(ballState.location.x * wRate, ballState.location.y * hRate)
    )
}

fun DrawScope.drawBalls(list: List<BallState>) = list.forEach { drawBall(it) }

fun DrawScope.drawProps(list: List<PropState>) = list.forEach { drawProp(it) }
fun DrawScope.drawProp(propState: PropState) {
    val location = propState.location
    drawRoundRect(
        propState.type.color,
        Offset(location.x * wRate, location.y * hRate),
        Size(propState.width * wRate, propState.height * hRate),
        CornerRadius(20f, 20f)
    )
}


@Preview
@Composable
fun PreviewGameScreen() {
    val viewModel = remember { GameViewModel() }
    GameScreen(viewModel)
}
