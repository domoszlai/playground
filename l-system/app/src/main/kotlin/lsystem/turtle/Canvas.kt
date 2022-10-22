package lsystem.turtle

import kotlin.math.min
import kotlin.math.max
import kotlin.math.abs
import kotlin.math.sqrt

sealed interface DrawCommand
data class MoveTo(val p: Point3D) : DrawCommand
data class LineTo(val p: Point3D) : DrawCommand

data class Point3D(val x: Double, val y: Double, val z: Double)
operator fun Point3D.plus(o: Point3D) : Point3D
    = Vector3D(x+o.x, y+o.y, z+o.z)
operator fun Point3D.minus(o: Point3D) : Point3D
        = Vector3D(x-o.x, y-o.y, z-o.z)


typealias Vector3D = Point3D
operator fun Vector3D.times(m: Double) : Vector3D
    = Vector3D(x*m, y*m, z*m)
operator fun Vector3D.unaryMinus() : Vector3D
    = Vector3D(-x, -y, -z)
fun Vector3D.cross(o: Vector3D) : Vector3D
    = Vector3D(y*o.z - z*o.y,z*o.x - x*o.z,x*o.y - y*o.x)
fun Vector3D.length() : Double = sqrt(x*x+y*y+z*z)
fun Vector3D.normalize() : Vector3D {
    val len = length()
    return Vector3D(x / len, y / len, z / len)
}

data class Rectangle(
    val p1: Point3D,
    val p2: Point3D = p1) {
    val width = abs(p2.x - p1.x)
    val height = abs(p2.y - p1.y)
    val depth = abs(p2.z - p1.z)
}

// Standard Cartesian coordinate system
class Canvas () {

    // From (0,0) unless first command is MoveTo
    var path = listOf<DrawCommand>()
    var boundaries = Rectangle(Point3D(0.0,0.0,0.0))

    fun moveTo(x: Double, y: Double, z: Double){
        appendToPath(MoveTo(Point3D(x, y, z)))
    }

    fun moveTo(p: Point3D){
        appendToPath(MoveTo(p))
    }

    fun lineTo(x: Double, y: Double, z: Double){
        appendToPath(LineTo(Point3D(x, y, z)))
    }

    fun lineTo(p: Point3D){
        appendToPath(LineTo(p))
    }

    private fun appendToPath(cmd: DrawCommand) {
        path = path + cmd
        updateBoundaries(cmd)
    }

    private fun updateBoundaries(cmd: DrawCommand) {
        if(path.size == 1 && cmd is MoveTo) {
            boundaries = Rectangle(cmd.p)
        }

        val p = when (cmd) {
            is MoveTo -> cmd.p
            is LineTo -> cmd.p
        }

        boundaries = Rectangle(
            Point3D(
                min(boundaries.p1.x, p.x),
                min(boundaries.p1.y, p.y),
                min(boundaries.p1.z, p.z)),
            Point3D(
                max(boundaries.p2.x, p.x),
                max(boundaries.p2.y, p.y),
                max(boundaries.p2.z, p.z))
        )
    }
}