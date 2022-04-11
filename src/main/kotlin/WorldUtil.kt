import dev.romainguy.kotlin.math.Float2
import dev.romainguy.kotlin.math.distance

fun Float2.toWorldCoords(): Float2 {
    return (this * Float2(1f, -1f) + Float2(0f, HEIGHT)) / WORLD_SCALE
}

fun getBallFromCoords(coords: Float2): Pair<Ball, Float2>? {
    return balls.firstOrNull { distance(it.pos, coords) < it.radius }
        ?.let { Pair(it, it.getLocalCoords(coords)) }
}