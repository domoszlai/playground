package lsystem

import kotlin.test.Test
import kotlin.test.assertEquals

class NodeRewriting {

    class LNode : TurtleRewriterNode()
    class RNode : TurtleRewriterNode()

    val F = Forward()
    val L = LNode()
    val R = RNode()
    val `+` = TurnLeft()
    val `-` = TurnRight()

    @Test fun macroTile3x3() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(L.symbol to listOf(L, F, `+`, R, F, R, `+`, F, L, `-`, F, `-`, L, F, L, F, L, `-`, F, R, F, R, `+`))
        r.addRule(R.symbol to listOf(`-`, L, F, L, F, `+`, R, F, R, F, R, `+`, F, `+`, R, F, `-`, L, F, L, `-`, F, R))
        val svg = generateSVG(r, listOf(`-`, L), 3)

        assertEquals(getResourceAsText("/node/macroTile3x3.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun peanoCurve() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(L.symbol to listOf(L, F, R, F, L, `-`, F, `-`, R, F, L, F, R, `+`, F, `+`, L, F, R, F, L))
        r.addRule(R.symbol to listOf(R, F, L, F, R, `+`, F, `+`, L, F, R, F, L, `-`, F, `-`, R, F, L, F, R))
        val svg = generateSVG(r, listOf(L), 3)

        assertEquals(getResourceAsText("/node/peanoCurve.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }
}