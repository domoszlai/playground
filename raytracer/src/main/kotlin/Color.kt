data class Color(val r: Float, val g: Float, val b: Float) {

    constructor(r: Int, g: Int, b: Int):
        this(r.toFloat(), g.toFloat(), b.toFloat())

    companion object {
        val BLACK = Color(0, 0, 0)
    }

    constructor(vector3: Vector3):
        this(vector3.x, vector3.y, vector3.z)

    operator fun times(c: Float): Color {
        return Color(r  * c, g * c, b * c)
    }

    operator fun times(v: Color): Color {
        return Color(r  * v.r, g * v.g, b * v.b)
    }

    operator fun plus(v: Color): Color {
        return Color(r  + v.r, g + v.g, b + v.b)
    }
}