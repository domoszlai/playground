package lsystem

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

private fun fmt(d: Double) : String{
    val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    df.maximumFractionDigits = 5
    return df.format(d)
}

private fun buildSVGPathString(path: List<DrawCommand>) : String {
    return buildString {
        for(cmd in path) {
            when (cmd) {
                is MoveTo -> append("M${fmt(cmd.x)} ${fmt(cmd.y)} ")
                is LineTo -> append("L${fmt(cmd.x)} ${fmt(cmd.y)} ")
            }
        }
    }
}

private fun getMinimumLineLength(path: List<DrawCommand>) : Double {

    var minimumLength = Double.MAX_VALUE

    path.fold(Pair(0.0, 0.0), fun (prevPoint, cmd) : Pair<Double, Double> {
        return when (cmd) {
            is MoveTo -> Pair(cmd.x, cmd.y)
            is LineTo -> {
                minimumLength = min(minimumLength,
                    sqrt((prevPoint.first - cmd.x).pow(2) + (prevPoint.second - cmd.y).pow(2))
                )

                Pair(cmd.x, cmd.y)
            }
        }
    })

    return minimumLength
}

fun Canvas.toSVG(width: Int, height: Int): String {
    // Heuristic to get some proper stroke width for any arbitrary detailed drawings
    val strokeWidth = getMinimumLineLength(path)/10

    return buildString {
        append("<svg height=\"$height\" width=\"$width\"")
        append(" viewBox=\"0 0 ${fmt(boundaries.width)} ${fmt(boundaries.height)}\">\n")
        // Transform the image coordinate system to SVG (invert vertically)
        append("<g transform=\"translate(0,${fmt(boundaries.height)}) scale(1,-1)\">\n")
        append("<g transform=\"translate(${fmt(-boundaries.x1)},${fmt(-boundaries.y1)})\">\n")
        append("<path d=\"${buildSVGPathString(path)}\"")
        append( " stroke=\"black\" stroke-width=\"${fmt(strokeWidth)}\" fill=\"none\"/>\n")
        append("</g>\n")
        append("</g>\n")
        append("</svg>\n")
    }
}