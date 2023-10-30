data class Hit(
    val t: Float,
    val p: Point3,
    val normal: Vector3,
    val material: Material
    )

interface Hittable {
    fun hit(ray: Ray, t: Interval): Hit?
}

class HittableList(private val objects: List<Hittable>) : Hittable {
    override fun hit(ray: Ray, t: Interval): Hit? {
        var rec: Hit? = null
        var closestSoFar = t.max
        for (obj in objects) {
            val objRec = obj.hit(ray, Interval(t.min, closestSoFar))
            if (objRec != null) {
                closestSoFar = objRec.t
                rec = objRec
            }
        }
        return rec
    }
}