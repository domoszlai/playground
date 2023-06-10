class Ray {
    var origin: Point3
    var direction: Vector3
    constructor(origin: Vector3, direction: Vector3) {
        this.origin = origin
        this.direction = direction
    }

    fun at(t: Float): Vector3 {
        return origin + direction * t
    }
}