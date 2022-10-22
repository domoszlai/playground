package lsystem.turtle.output

import lsystem.turtle.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

// SVG generation works with a projection of the XY plane of the 3D space

private fun fmt(d: Double) : String{
    val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    df.maximumFractionDigits = 5
    return df.format(d)
}

private fun buildSVGPathString(path: List<DrawCommand>) : String {
    return buildString {
        for(cmd in path) {
            when (cmd) {
                is MoveTo -> append("M${fmt(cmd.p.x)} ${fmt(cmd.p.y)} ")
                is LineTo -> append("L${fmt(cmd.p.x)} ${fmt(cmd.p.y)} ")
            }
        }
    }
}

private fun getMinimumLineLength(path: List<DrawCommand>) : Double {

    var minimumLength = Double.MAX_VALUE

    path.fold(Point3D(0.0, 0.0, 0.0), fun (prevPoint, cmd) : Point3D {
        return when (cmd) {
            is MoveTo -> cmd.p
            is LineTo -> {

                minimumLength = min(minimumLength,
                    sqrt((prevPoint.x - cmd.p.x).pow(2) + (prevPoint.y - cmd.p.y).pow(2))
                )

                cmd.p
            }
        }
    })

    return minimumLength
}

fun Canvas.toSVG(width: Int? = null, height: Int? = null): String {
    // Heuristic to get some proper stroke width for any arbitrary detailed drawings
    val strokeWidth = getMinimumLineLength(path)/10

    return buildString {
        append("<svg")
        if(height != null) append(" height=\"$height\"")
        if(width != null) append(" width=\"$width\"")
        append(" viewBox=\"0 0 ${fmt(boundaries.width)} ${fmt(boundaries.height)}\">\n")
        // Transform the image coordinate system to SVG (invert vertically)
        append("<g transform=\"translate(0,${fmt(boundaries.height)}) scale(1,-1)\">\n")
        append("<g transform=\"translate(${fmt(-boundaries.p1.x)},${fmt(-boundaries.p1.y)})\">\n")
        append("<path d=\"${buildSVGPathString(path)}\"")
        append( " stroke=\"black\" stroke-width=\"${fmt(strokeWidth)}\" fill=\"none\"/>\n")
        append("</g>\n")
        append("</g>\n")
        append("</svg>")
    }
}