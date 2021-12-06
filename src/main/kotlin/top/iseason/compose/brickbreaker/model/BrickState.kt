package top.iseason.compose.brickbreaker.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

//砖块对象，储存砖块数据
data class BrickState(
    val location: Offset = Offset(0F, 0F),
    val width: Float = 50F,
    val height: Float = 25F,
    val maxHealth: Int = 3,
    var health: Int = maxHealth,
    val color: Color = Color.Black
) {
    companion object {
        fun of(offsetList: List<Offset>) = offsetList.map { BrickState(it) }
        fun of(xRange: IntRange, yRange: IntRange) =
            of(mutableListOf<Offset>().apply {
                xRange.forEach { x ->
                    yRange.forEach { y ->
                        this += Offset(x.toFloat(), y.toFloat())
                    }
                }
            })
    }

    fun offsetBy(step: Pair<Int, Int>) =
        copy(location = Offset(location.x + step.first, location.y + step.second))

}