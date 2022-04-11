import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.length
import processing.core.PApplet
import processing.core.PApplet.cos
import processing.core.PApplet.sin
import java.awt.Color
import kotlin.math.atan2

data class Ball(
    var m: Float = 1f,
    var radius: Float = 1f,
    var color: Int = Color.BLUE.rgb,
    var k: Float = 1000f,

    var pos: Float2 = Float2(),
    var angle: Float = 0f,
    var speed: Float2 = Float2(),
    var angSpeed: Float = 0f,
    var force: Float2 = Float2(),
    var momentum: Float = 0f,
) {
    fun getLocalCoords(coords: Float2): Float2 {
        val l = coords - pos
        val r = length(l)
        val a = angle(l) - angle
        return Float2(r, a)
    }

    fun getWorldCoords(localC: Float2): Float2 {
        var (r, a) = localC
        a+=angle
        return pos+Float2(cos(a)*r, sin(a)*r)
    }
}