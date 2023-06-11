import kotlin.random.Random

data class ScatterResult(val scattered: Ray, val attenuation: Color)

interface Material {
    fun scatter(ray: Ray, hit: Hit): ScatterResult?
}

fun randomInUnitSphere() : Vector3 {
    var p: Vector3
    do {
        p = Vector3(Random.nextFloat(), Random.nextFloat(), Random.nextFloat()) * 2f - Vector3(1.0f, 1.0f, 1.0f)
    } while (p.lengthSquared() >= 1.0)
    return p
}

fun randomUnitVector() : Vector3 {
    return randomInUnitSphere().normalize()
}