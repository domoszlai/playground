package lsystem.turtle.output

import lsystem.turtle.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

// SVG generation works with a projection of the XY plane of the 3D space

private fun fmt(d: Double) : String{
    val df = DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH))
    df.maximumFractionDigits = 5
    return df.format(d)
}

private fun buildSVGPath(path: List<DrawCommand>) : String {
    var prevPoint: Point3D = Point3D(0.0, 0.0, 0.0)
    var prevLineWidth: Double = 0.0
    var inPath = false

    return buildString {
        for(cmd in path) {
            when (cmd) {
                is MoveTo ->
                    if(prevLineWidth == 0.0) {
                        prevPoint = cmd.p
                    }else {
                        append("M${fmt(cmd.p.x)} ${fmt(cmd.p.y)} ")
                    }
                is LineTo -> {
                    if (prevLineWidth != cmd.lineWidth) {

                        if(inPath)
                            append("\" stroke=\"black\" stroke-width=\"${fmt(prevLineWidth)}\" fill=\"none\"/>")

                        inPath = true
                        prevLineWidth = cmd.lineWidth

                        append("<path d=\"M${fmt(prevPoint.x)} ${fmt(prevPoint.y)}")
                    }

                    append(" L${fmt(cmd.p.x)} ${fmt(cmd.p.y)}")
                }
            }
        }

        if(inPath)
            append("\" stroke=\"black\" stroke-width=\"${fmt(prevLineWidth)}\" fill=\"none\"/>")
    }
}

fun Canvas.toSVG(width: Int? = null, height: Int? = null): String {

    return buildString {
        append("<svg")
        if(height != null) append(" height=\"$height\"")
        if(width != null) append(" width=\"$width\"")
        append(" viewBox=\"0 0 ${fmt(boundaries.width)} ${fmt(boundaries.height)}\">\n")
        // Transform the image coordinate system to SVG (invert vertically)
        append("<g transform=\"translate(0,${fmt(boundaries.height)}) scale(1,-1)\">\n")
        append("<g transform=\"translate(${fmt(-boundaries.p1.x)},${fmt(-boundaries.p1.y)})\">\n")
        append("${buildSVGPath(path)}\n")
        append("</g>\n")
        append("</g>\n")
        append("</svg>")
    }
}