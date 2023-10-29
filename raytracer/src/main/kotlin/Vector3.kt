import kotlin.math.sqrt

typealias Point3 = Vector3

data class Vector3(val x: Float, val y: Float, val z: Float) {

    constructor(x: Int, y: Int, z: Int) : this(x.toFloat(), y.toFloat(), z.toFloat())
    constructor(v: Vector3) : this(v.x, v.y, v.z)

    operator fun plus(v: Vector3): Vector3 {
        return Vector3(x + v.x, y + v.y, z + v.z)
    }

    operator fun minus(v: Vector3): Vector3 {
        return Vector3(x - v.x, y - v.y, z - v.z)
    }

    operator fun times(c: Int): Vector3 {
        val f = c.toFloat()
        return Vector3(x * f, y * f, z * f)
    }

    operator fun times(c: Float): Vector3 {
        return Vector3(x * c, y * c, z * c)
    }

    operator fun times(v: Vector3): Vector3 {
        return Vector3(x * v.x, y * v.y, z * v.z)
    }

    operator fun div(c: Int): Vector3 {
        return this * (1f / c.toFloat())
    }

    operator fun div(c: Float): Vector3 {
        return this * (1f / c)
    }

    fun dot(v: Vector3): Float {
        return x * v.x + y * v.y + z * v.z
    }

    fun cross(v: Vector3): Vector3 {
        return Vector3(y * v.z - z * v.y, z * v.x - x * v.z, x * v.y - y * v.x)
    }

    fun normalize(): Vector3 {
        return this / length()
    }

    fun length(): Float {
        return sqrt(x * x + y * y + z * z)
    }

    fun lengthSquared(): Float {
        return x * x + y * y + z * z
    }

    fun nearZero(): Boolean {
        val s = 1e-8f
        return x < s && x > -s && y < s && y > -s && z < s && z > -s
    }
}