package lsystem.turtle.output

import lsystem.turtle.*
import kotlin.math.cos
import kotlin.math.sin

class Circle(val center: Point3D, val radius: Double, val normal: Vector3D) {

    fun pointAt(angleRadian: Double): Point3D {
        val n = normal.normalize()

        // v1 is arbitrary vector orthogonal to n
        val v1 = if(n.x != 0.0 || n.z != 0.0)
            Vector3D(n.z,0.0, -n.x).normalize()
        else
            Vector3D(n.y,0.0, -n.x).normalize();

        // v1, v2 and n are orthogonal -> base
        val v2 = n.cross(v1)
        return center + (v1 * cos(angleRadian) + v2 * sin(angleRadian)) * radius;
    }
}