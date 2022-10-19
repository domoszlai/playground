package lsystem

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.min
import kotlin.math.max
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

sealed interface DrawCommand
data class MoveTo(val x: Double, val y: Double) : DrawCommand
data class LineTo(val x: Double, val y: Double) : DrawCommand

data class Rectangle(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double) {
    val width = abs(x2 - x1)
    val height = abs(y2 - y1)
}

class Canvas (private val width: Int, private val height: Int) {

    // From (0,0) unless first command is MoveTo
    var path = listOf<DrawCommand>()
    var boundaries = Rectangle(0.0, 0.0, 0.0, 0.0)

    fun moveTo(x: Double, y: Double){
        appendToPath(MoveTo(x, y))
    }

    fun lineTo(x: Double, y: Double){
        appendToPath(LineTo(x, y))
    }

    private fun appendToPath(cmd: DrawCommand) {
        path = path + cmd
        updateBoundaries(cmd)
    }

    private fun updateBoundaries(cmd: DrawCommand) {
        if(path.size == 1 && cmd is MoveTo) {
            boundaries = Rectangle(cmd.x, cmd.y, cmd.x, cmd.y)
        }

        val (x,y) = when (cmd) {
            is MoveTo -> Pair(cmd.x, cmd.y)
            is LineTo -> Pair(cmd.x, cmd.y)
        }

        boundaries = Rectangle(
            min(boundaries.x1, x),
            min(boundaries.y1, y),
            max(boundaries.x2, x),
            max(boundaries.y2, y)
        )
    }

    private fun getMinimalLineLength(path: List<DrawCommand>) : Double {

        var minimumLength = Double.MAX_VALUE

        //var remaining = path.dropWhile { it is MoveTo }
        path.fold(Pair(0.0, 0.0), fun (prevPoint, cmd) : Pair<Double, Double> {
            return when (cmd) {
                is MoveTo -> Pair(cmd.x, cmd.y)
                is LineTo -> {
                    minimumLength = min(minimumLength,
                        sqrt((prevPoint.first - cmd.x).pow(2) + (prevPoint.second - cmd.y).pow(2)))

                    Pair(cmd.x, cmd.y)
                }
            }
        })

        return minimumLength
    }

    fun toSVG() : String {
        val b = boundaries
        val strokeWidth = getMinimalLineLength(path)/10

        return buildString {
            append("<svg height=\"$height\" width=\"$width\"")
            append(" viewBox=\"${fmt(b.x1)} ${fmt(b.y1)} ${fmt(b.width)} ${fmt(b.height)}\">\n")
            append("<path d=\"${buildSVGPathString(path)}\"")
            // Heuristic...
            append( " stroke=\"black\" stroke-width=\"${fmt(strokeWidth)}\" fill=\"none\"/>\n")
            append("</svg>\n")
        }
    }

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
}