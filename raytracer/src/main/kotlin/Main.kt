import kotlin.math.sqrt
import kotlin.random.Random

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
        return Color(0,0,0)

    val rec = hittable.hit(ray, 0.001f, Float.MAX_VALUE)
    return if(rec != null) {
        val target = rec.p + rec.normal + randomUnitVector()
        rayColor(Ray(rec.p, target - rec.p), hittable, depth - 1) * 0.5f
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
        Sphere(Point3(0f, 0f, -1f), 0.5f),
        Sphere(Point3(0f, -100.5f, -1f), 100f)))

    // Camera

    val cam = Camera()

    // Render

    println("P3\n$imageWidth $imageHeight\n255")

    for (y in imageHeight - 1 downTo 0) {
        System.err.println("Scanlines remaining: $y")
        for (x in 0 until imageWidth) {
            var pixelColor = Color(0f, 0f, 0f)
            for (s in 0 until samplesPerPixel) {
                val u = (x + Random.nextFloat()) / (imageWidth - 1)
                val v = (y + Random.nextFloat()) / (imageHeight - 1)
                val r = cam.getRay(u, v)
                pixelColor += rayColor(r, world, maxDepth)
            }
            writeColor(pixelColor, samplesPerPixel)
        }
    }

    System.err.println("Done")
}