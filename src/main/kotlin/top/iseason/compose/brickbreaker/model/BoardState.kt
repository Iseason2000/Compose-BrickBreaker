package top.iseason.compose.brickbreaker.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * 弹板数据类
 */
data class BoardState(
    var length: Int = 100,
    var width: Int = 20,
    var location: Offset = Offset((250f - length / 2), 480f),
    var speed: Float = 10F,
    var color: Color = Color.DarkGray,
    var direction: Int = 0
) {
    /**
     * 根据速度生成向右移动的状态
     */
    fun moveRight(): BoardState {
        return copy(location = Offset(location.x + speed, location.y))
    }

    /**
     * 根据速度生成向左移动的状态
     */
    fun moveLeft(): BoardState {
        return copy(location = Offset(location.x - speed, location.y))
    }
}