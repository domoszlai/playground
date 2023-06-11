package material

import Color
import Hit
import Material
import Ray
import ScatterResult
import Vector3
import randomInUnitSphere

class Metal : Material {

    private val albedo: Color
    private val fuzz: Float

    constructor(albedo: Color, f: Float) {
        this.albedo = albedo
        this.fuzz = if (f < 1) f else 1f
    }

    private fun reflect(v: Vector3, n: Vector3): Vector3 {
        return v - n * v.dot(n) * 2f
    }

    override fun scatter(ray: Ray, hit: Hit): ScatterResult? {
        val reflected = reflect(ray.direction.normalize(), hit.normal)
        val scattered = Ray(hit.p, reflected + randomInUnitSphere() * fuzz)
        return if (scattered.direction.dot(hit.normal) > 0) {
            ScatterResult(scattered, albedo)
        } else {
            null
        }
    }
}