package hittable

import Hit
import Hittable
import Material
import Ray
import Vector3
import kotlin.math.sqrt

class Sphere : Hittable {
    private val center : Vector3
    private val radius : Float
    private val material : Material

    constructor(center: Vector3, radius: Float, material: Material) : super() {
        this.center = center
        this.radius = radius
        this.material = material
    }

    override fun hit(ray: Ray, tMin: Float, tMax: Float): Hit? {
        val oc = ray.origin - center
        val a = ray.direction.lengthSquared()
        val halfB = oc.dot(ray.direction)
        val c = oc.lengthSquared() - radius * radius
        val discriminant = halfB * halfB - a * c
        if(discriminant < 0) {
            return null
        }
        val sqrtD = sqrt(discriminant)
        var root = (-halfB - sqrtD) / a
        if(root < tMin || tMax < root) {
            root = (-halfB + sqrtD) / a
            if(root < tMin || tMax < root) {
                return null
            }
        }

        val p = ray.at(root)
        return Hit(root, p, (p - center) / radius, material)
    }
}