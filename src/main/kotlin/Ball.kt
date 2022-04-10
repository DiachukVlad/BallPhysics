import dev.romainguy.kotlin.math.Float2
import java.awt.Color

class Ball(
    var m: Float = 1f,
    var radius: Float = 1f,
    var speed: Float2 = Float2(),
    var pos: Float2 = Float2(),
    var force: Float2 = Float2(),
    var color: Int = Color.BLUE.rgb,
    var k: Float = 400f
) {
}