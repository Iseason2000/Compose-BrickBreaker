package top.iseason.compose.brickbreaker.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.iseason.compose.brickbreaker.model.*
import top.iseason.compose.brickbreaker.ui.FPS
import top.iseason.compose.brickbreaker.ui.WIDTH
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random


class GameViewModel(private val width: Int = 500, private val height: Int = 500) {
    //界面数据
    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    //使用协程，防止线程阻塞
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    //当前挡板是否为初始化(小球跟随挡板移动)
    private var _boardAction: BoardAction = BoardAction.MoveStop

    /**
     * 处理挡板事件
     */
    fun boardAction(boardAction: BoardAction) {
        coroutineScope.launch {
            when (boardAction) {
                is BoardAction.Tick ->
                    when (_boardAction) {
                        BoardAction.MoveRight -> {
                            val newBoardState = getViewState().boardState.moveRight()
                            if (newBoardState.location.x + newBoardState.length > WIDTH) return@launch
                            newBoardState.direction = 1
                            upDateBoard(newBoardState)
                        }
                        BoardAction.MoveLeft -> {
                            val newBoardState = getViewState().boardState.moveLeft()
                            if (newBoardState.location.x < 0f) return@launch
                            newBoardState.direction = -1
                            upDateBoard(newBoardState)
                        }
                        BoardAction.LaunchBall -> launchBall()
                        BoardAction.MoveStop -> {
                            val boardState = getViewState().boardState
                            boardState.direction = 0
                            upDateBoard(boardState)
                        }
                        else -> {}
                    }

                else -> {
                    _boardAction = boardAction
                }
            }
        }
    }

    /**
     * 处理球事件
     */
    fun ballAction(ballAction: BallAction) {
        coroutineScope.launch {
            when (ballAction) {
                BallAction.Tick -> {
                    val ballStates = getViewState().ballStates
                    val mutableListOf = mutableListOf<BallState>()
                    ballStates.forEach {
                        val checkBallAction = getNextBall(it) ?: return@forEach
                        mutableListOf.add(checkBallAction)
                    }
                    upDateBalls(mutableListOf)
                }
                BallAction.Bounce -> {
                    val viewState = getViewState()
                    viewState.bricks = viewState.bricks.toMutableList().apply {
                        removeIf {
                            val isDead = it.health <= 0
                            if (isDead) placePropInChance(it)
                            isDead
                        }
                    }
                    emit(viewState)
                    //一个砖块20块
                    modifyScore(20)
                    if (getViewState().bricks.isEmpty()) gameAction(GameAction.Win)
                }
                BallAction.Over -> {
                    if (getViewState().ballStates.isEmpty())
                        gameAction(GameAction.OutOffBalls)
                }
                BallAction.ReSet -> {
                    resetBoardAndBall()
                }
            }
        }
    }

    /**
     * 处理游戏事件
     */
    fun gameAction(gameAction: GameAction) {
        coroutineScope.launch {
            val viewState = getViewState()
            when (gameAction) {
                GameAction.OutOffBalls -> {
                    viewState.health -= 1
                    emit(viewState)
                    if (viewState.health <= 0) {
                        gameAction(GameAction.Lost)
                        return@launch
                    }
                    ballAction(BallAction.ReSet)
                }
                GameAction.Lost -> {
                    //失败界面
                }
                GameAction.Win -> {}
                else -> {}
            }
        }
    }

    /**
     * 重置挡板与小球
     */
    fun resetBoardAndBall() {
        val viewState = getViewState().apply { this.isBallOnBoard = true }
        emit(viewState)
        upDateBoard()
    }

    /**
     * 更新挡板
     */
    private fun upDateBoard(boardState: BoardState = BoardState()) {
        val viewState = copyViewState()
        viewState.boardState = boardState
        if (viewState.isBallOnBoard) {
            val bLocation = boardState.location
            viewState.ballStates = listOf(
                BallState().apply {
                    location = Offset(bLocation.x + boardState.length / 2, bLocation.y - size)
                })
        }
        emit(viewState)
    }

    /**
     * 更新所有小球
     */
    private fun upDateBalls(balls: List<BallState>) {
        val copyViewState = copyViewState()
        copyViewState.ballStates = balls
        emit(copyViewState)
    }

