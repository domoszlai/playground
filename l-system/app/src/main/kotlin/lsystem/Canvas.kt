package lsystem

import kotlin.math.min
import kotlin.math.max
import kotlin.math.abs

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

// Standard Cartesian coordinate system
class Canvas () {

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
}