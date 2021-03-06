package top.iseason.compose.brickbreaker.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

/**
 * 小球数据类
 */
data class BallState(
    var location: Offset = Offset(0F, 0F),
    var velocity: Velocity = Velocity(0F, 0F),
    var size: Float = 10.0F,
    var color: Color = Color.DarkGray
) {
    /**
     * 速度绕Y轴转
     */
    fun rotateY() = copy(velocity = Velocity(-velocity.x, velocity.y))

    /**
     * 速度绕X轴反转
     */
    fun rotateX() = copy(velocity = Velocity(velocity.x, -velocity.y))

    /**
     * 强制速度向上
     */
    fun forceUp() = copy(velocity = Velocity(velocity.x, -abs(velocity.y)))

}