    /**
     * 根据当前小球获取下一帧小球并计算碰撞
     */
    private fun getNextBall(ball: BallState): BallState? {
        val ballLocation = ball.location.plus(ball.velocity.toOffset())
        val viewState = getViewState()
        with(viewState.boardState) {
            //是否x坐标与板重合
            when (ballLocation.x) {
                !in (location.x)..(location.x + length) -> return@with
                else -> {}
            }
            if ((ballLocation.y) <= (height - width - ball.size)) return@with

            //根据挡板运动方向及长度权重偏移小球方向
            val l1 = ball.velocity.length()
            val x = ball.velocity.x + direction * length / 40
            val y = ball.velocity.y
            val l2 = Velocity(x, y).length()
            ball.velocity = Velocity(x * l1 / l2, y * l1 / l2)

            return ball.forceUp()
        }
        with(viewState.bricks) {
            forEach {
                with(it) {
                    val border = ball.size / 2 //碰撞偏移量
                    val dx = ballLocation.x - location.x + border //>0 球X轴与砖块最左方+球size的距离
                    val dzx = width + border * 2 - dx //>0 球X轴与砖块最右方+球size的距离
                    if (!(dx > 0 && dzx > 0)) return@forEach
                    val dy = ballLocation.y - location.y + border   //>0 球y轴与砖块最上方+球size的距离
                    val dzy = height + border * 2 - dy       //>0 球y轴与砖块最下方+球size的距离
                    if (!(dy > 0 && dzy > 0)) return@forEach
                    health -= 1
                    ballAction(BallAction.Bounce)
                    return if (min(abs(dx), abs(dzx)) - min(abs(dy), abs(dzy)) >= 0)
                        ball.rotateX()
                    else
                        ball.rotateY()

                }
            }
        }
        //道具检测
        with(viewState.props) {
            forEach {
                with(it) {
                    val border = ball.size / 2 //碰撞偏移量
                    val dx = ballLocation.x - location.x + border //>0 球X轴与砖块最左方+球size的距离
                    val dzx = width + border * 2 - dx //>0 球X轴与砖块最右方+球size的距离
                    if (!(dx > 0 && dzx > 0)) return@forEach
                    val dy = ballLocation.y - location.y + border   //>0 球y轴与砖块最上方+球size的距离
                    val dzy = height + border * 2 - dy       //>0 球y轴与砖块最下方+球size的距离
                    if (!(dy > 0 && dzy > 0)) return@forEach
                    //吃到道具
                    applyProp(it)
                }
            }
        }
        with(ballLocation) {
            if (x < ball.size || x > width - ball.size) return ball.rotateY()
            if (y < ball.size) return ball.rotateX()
            if (y > height - ball.size) {
                ballAction(BallAction.Over)
                return null
            }
        }
        return ball.copy(location = ballLocation)
    }

    /**
     * 发射板上的小球
     */
    private fun launchBall() {
        val viewState = getViewState()
        if (!viewState.isBallOnBoard) return
        viewState.isBallOnBoard = false
        viewState.ballStates.forEach {
            it.velocity = getRandomVelocity(600.0 / FPS)
        }
        emit(viewState)
    }

    /**
     * 将砖块以一定概率转化为随机道具
     */
    private fun placePropInChance(brick: BrickState) {
        if (Random.nextFloat() > 0.2) return
        emit(
            getViewState().apply {
                props = props.toMutableList().apply {
                    add(PropState(location = brick.location).getRandom())
                }
            })
    }

    /**
     * 根据道具应用效果
     */
    private fun applyProp(prop: PropState) {
        coroutineScope.launch {
            val viewState = getViewState()
            when (prop.type) {
                PropType.ENLARGE -> {
                    viewState.ballStates.forEach { it.size = it.size * 2F }
                }
                PropType.NARROW -> {
                    viewState.ballStates.forEach { it.size = it.size * 0.5F }
                }
                PropType.SPEEDUP -> {
                    viewState.ballStates.forEach { it.velocity = it.velocity.times(1.5F) }
                }
                PropType.SPEEDDOWN -> {
                    viewState.ballStates.forEach { it.velocity = it.velocity.times(0.8F) }
                }
                PropType.SPLITE -> {
                    //最多12个，太卡了
                    if (viewState.ballStates.size > 12) return@launch
                    val mutableList = viewState.ballStates.toMutableList()
                    viewState.ballStates.onEach {
                        mutableList.add(
                            it.copy(
                                velocity = getRandomVelocity(it.velocity.length().toDouble())
                            )
                        )
                    }
                    viewState.ballStates = mutableList
                }
                PropType.BOARD_SPEEDUP -> {
                    viewState.boardState.speed *= 1.2F
                }
                PropType.BOARD_SPEEDDOWN -> {
                    viewState.boardState.speed *= 0.8F
                }
                PropType.LONGER -> {
                    viewState.boardState.length *= 2
                }
                PropType.SHORTEN -> {
                    viewState.boardState.length = (viewState.boardState.length * 0.5).toInt()
                }
            }
            modifyScore(prop.type.score)
            viewState.props = viewState.props.toMutableList().apply { removeIf { it == prop } }
            emit(viewState)
        }
    }

    /**
     * 生成随机方向的指定模的速度向量
     */
    private fun getRandomVelocity(length: Double): Velocity {
        val x = Random.nextDouble(-length * 0.7, length * 0.7).toFloat()
        val y = (length.pow(2) - x.pow(2)).pow(0.5).toFloat()
        return Velocity(x, -y)
    }

    /**
     * 速度的拓展方法，将速度转为OffSet
     */
    private fun Velocity.toOffset() = Offset(x, y)

    /**
     * 取模
     */
    private fun Velocity.length() = sqrt(x.pow(2) + y.pow(2))

    /**
     * 对外的更新状态的接口
     */
    fun setState(state: ViewState) = emit(state)

    /**
     * 修改当前分数
     */
    private fun modifyScore(score: Int) = emit(getViewState().apply { this.score += score })

    /**
     * 发射新ViewState
     */
    private fun emit(state: ViewState) {
        _viewState.value = state
    }


    /**
     * 复制当前ViewState
     */
    private fun copyViewState() = _viewState.value.copy()

    /**
     * 获取当前ViewState
     */
    private fun getViewState() = _viewState.value
}
