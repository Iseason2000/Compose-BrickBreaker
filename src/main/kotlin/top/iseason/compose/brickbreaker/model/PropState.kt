package top.iseason.compose.brickbreaker.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

/**
 * 道具数据类
 */
data class PropState(
    val type: PropType = PropType.ENLARGE,
    val width: Float = 50F,
    val height: Float = 25F,
    val location: Offset = Offset(0F, 0F)
) {
    /**
     * 生成随机道具
     */
    fun getRandom() = copy(type = PropType.values().random())
}

/**
 * 道具类型及对应分数、颜色
 */
enum class PropType(val score: Int, val color: Color) {
    //好的
    ENLARGE(20, Color(255, 87, 34)),
    SPEEDDOWN(20, Color(255, 152, 0)),
    LONGER(30, Color(205, 220, 57)),
    SPLITE(200, Color(76, 175, 80)),
    BOARD_SPEEDUP(10, Color(121, 85, 72)),

    //环的
    NARROW(-10, Color(33, 150, 243)),
    SPEEDUP(-10, Color(103, 58, 183)),
    BOARD_SPEEDDOWN(-10, Color(96, 125, 139)),
    SHORTEN(-20, Color(236, 64, 122))

}
