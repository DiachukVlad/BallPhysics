import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.length
import processing.core.PApplet
import processing.core.PApplet.cos
import processing.core.PApplet.sin
import kotlin.math.atan2

fun PApplet.circle(x: Float, y: Float, radius: Float) {
    ellipse(x, y, radius, radius)
}

fun middlePoint(a: Float2, b: Float2): Float2 {
    return (a + b) / 2f
}

fun angle(a: Float2): Float {
    return atan2(a.y, a.x)
}

fun Float2.rotate(a: Float): Float2 {
    val sin = sin(a)
    val cos = cos(a)

    return Float2(cos * x - sin * y, sin * x + cos * y)
}

operator fun Float.times(v: Float2): Float2 {
    return v * this
}