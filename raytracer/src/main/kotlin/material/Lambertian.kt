package material

import Color
import Hit
import Material
import Ray
import ScatterResult
import randomUnitVector

class Lambertian : Material {

    private val albedo: Color

    constructor(albedo: Color){
        this.albedo = albedo
    }

    override fun scatter(ray: Ray, record: Hit): ScatterResult {
        var scatterDirection = record.normal + randomUnitVector()

        // Catch degenerate scatter direction
        if (scatterDirection.nearZero())
            scatterDirection = record.normal

        val scattered = Ray(record.p, scatterDirection)
        return ScatterResult(scattered, albedo)
    }
}