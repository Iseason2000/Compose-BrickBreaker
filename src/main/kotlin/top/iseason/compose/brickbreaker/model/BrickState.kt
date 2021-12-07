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
    val color: Color = Color.DarkGray
)