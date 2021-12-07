package top.iseason.compose.brickbreaker.viewmodel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import top.iseason.compose.brickbreaker.model.BallState
import top.iseason.compose.brickbreaker.model.BoardState
import top.iseason.compose.brickbreaker.model.BrickState
import top.iseason.compose.brickbreaker.model.PropState
import kotlin.math.abs
import kotlin.random.Random

val level1 = ViewState().apply {
    val mutableListOf = mutableListOf<BrickState>()
    for (i in 0..4) {
        for (n in 0..3) {
            mutableListOf.add(
                BrickState(
                    Offset(i * 100F + 20F, n * 80F),
                    maxHealth = 1
                )
            )
        }
    }
    bricks = mutableListOf
}
val level2 = ViewState().apply {
    val mutableListOf = mutableListOf<BrickState>()
    for (n in 3 downTo 0) {
        val padding = abs(n - 3) * 50F + 80F
        for (i in 0..n * 2) {
            mutableListOf.add(
                BrickState(
                    Offset(i * 50F + padding, abs(n - 3) * 50F),
                    maxHealth = 2,
                    color = Color(8, 77, 78)
                )
            )
        }
    }
    bricks = mutableListOf
    boardState = BoardState(length = 80, color = Color(255, 114, 153))
    ballStates = listOf(
        BallState(
            color = Color(255, 114, 153),
            location = Offset(boardState.location.x + boardState.length / 2, boardState.location.y - 10F)
        )
    )
}
val level3 = ViewState().apply {
    val mutableListOf = mutableListOf<BrickState>()
    for (n in 3 downTo 0) {
        val padding = abs(n - 3) * 50F + 80F
        for (i in 0..n * 2) {
            mutableListOf.add(
                BrickState(
                    Offset(i * 50F + padding, abs(n - 3) * 25F),
                    maxHealth = 3,
                    color = Color(8 + 20 * i, 77 + 20 * n, 78)
                )
            )
        }
    }
    bricks = mutableListOf
}
val level4 = ViewState().apply {
    val mutableListOf = mutableListOf<BrickState>()
    for (n in 0..3) {
        for (i in 0..3) {
            mutableListOf.add(
                BrickState(
                    Offset(i * 50F + 150, n * 25F),
                    maxHealth = 1,
                    color = Color(46 + i * 30, 204, 113)
                )
            )
            mutableListOf.add(
                BrickState(
                    Offset(i * 50F + 150, n * 25F + 200),
                    maxHealth = 3,
                    color = Color(142 + n * 10, 68, 173)
                )
            )
        }
    }
    bricks = mutableListOf
}

//最后一关，采用全随机
fun getLevel5() = ViewState().apply {
    val mutableListOf = mutableListOf<BrickState>()
    for (n in 1..50) {
        mutableListOf.add(
            BrickState(
                Offset(Random.nextInt(450).toFloat(), Random.nextInt(400).toFloat()),
                maxHealth = 1,
                color = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
            )
        )
    }
    bricks = mutableListOf
    ballStates = listOf(
        BallState(
            color = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)),
            location = Offset(boardState.location.x + boardState.length / 2, boardState.location.y - 10F)
        ),
        BallState(
            color = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)),
            location = Offset(boardState.location.x + boardState.length / 2, boardState.location.y - 10F)
        ),
        BallState(
            color = Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)),
            location = Offset(boardState.location.x + boardState.length / 2, boardState.location.y - 10F)
        )
    )
}

fun ViewState.deepCopy() =
    ViewState(
        bricks = mutableListOf<BrickState>().apply {
            bricks.forEach { add(it.copy()) }
        }.toList(),
        ballStates = mutableListOf<BallState>().apply {
            ballStates.forEach { add(it.copy()) }
        }.toList(),
        props = mutableListOf<PropState>().apply {
            props.forEach { add(it.copy()) }
        }.toList(),
        boardState = boardState.copy(),
        isBallOnBoard = isBallOnBoard,
        health = health,
        score = score
    )

