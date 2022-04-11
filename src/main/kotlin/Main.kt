import dev.romainguy.kotlin.math.Float2
import processing.core.PApplet
import java.awt.Color

const val WORLD_SCALE = 30f
const val HEIGHT = 720f
const val WIDTH = 1280f

class Main : PApplet() {
    override fun settings() {
        size(WIDTH.toInt(), HEIGHT.toInt())
    }

    override fun setup() {
        frameRate(60f)
        ellipseMode(PApplet.RADIUS)

        textSize(16f)
    }

    override fun draw() {
        scale(1f, -1f)
        translate(0f, -height.toFloat())

        scale(WORLD_SCALE, WORLD_SCALE)

        background(255)
        drawFigures()

        drawBarrier()

        // reset transformations
        resetMatrix()
        drawTexts()
    }

    private fun drawTexts() {
        fill(0f);
        text("${(timer * 100).toInt() / 100f} sec", 5f, 16f)
        text("$energy", 5f, 16 * 2f)
    }

    private fun drawFigures() {
        balls.forEach {
            noStroke()
            fill(it.color)
            circle(it.pos.x, it.pos.y, it.radius)

            stroke(0f)
            strokeWeight(0.05f)
            line(it.pos.x, it.pos.y, it.pos.x + cos(it.angle) * it.radius, it.pos.y + sin(it.angle) * it.radius)
        }

        fill(0)
        circle(coords.x, coords.y, 0.1f)

        fill(Color.RED.rgb)
        circle(collision.x, collision.y, 0.1f)
    }

    private fun drawBarrier() {
        noFill()
        stroke(0f)
        strokeWeight(0.05f)

        rect(minX, minY, maxX - minX, maxY - minY)
    }

    var coords = Float2()

    override fun mousePressed() {
        val (ball, localC) = getBallFromCoords(mousePos) ?: return
        onTick = {
            ball.run {
                coords = getWorldCoords(localC)

                val (f, m) = getForceAndMomentum(ball, coords, (mousePos - coords))
                force += f
                momentum += m
            }
        }
    }

    override fun mouseReleased() {
        coords = Float2(-1f, -1f)
        onTick = {}
    }

    override fun mouseDragged() {
        mousePos = Float2(mouseX.toFloat(), mouseY.toFloat()).toWorldCoords()
    }

    override fun mouseMoved() {
        mousePos = Float2(mouseX.toFloat(), mouseY.toFloat()).toWorldCoords()
    }
}

var mousePos = Float2()

fun main() {
    PApplet.main(Main::class.java)
    startPhysics()
}