package lsystem

import java.io.File

fun generateSVG(rewriter: Rewriter<TurtleRewriterNode>, axiom: List<TurtleRewriterNode>, n: Int, params: TurtleParams = TurtleParams()) {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    val svg = turtle.canvas.toSVG()
    File("c:\\users\\dlacko\\desktop\\gen.svg").writeText(svg)
}

fun generateObj(rewriter: Rewriter<TurtleRewriterNode>, axiom: List<TurtleRewriterNode>, n: Int, params: TurtleParams = TurtleParams()) {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    val obj = turtle.canvas.toObj()
    File("c:\\users\\dlacko\\desktop\\gen.obj").writeText(obj)
}

fun main() {

    // Edge rewriting
    val F = TurtleRewriterNode.createForwardDrawingNode()
    val Fl = TurtleRewriterNode.createForwardDrawingNode()
    val Fr = TurtleRewriterNode.createForwardDrawingNode()
    val f = TurtleRewriterNode.createForwardNonDrawingNode()
    val `+` = TurtleRewriterNode.createTurnLeftNode()
    val `-` = TurtleRewriterNode.createTurnRightNode()
    val `|` = TurtleRewriterNode.createTurnAroundNode()
    val `&` = TurtleRewriterNode.createPitchDownNode()
    val `^` = TurtleRewriterNode.createPitchUpNode()
    val `Rf` = TurtleRewriterNode.createRollLeftNode()
    val `Rr` = TurtleRewriterNode.createRollRightNode()
    val `(` = TurtleRewriterNode.createPushStateNode()
    val `)` = TurtleRewriterNode.createPopStateNode()
    // Node rewriting
    val L = TurtleRewriterNode.createCustomNode("L")
    val R = TurtleRewriterNode.createCustomNode("R")
    val X = TurtleRewriterNode.createCustomNode("X")
    val A = TurtleRewriterNode.createCustomNode("A")
    val B = TurtleRewriterNode.createCustomNode("B")
    val C = TurtleRewriterNode.createCustomNode("C")
    val D = TurtleRewriterNode.createCustomNode("D")

    val kochIslandGenerator = Rewriter<TurtleRewriterNode>()
    kochIslandGenerator.addRule(F to listOf(F, `-`, F, `+`, F, `+`, F, F, `-`, F, `-`, F, `+`, F))
    //generateSVG(kochIslandGenerator, listOf(F, `-`, F, `-`, F, `-`, F), 3)

    val quadraticSnowflake = Rewriter<TurtleRewriterNode>()
    quadraticSnowflake.addRule(F to listOf(F, `+`, F, `-`, F, `-`, F, `+`, F))
    //generateSVG(quadraticSnowflake, listOf(`-`, F), 4)

    val gosperCurve = Rewriter<TurtleRewriterNode>()
    gosperCurve.addRule(Fl to listOf(Fl, `+`, Fr, `+`, `+`, Fr, `-`, Fl, `-`, `-`, Fl, Fl, `-`, Fr, `+`))
    gosperCurve.addRule(Fr to listOf(`-`, Fl, `+`, Fr, Fr, `+`, `+`, Fr, `+`, Fl, `-`, `-`, Fl, `-`, Fr))
    //generateSVG(gosperCurve, listOf(Fl), 4, TurtleParams(angleIncrementDegrees = 60.0))

    val macroTile3x3 = Rewriter<TurtleRewriterNode>()
    macroTile3x3.addRule(L to listOf(L, F, `+`, R, F, R, `+`, F, L, `-`, F, `-`, L, F, L, F, L, `-`, F, R, F, R, `+`))
    macroTile3x3.addRule(R to listOf(`-`, L, F, L, F, `+`, R, F, R, F, R, `+`, F, `+`, R, F, `-`, L, F, L, `-`, F, R))
    //generateSVG(macroTile3x3, listOf(`-`, L), 3)

    val peanoCurve = Rewriter<TurtleRewriterNode>()
    peanoCurve.addRule(L to listOf(L, F, R, F, L, `-`, F, `-`, R, F, L, F, R, `+`, F, `+`, L, F, R, F, L))
    peanoCurve.addRule(R to listOf(R, F, L, F, R, `+`, F, `+`, L, F, R, F, L, `-`, F, `-`, R, F, L, F, R))
    //generateSVG(peanoCurve, listOf(L), 2)

    val axial1 = Rewriter<TurtleRewriterNode>()
    axial1.addRule(F to listOf(F, `(`, `+`, F, `)`, F, `(`, `-`, F, `)`, F))
    //generateSVG(axial1, listOf(F), 5, TurtleParams(angleIncrementDegrees = 25.7))

    val axial2 = Rewriter<TurtleRewriterNode>()
    axial2.addRule(F to listOf(F, `(`, `+`, F, `)`, F, `(`, `-`, F, `)`, `(`, F, `)`))
    //generateSVG(axial2, listOf(F), 5, TurtleParams(angleIncrementDegrees = 20.0))

    val axial4 = Rewriter<TurtleRewriterNode>()
    axial4.addRule(X to listOf(F, `(`, `+`, X, `)`, F, `(`, `-`, X, `)`, `+`, X, ))
    axial4.addRule(F to listOf(F, F))
    //generateSvg(axial4, listOf(X), 7, TurtleParams(angleIncrementDegrees = 20.0))

    val hilbertCurve3D = Rewriter<TurtleRewriterNode>()
    hilbertCurve3D.addRule(A to listOf(B, `-`, F, `+`, C, F, C, `+`, F, `-`, D, `&`, F, `^`, D, `-`, F, `+`, `&`, `&`, C, F, C, `+`, F, `+`, B, Rr, Rr ))
    hilbertCurve3D.addRule(B to listOf(A, `&`, F, `^`, C, F, B, `^`, F, `^`, D, `^`, `^`, `-`, F, `-`, D, `^`, `|`, F, `^`, B, `|`, F, C, `^`, F, `^`, A, Rr, Rr ))
    hilbertCurve3D.addRule(C to listOf(`|`, D, `^`, `|`, F, `^`, B, `-`, F, `+`, C, `^`, F, `^`, A, `&`, `&`, F, A, `&`, F, `^`, C, `+`, F, `+`, B, `^`, F, `^`, D, Rr, Rr))
    hilbertCurve3D.addRule(D to listOf(`|`, C, F, B, `-`, F, `+`, B, `|`, F, A, `&`, F, `^`, A, `&`, `&`, F, B, `-`, F, `+`, B, `|`, F, C, Rr, Rr))
    generateObj(hilbertCurve3D, listOf(A), 3)

    val test = Rewriter<TurtleRewriterNode>()
    //generateObj(test, listOf(F,F,F,`-`,F,F,`-`,F,`-`,F,`+`,F,`+`,F,F,`-`,F,`-`,F,F,F), 1)
    //generateObj(test, listOf(F,F,F,`&`,F), 1)
}
