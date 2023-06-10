import kotlin.math.sqrt

class Sphere : Hittable {
    private var center : Vector3
    private var radius : Float
    constructor(center: Vector3, radius: Float) : super() {
        this.center = center
        this.radius = radius
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
        return Hit(root, p, (p - center) / radius)
    }
}