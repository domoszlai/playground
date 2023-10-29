import kotlin.random.Random

class Camera (imageWidth: Int, imageHeight: Int) {

    private val aspectRatio = imageWidth.toFloat() / imageHeight.toFloat()

    // Camera
    private val focalLength = 1.0f
    private val viewportHeight = 2.0f
    private val viewportWidth = aspectRatio * viewportHeight
    private val cameraCenter = Point3(0, 0, 0)

    // Calculate the vectors across the horizontal and down the vertical viewport edges.
    private val viewportU = Vector3(viewportWidth, 0f, 0f)
    private val viewportV = Vector3(0f, -viewportHeight, 0f)

    // Calculate the horizontal and vertical delta vectors from pixel to pixel.
    private val pixelDeltaU = viewportU / imageWidth
    private val pixelDeltaV = viewportV / imageHeight

    // Calculate the location of the upper left pixel.
    private val viewportUpperLeft = cameraCenter - viewportU / 2f - viewportV / 2f - Vector3(0f, 0f, focalLength)
    private val pixel00 = viewportUpperLeft + (pixelDeltaU + pixelDeltaV) * 0.5f

    // Get a randomly sampled camera ray for the pixel at location i,j.
    fun getRay(i: Int, j: Int): Ray {
        val pixelCenter = pixel00 + (pixelDeltaU * i) + (pixelDeltaV * j)
        val pixelSample = pixelCenter + pixelSampleSquare();

        val rayOrigin = cameraCenter;
        val rayDirection = pixelSample - rayOrigin;
        return Ray(rayOrigin, rayDirection)
    }

    // Returns a random point in the square surrounding a pixel at the origin.
    private fun pixelSampleSquare(): Vector3 {
        val px = Random.nextFloat() - 0.5f
        val py = Random.nextFloat() - 0.5f
        return pixelDeltaU * px + pixelDeltaV * py
    }
}