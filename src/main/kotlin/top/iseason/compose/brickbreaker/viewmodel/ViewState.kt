package top.iseason.compose.brickbreaker.viewmodel

import top.iseason.compose.brickbreaker.model.BallState
import top.iseason.compose.brickbreaker.model.BoardState
import top.iseason.compose.brickbreaker.model.BrickState
import top.iseason.compose.brickbreaker.model.PropState

data class ViewState(
    var bricks: List<BrickState> = emptyList(),
    var ballStates: List<BallState> = emptyList(),
    var props: List<PropState> = emptyList(),
    var gameStatus: GameStatus = GameStatus.PLAYING,
    var boardState: BoardState = BoardState(),
    var isBallOnBoard: Boolean = true,
    var health: Int = 3,
    var score: Int = 0
)

//定义游戏状态
enum class GameStatus {
    READY,
    PLAYING,
    STOP,
    WIN,
    LOST
}

sealed class GameAction {
    object Start : GameAction()

    object Restart : GameAction()
    object Pause : GameAction()
    object OutOffBalls : GameAction()
    object Lost : GameAction()
    object Win : GameAction()
    // object Resume : GameAction()
}

sealed class BoardAction {
    object Tick : BoardAction()
    object MoveLeft : BoardAction()
    object MoveRight : BoardAction()
    object MoveStop : BoardAction()
    object LaunchBall : BoardAction()
}

sealed class BallAction {
    object Tick : BallAction()
    object Bounce : BallAction()
    object Over : BallAction()
    object ReSet : BallAction()
}