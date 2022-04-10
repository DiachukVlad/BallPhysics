import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.distance
import dev.romainguy.kotlin.math.length
import dev.romainguy.kotlin.math.normalize
import processing.core.PApplet.sqrt
import java.awt.Color

val maxX = 42f
val maxY = 22f

val g = Float2(0f, -2f)

val balls = arrayOf(
    Ball(
        pos = Float2(5f, 1.5f),
        speed = Float2(2f, -0.2f)
    )
)

var collisions = arrayListOf<Pair<Pair<Ball, Ball?>, Float2>>()

var timer = 0f
var energy = 0f

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

        if (first.pos.x < first.radius) collisions.add(Pair(Pair(first, null), Float2(0f, first.pos.y)))
        if (first.pos.y < first.radius) collisions.add(Pair(Pair(first, null), Float2(first.pos.x, 0f)))
        if (first.pos.x > maxX - first.radius) collisions.add(Pair(Pair(first, null), Float2(maxX, first.pos.y)))
        if (first.pos.y > maxY - first.radius) collisions.add(Pair(Pair(first, null), Float2(first.pos.x, maxY)))
    }

    balls.forEach { it.force = Float2() }

    // calc forces
    collisions.forEach { (balls, p) ->
        val (a, b) = balls

        val del = a.radius - distance(a.pos, p)
        val normal = normalize(a.pos - p)
        var f = normal * del * a.k
        if (b == null) f *= 2f
        a.force += f
        b?.let { it.force += -normal * del * a.k }
    }

    // update speed and pos
    balls.forEach {
        it.speed += (it.force / it.m) * timeF
        it.pos += it.speed * timeF

        energy += it.m * length(it.speed).let { it * it } / 2 - g.y * it.m  * it.pos.y
    }
}