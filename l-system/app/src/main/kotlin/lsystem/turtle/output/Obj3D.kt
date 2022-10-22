package lsystem.turtle.output

import lsystem.turtle.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.PI
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

private fun fmt(d: Double) : String{
    val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    df.maximumFractionDigits = 5
    return df.format(d)
}

private fun getMinimumLineLength(path: List<DrawCommand>) : Double {

    var minimumLength = Double.MAX_VALUE

    path.fold(Point3D(0.0, 0.0, 0.0), fun (prevPoint, cmd) : Point3D {
        return when (cmd) {
            is MoveTo -> cmd.p
            is LineTo -> {

                minimumLength = min(minimumLength,
                    sqrt((prevPoint.x - cmd.p.x).pow(2) + (prevPoint.y - cmd.p.y).pow(2)+ (prevPoint.z - cmd.p.z).pow(2))
                )

                cmd.p
            }
        }
    })

    return minimumLength
}

typealias Vertex = Point3D
data class Face(val vertexIndexes: List<Int>)
data class Mesh(val vertices: List<Vertex>, val faces: List<Face>)

fun generateCylindricalMesh(p1: Point3D, p2: Point3D, r: Double): Mesh {
    val nrp = 50

    val n = p1 - p2
    val c1 = Circle(p1, r, n)
    val c2 = Circle(p2, r, n)
    val angle = 2 * PI / nrp

    var vertices = listOf<Vertex>()
    var faces = listOf<Face>()

    for (i in 0 until nrp) {
        vertices = vertices + c1.pointAt(angle * i)
    }

    for (i in 0 until nrp) {
        vertices = vertices + c2.pointAt(angle * i)
    }

    for (i in 1 until nrp) {
        faces = faces + Face(listOf(i, i + 1, i + nrp))
        faces = faces + Face(listOf(i + nrp, i + nrp + 1, i + 1))
    }

    faces = faces + Face(listOf(nrp, 1, nrp * 2))
    faces = faces + Face(listOf(nrp * 2, nrp + 1, 1))

    return Mesh(vertices, faces)
}

// https://nccastaff.bournemouth.ac.uk/jmacey/OldWeb/RobTheBloke/www/source/obj.html
fun Canvas.toObj(): String {
    // Heuristic to get some proper stroke width for any arbitrary detailed drawings
    val strokeWidth = getMinimumLineLength(path)/10

    var startPoint = Point3D(0.0, 0.0, 0.0)

    var vertices = mutableListOf<Vertex>()
    var faces = mutableListOf<Face>()

    for(cmd in path){
        startPoint = when(cmd){
            is MoveTo -> {
                cmd.p
            }
            is LineTo -> {
                val mesh = generateCylindricalMesh(startPoint, cmd.p, strokeWidth / 2)

                val idxShift = vertices.size
                vertices.addAll(mesh.vertices)
                faces.addAll(mesh.faces.map {
                    it.copy(vertexIndexes = it.vertexIndexes.map(fun (idx) = idx + idxShift))
                })

                cmd.p
            }
        }
    }

    return buildString {
        for(v in vertices){
            append("v ${fmt(v.x)} ${fmt(v.y)} ${fmt(v.z)}\n")
        }

        for(v in faces){
            append("f"+v.vertexIndexes.joinToString(separator = " ") { " $it"}+"\n")
        }
    }
}