package lsystem

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.min
import kotlin.math.max
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

sealed interface DrawCmd
data class MoveTo(val x: Double, val y: Double) : DrawCmd
data class LineTo(val x: Double, val y: Double) : DrawCmd

data class Rectangle(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double) {
    val width = abs(x2 - x1)
    val height = abs(y2 - y1)
}

class Canvas (private val width: Int, private val height: Int) {

    private fun getBoundaries(path: List<DrawCmd>) : Rectangle {

        // Go over first MoveTos to figure out start point
        val beginningMoves = path.takeWhile { it is MoveTo }
        var startPoint = beginningMoves.fold(Rectangle(0.0,0.0,0.0,0.0)) {
            rect, m -> if (m is MoveTo) Rectangle(m.x, m.y, m.x, m.y) else rect
        }

        var remaining = path.dropWhile { it is MoveTo }
        return remaining.fold(startPoint, fun (rect, cmd) : Rectangle {
            val (x,y) = when (cmd) {
                is MoveTo -> Pair(cmd.x, cmd.y)
                is LineTo -> Pair(cmd.x, cmd.y)
            }
            return Rectangle(
                min(rect.x1, x),
                min(rect.y1, y),
                max(rect.x2, x),
                max(rect.y2, y)
            )
        })
    }

    private fun getMinimalLineLength(path: List<DrawCmd>) : Double {

        // Go over first MoveTos to figure out start point
        /*val beginningMoves = path.takeWhile { it is MoveTo }
        var startPoint = beginningMoves.fold(Pair(0.0, 0.0)) {
                startPoint, m -> if (m is MoveTo) Pair(m.x, m.y) else startPoint
        }*/

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

    fun toSVG(path: List<DrawCmd>) : String {
        val b = getBoundaries(path)
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
        df.maximumFractionDigits = 5;
        return df.format(d)
    }

    private fun buildSVGPathString(path: List<DrawCmd>) : String {
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