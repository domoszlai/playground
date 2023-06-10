data class Hit(
    val t: Float,
    val p: Point3,
    val normal: Vector3
    )

interface Hittable {
    fun hit(ray: Ray, tMin: Float, tMax: Float): Hit?
}

class HittableList(private val objects: List<Hittable>) : Hittable {
    override fun hit(ray: Ray, tMin: Float, tMax: Float): Hit? {
        var rec: Hit? = null
        var closestSoFar = tMax
        for (obj in objects) {
            val objRec = obj.hit(ray, tMin, closestSoFar)
            if (objRec != null) {
                closestSoFar = objRec.t
                rec = objRec
            }
        }
        return rec
    }
}