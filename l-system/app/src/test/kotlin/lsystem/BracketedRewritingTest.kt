package lsystem

import kotlin.test.Test
import kotlin.test.assertEquals

import lsystem.turtle.TurtleParams

class BracketedRewritingTest {

    class XNode : TurtleRewriterNode()

    val F = ForwardDrawing()
    val X = XNode()
    val `+` = TurnLeft()
    val `-` = TurnRight()
    val `(` = PushState()
    val `)` = PopState()

    @Test fun axial1() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, `(`, `+`, F, `)`, F, `(`, `-`, F, `)`, F))
        val svg = generateSVG(r, listOf(F), 5, TurtleParams(angleIncrementDegrees = 25.7))

        assertEquals(getResourceAsText("/bracketed/axial1.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun axial2() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, `(`, `+`, F, `)`, F, `(`, `-`, F, `)`, `(`, F, `)`))
        val svg = generateSVG(r, listOf(F), 5, TurtleParams(angleIncrementDegrees = 20.0))

        assertEquals(getResourceAsText("/bracketed/axial2.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun axial3() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, F, `-`, `(`, `-`, F, `+`, F,  `+`, F, `)`,`+`, `(`, `+`, F, `-`, F, `-`, F, `)`))
        val svg = generateSVG(r, listOf(F), 4, TurtleParams(angleIncrementDegrees = 22.5))

        assertEquals(getResourceAsText("/bracketed/axial3.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun axial4() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(X.symbol to listOf(F, `(`, `+`, X, `)`, F, `(`, `-`, X, `)`, `+`, X ))
        r.addRule(F.symbol to listOf(F, F))
        val svg = generateSVG(r, listOf(X), 7, TurtleParams(angleIncrementDegrees = 20.0))

        assertEquals(getResourceAsText("/bracketed/axial4.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun axial5() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(X.symbol to listOf(F, `(`, `+`, X, `)`, `(`, `-`, X, `)`, F, X ))
        r.addRule(F.symbol to listOf(F, F))
        val svg = generateSVG(r, listOf(X), 7, TurtleParams(angleIncrementDegrees = 25.7))

        assertEquals(getResourceAsText("/bracketed/axial5.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun axial6() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(X.symbol to listOf(F, `-`, `(`, `(`, X, `)`, `+`, X, `)`, `+`, F, `(`, `+`, F, X, `)`, `-`, X ))
        r.addRule(F.symbol to listOf(F, F))
        val svg = generateSVG(r, listOf(X), 5, TurtleParams(angleIncrementDegrees = 22.5))

        assertEquals(getResourceAsText("/bracketed/axial6.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }
}