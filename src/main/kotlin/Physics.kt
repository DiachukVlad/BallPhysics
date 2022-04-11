import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.distance
import dev.romainguy.kotlin.math.length
import dev.romainguy.kotlin.math.normalize
import processing.core.PApplet.*
import processing.core.PConstants
import java.awt.Color
import kotlin.math.PI

var onTick = {}

val maxX = 42f
val minX = 1f

val maxY = 22f
val minY = 1f

val g = Float2(0f, -9.81f)

val balls = arrayOf(
    Ball(
        pos = Float2(5f, 5f),
        speed = Float2(),
        angle = -PConstants.PI / 4,
        angSpeed = 0.2f
    )
)

var collisions = arrayListOf<Pair<Pair<Ball, Ball?>, Float2>>()

var timer = 0f
var energy = 0f

var collision = Float2()

fun tick(timeF: Float) {
    timer += timeF
    energy = 0f

    // look for collisions
    collisions.clear()
    balls.forEach { first ->
        balls.forEach { second ->
            if (first != second) {
                if (distance(first.pos, second.pos) < first.radius + second.radius) {
                    collisions.add(Pair(Pair(first, second), middlePoint(first.pos, second.pos)))
                }
            }
        }

        if (first.pos.x < minX + first.radius) collisions.add(Pair(Pair(first, null), Float2(minX, first.pos.y)))
        if (first.pos.y < minY + first.radius) collisions.add(Pair(Pair(first, null), Float2(first.pos.x, minY)))
        if (first.pos.x > maxX - first.radius) collisions.add(Pair(Pair(first, null), Float2(maxX, first.pos.y)))
        if (first.pos.y > maxY - first.radius) collisions.add(Pair(Pair(first, null), Float2(first.pos.x, maxY)))
    }

    balls.forEach {
        it.force = Float2()
        it.momentum = 0f
    }

    onTick()

    collision = Float2()
    // calc forces
    collisions.forEach { (balls, p) ->
        val (a, b) = balls

        collision = p

        val del = a.radius - distance(a.pos, p)
        val normal = normalize(a.pos - p)
        var f = normal * del * a.k
        if (b == null) f *= 2f
        a.force += f
        b?.let { it.force += -normal * del * a.k }

        a.speed *= 0.995f
    }

    // update speed and pos
    balls.forEach {
        it.speed += (it.force / it.m + g) * timeF
        it.pos += it.speed * timeF

        it.angSpeed += it.momentum / it.m * timeF
        it.angle += it.angSpeed * timeF


        energy += it.m * length(it.speed).let { it * it } / 2 - g.y * it.m * it.pos.y
    }
}

fun getForceAndMomentum(ball: Ball, coords: Float2, force: Float2): Pair<Float2, Float> {
    val alf = angle(ball.pos - coords) - angle(force)
    val m = length(force) * distance(ball.pos, coords) * sin(alf)

    val fEnd = coords + force - ball.pos
    val fNor = normalize(fEnd)
    val f = fNor * (length(fEnd) - length(coords-ball.pos))
    return f to m
}