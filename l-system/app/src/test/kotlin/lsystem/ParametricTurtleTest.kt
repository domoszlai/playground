package lsystem

import lsystem.turtle.TurtleParams
import org.junit.Test
import kotlin.math.sqrt
import kotlin.test.assertEquals

class ParametricTurtleTest {

    @Test
    fun rowOfTrees() {

        data class F(val x: Double, val t: Int): ForwardDrawing(x)

        val `+` = TurnLeft()
        val `-` = TurnRight()

        val c = 1
        val p = 0.3
        val q = c - p
        val h = sqrt(p * q)

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F::class, {it.t == 0}, {listOf(
            F(it.x * p, 2), `+`, F(it.x * h, 1), `-`, `-`, F(it.x * h, 1), `+`, F(it.x * q, 0))})
        r.addRule(F::class, {it.t > 0}, {listOf(F(it.x, it.t - 1))})

        val svg = generateSVG(r, listOf(F(1.0, 0)), 10, TurtleParams(angleIncrementDegrees = 86.0))

        assertEquals(getResourceAsText("/turtle/parametric/rowOfTrees.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test
    fun noname1() {

        data class F(val x: Double): ForwardDrawing(x)
        data class A(val s: Double): TurtleRewriterNode()

        val `+` = TurnLeft()
        val `-` = TurnRight()
        val `(` = PushState()
        val `)` = PopState()

        val R = 1.456

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(A::class, replacement = {listOf(
            F(it.s),
            `(`, `+`, A(it.s / R), `)`,
            `(`, `-`, A(it.s / R), `)`)})

        val svg = generateSVG(r, listOf(A(1.0)), 10, TurtleParams(angleIncrementDegrees = 86.0))

        assertEquals(getResourceAsText("/turtle/parametric/noname1.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

}