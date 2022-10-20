package lsystem

fun generateSVG(rewriter: Rewriter<TurtleRewriterNode>, axiom: List<TurtleRewriterNode>, n: Int, params: TurtleParams = TurtleParams()) {
    val turtle = Turtle(params)
    turtle.execute(rewriter, axiom, n)
    val svg = turtle.canvas.toSVG()
    println(svg)
}

fun main() {

    // Edge rewriting
    val F = TurtleRewriterNode.createForwardDrawingNode()
    val Fl = TurtleRewriterNode.createForwardDrawingNode()
    val Fr = TurtleRewriterNode.createForwardDrawingNode()
    val f = TurtleRewriterNode.createForwardNonDrawingNode()
    val `+` = TurtleRewriterNode.createLeftNode()
    val `-` = TurtleRewriterNode.createRightNode()
    val `(` = TurtleRewriterNode.createPushStateNode()
    val `)` = TurtleRewriterNode.createPopStateNode()
    // Node rewriting
    val L = TurtleRewriterNode.createCustomNode("L")
    val R = TurtleRewriterNode.createCustomNode("R")
    val X = TurtleRewriterNode.createCustomNode("X")

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
    generateSVG(axial4, listOf(X), 7, TurtleParams(angleIncrementDegrees = 20.0))

    val test = Rewriter<TurtleRewriterNode>()
    //generateSVG(axial4, listOf(F,F,F,`-`,F,F,`-`,F,`-`,F,`+`,F,`+`,F,F,`-`,F,`-`,F,F,F), 1)
}
