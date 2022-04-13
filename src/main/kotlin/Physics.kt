import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.distance
import dev.romainguy.kotlin.math.length
import dev.romainguy.kotlin.math.normalize
import dev.romainguy.kotlin.math.dot
import processing.core.PApplet.*

var onTick = {}

val maxX = 42f
val minX = 1f

val maxY = 22f
val minY = 1f

val g = Float2(0f, -9.81f)

val balls = arrayOf(
    Ball(
        pos = Float2(5f, 10f),
        speed = Float2(5f, 5f),
        angSpeed = 5f,
        m = 1f,
        frictionCoeff = 5f
    ),
    Ball(
        pos = Float2(20f, 11.8f),
        speed = Float2(0f, 0f),
        angSpeed = 0f,
        m = 1f
    )
)

var collisions = arrayListOf<Pair<Pair<Ball, Ball?>, Float2>>()

var timer = 0f
var energy = 0f

var collision = Float2()

fun tick(dt: Float) {
    timer += dt
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
//        it.force = g * it.m
        it.force = Float2()
        it.torque = 0f
    }

    onTick()

    collision = Float2()
    // calc forces
    collisions.forEach { (balls, p) ->
        val (a, b) = balls

        collision = p

        val dr = a.radius - distance(a.pos, p)
        val normalToP = normalize(a.pos - p)
        val normalInP = normalize(p - a.pos).rotate(-PI / 2f)

        // spring force
        val springForce = dr * a.k
        var vecSpringForce = normalToP * springForce
        if (b == null) vecSpringForce *= 2f
        a.force += vecSpringForce
        b?.let { it.force -= vecSpringForce}

        // get force from speed and rotation
        val speedInP = a.angSpeed / a.radius
        val vectorSpeedInP = a.speed - normalInP * speedInP

        val forceInP = a.m * vectorSpeedInP / dt / 2f
        var projSpeedInP = forceInP projectOn normalInP

        // get friction force
        val maxFrictionForce = springForce * a.frictionCoeff
        if (length(projSpeedInP) > maxFrictionForce) {
            projSpeedInP = normalize(projSpeedInP) * maxFrictionForce
        }

        // apply friction force for objects
        if (b!=null) {
            val (fr, m) = getForceAndMomentum(a, p, -projSpeedInP)

            a.force += fr
            a.torque += m

            b.force += -fr
            b.torque += m
        } else {
            val (fr, m) = getForceAndMomentum(a, p, -projSpeedInP)

            a.force += fr
            a.torque += m
            println(m)
        }
    }

    // update speed and pos
    balls.forEach {
        it.speed += (it.force / it.m) * dt
        it.pos += it.speed * dt

        it.angSpeed += it.torque / it.m * dt
        it.angle += it.angSpeed * dt

        energy += it.m * length(it.speed).let { it * it } / 2 - g.y * it.m * it.pos.y
    }
}

fun getForceAndMomentum(ball: Ball, coords: Float2, force: Float2): Pair<Float2, Float> {
    val alf = angle(ball.pos - coords) - angle(force)
    val m = length(force) * distance(ball.pos, coords) * sin(alf)

    val fEnd = coords + force - ball.pos
    val fNor = normalize(fEnd)
    val f = fNor * (length(fEnd) - length(coords - ball.pos))
    return f to m
}

infix fun Float2.projectOn(b: Float2): Float2 {
    return b / length(b) * dot(this, b) / length(b)
}