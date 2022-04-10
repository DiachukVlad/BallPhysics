import processing.core.PApplet
import kotlin.concurrent.thread

class Main : PApplet() {
    override fun settings() {
        size(1280, 720)
    }

    override fun setup() {
        frameRate(60f)
        ellipseMode(PApplet.RADIUS)

        textSize(16f)
    }

    override fun draw() {
        scale(1f, -1f)
        translate(0f, -height.toFloat())

        scale(30f, 30f)

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
        text("$energy", 5f, 16*2f)
    }

    private fun drawFigures() {
        balls.forEach {
            noStroke()
            fill(it.color)
            circle(it.pos.x, it.pos.y, it.radius)
        }
    }

    private fun drawBarrier() {
        noFill()
        stroke(0f)
        strokeWeight(0.05f)

        rect(0f, 0f,maxX, maxY)
    }
}

fun main() {
    PApplet.main(Main::class.java)
    startPhysics()
}