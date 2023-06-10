class Camera {

    private val aspectRatio = 16f / 9f
    private val viewportHeight = 2.0f
    private val viewportWidth = aspectRatio * viewportHeight
    private val focalLength = 1.0f

    private val origin = Point3(0, 0, 0)
    private val horizontal = Vector3(viewportWidth, 0f, 0f)
    private val vertical = Vector3(0f, viewportHeight, 0f)
    private val lowerLeftCorner = origin - horizontal / 2f - vertical / 2f - Vector3(0f, 0f, focalLength)

    fun getRay(u: Float, v: Float): Ray {
        return Ray(origin, lowerLeftCorner + horizontal * u + vertical * v - origin)
    }

}