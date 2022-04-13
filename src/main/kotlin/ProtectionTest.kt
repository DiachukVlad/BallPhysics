import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.length
import dev.romainguy.kotlin.math.normalize
import dev.romainguy.kotlin.math.rotation
import processing.core.PApplet
import java.awt.Color

class ProjectionTest: PApplet() {
    override fun settings() {
        size(WIDTH.toInt(), HEIGHT.toInt())
    }

    override fun setup() {
        frameRate(60f)
        ellipseMode(PApplet.RADIUS)

        textSize(16f)
    }

    val red = Color.RED.rgb
    val green = Color.GREEN.rgb
    val blue = Color.BLUE.rgb
    val gray = Color.GRAY.rgb

    val pos = Float2(0f, 2f)
    val speed = Float2(4f, 2f)
    val collision = Float2(5f,0f)

    override fun draw() {
        scale(1f, -1f)
        translate(0f, -height.toFloat())

        scale(WORLD_SCALE, WORLD_SCALE)

        translate(10f, 10f)

        background(255)

        val rNor = normalize((pos - collision).rotate(-PI/2))
        val proj = rNor * length(speed - pos) * sin(angle(speed) - angle(collision - pos))

        fCircle(pos, color = red)
        drawVec(speed, start = pos, color = green)
        drawVec(proj, start=collision, color = blue)
    }

    fun drawVec(vec: Float2, start: Float2 = Float2(), color: Int = 0) {
        stroke(color)
        strokeWeight(0.2f)
        line(start.x, start.y, vec.x + start.x, vec.y + start.y)

        fCircle(vec + start, color = color)
    }

    fun fCircle(a: Float2, r: Float = 0.2f, color: Int = 0) {
        noStroke()
        fill(color)
        circle(a.x, a.y, r)
    }
}

fun main() {
    PApplet.main(ProjectionTest::class.java)
}