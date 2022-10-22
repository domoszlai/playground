package lsystem

import kotlin.test.Test
import kotlin.test.assertEquals

import lsystem.turtle.TurtleParams

class EdgeRewriting {

    class ForwardLeft : Forward()
    class ForwardRight : Forward()

    val F = Forward()
    val Fl = ForwardLeft()
    val Fr = ForwardRight()
    val `+` = TurnLeft()
    val `-` = TurnRight()

    @Test fun kochIsland1() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, `-`, F, `+`, F, `+`, F, F, `-`, F, `-`, F, `+`, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 3)

        assertEquals(getResourceAsText("/edge/kochIsland1.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun kochIsland2() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, F, `-`, F, `-`, F, `-`, F, `-`, F, `-`, F, `+`, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 4)

        assertEquals(getResourceAsText("/edge/kochIsland2.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun kochIsland3() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, F, `-`, F, `-`, F, `-`, F, `-`, F, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 4)

        assertEquals(getResourceAsText("/edge/kochIsland3.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun kochIsland4() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, F, `-`, F, `+`, F, `-`, F, `-`, F, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 3)

        assertEquals(getResourceAsText("/edge/kochIsland4.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun kochIsland5() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, F, `-`, F, `-`, `-`, F, `-`, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 4)

        assertEquals(getResourceAsText("/edge/kochIsland5.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun kochIsland6() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, `-`, F, F, `-`, `-`, F, `-`, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 5)

        assertEquals(getResourceAsText("/edge/kochIsland6.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun kochIsland7() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, `-`, F, `+`, F, `-`, F, `-`, F))
        val svg = generateSVG(r, listOf(F, `-`, F, `-`, F, `-`, F), 4)

        assertEquals(getResourceAsText("/edge/kochIsland7.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun quadraticSnowflake() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(F.symbol to listOf(F, `+`, F, `-`, F, `-`, F, `+`, F))
        val svg = generateSVG(r, listOf(`-`, F), 4)

        assertEquals(getResourceAsText("/edge/quadraticSnowflake.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun dragonCurve() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(Fl.symbol to listOf(Fl, `+`, Fr, `+`))
        r.addRule(Fr.symbol to listOf(`-`, Fl, `-`, Fr))
        val svg = generateSVG(r, listOf(Fl), 10)

        assertEquals(getResourceAsText("/edge/dragonCurve.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun sierspinskiGasket() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(Fl.symbol to listOf(Fr, `+`, Fl, `+`, Fr))
        r.addRule(Fr.symbol to listOf(Fl, `-`, Fr, `-`, Fl))
        val svg = generateSVG(r, listOf(Fr), 6, TurtleParams(angleIncrementDegrees = 60.0))

        assertEquals(getResourceAsText("/edge/sierspinskiGasket.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }

    @Test fun gospelCurve() {

        val r = ParametricRewriter<TurtleRewriterNode>()
        r.addRule(Fl.symbol to listOf(Fl, `+`, Fr, `+`, `+`, Fr, `-`, Fl, `-`, `-`, Fl, Fl, `-`, Fr, `+`))
        r.addRule(Fr.symbol to listOf(`-`, Fl, `+`, Fr, Fr, `+`, `+`, Fr, `+`, Fl, `-`, `-`, Fl, `-`, Fr))
        val svg = generateSVG(r, listOf(Fl), 4, TurtleParams(angleIncrementDegrees = 60.0))

        assertEquals(getResourceAsText("/edge/gospelCurve.svg")?.deleteWhitespace(), svg.deleteWhitespace())
    }
}