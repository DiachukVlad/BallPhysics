import dev.romainguy.kotlin.math.Float2
import processing.core.PApplet

fun PApplet.circle(x: Float, y: Float, radius: Float) {
    ellipse(x, y, radius, radius)
}

fun middlePoint(a: Float2, b: Float2): Float2 {
    return (a + b) / 2f
}