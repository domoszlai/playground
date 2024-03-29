import hittable.Sphere
import material.Lambertian
import material.Metal
import kotlin.math.sqrt

fun writeColor(pixelColor: Color,  samplesPerPixel: Int) {
    val r = pixelColor.r
    val g = pixelColor.g
    val b = pixelColor.b

    // Divide the color by the number of samples and gamma-correct for gamma=2.0.
    val scale = 1.0f / samplesPerPixel
    val rScaled = sqrt(scale * r)
    val gScaled = sqrt(scale * g)
    val bScaled = sqrt(scale * b)

    // Write the translated [0,255] value of each color component.
    println("${(256 * rScaled.coerceIn(0.0f, 0.999f)).toInt()} " +
            "${(256 * gScaled.coerceIn(0.0f, 0.999f)).toInt()} " +
            "${(256 * bScaled.coerceIn(0.0f, 0.999f)).toInt()} ")
}

fun rayColor(ray: Ray, hittable: Hittable, depth: Int) : Color {
    // If we've exceeded the ray bounce limit, no more light is gathered.
    if (depth <= 0)
        return Color.BLACK

    val rec = hittable.hit(ray, Interval(0.001f, Float.MAX_VALUE))
    return if(rec != null) {
        val scatterResult = rec.material.scatter(ray, rec)
        if(scatterResult != null)
            scatterResult.attenuation * rayColor(scatterResult.scattered, hittable, depth - 1)
        else
            Color.BLACK
    }
    else {
        val unitDirection = ray.direction.normalize()
        val t = 0.5f * (unitDirection.y + 1.0f)
        (Color(1f, 1f, 1f) * (1.0f - t)) + (Color(0.5f, 0.7f, 1f) * t)
    }
}

fun main() {

    // Image

    val aspectRatio = 16.0f / 9.0f
    val imageWidth = 400
    val imageHeight = (imageWidth / aspectRatio).toInt()
    val samplesPerPixel = 100
    val maxDepth = 5

    // World

    val world = HittableList(listOf(
        Sphere(Point3(0f, 0f, -1f), 0.5f, Lambertian(Color(0.7f, 0.3f, 0.3f))),
        Sphere(Point3(0f, -100.5f, -1f), 100f, Lambertian(Color(0.8f, 0.8f, 0.0f))),
        Sphere(Point3(-1f, 0f, -1f), 0.5f, Metal(Color(0.8f, 0.8f, 0.8f), 0.3f)),
        Sphere(Point3(1f, 0f, -1f), 0.5f, Metal(Color(0.8f, 0.6f, 0.2f), 1f)),
    ))

    // Camera

    val cam = Camera(imageWidth, imageHeight)

    // Render

    println("P3\n$imageWidth $imageHeight\n255")

    for (j in 0 until imageHeight) {
        System.err.println("Scanlines remaining: $j")
        for (i in 0 until imageWidth) {
            var pixelColor = Color(0f, 0f, 0f)
            for (sample in 0 until samplesPerPixel) {
                val r = cam.getRay(i, j)
                pixelColor += rayColor(r, world, maxDepth)
            }
            writeColor(pixelColor, samplesPerPixel)
        }
    }

    System.err.println("Done")
}