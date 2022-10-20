package lsystem

fun Turtle.execute(
    rewriter: Rewriter<TurtleRewriterNode>,
    axiom: List<TurtleRewriterNode>,
    n: Int
){
    val nodes = rewriter.rewrite(axiom, n)
    var turtleCommands = nodes.flatMap { it.turtleCommands }
    this.execute(turtleCommands)
}

class TurtleRewriterNode(
    private val name: String,
    val turtleCommands: List<TurtleCommand>
) {
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    companion object {
        fun createForwardDrawingNode(name: String = "F"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PenDown, Forward))

        fun createForwardNonDrawingNode(name: String = "f"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PenUp, Forward))

        fun createTurnLeftNode(name: String = "+"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(TurnLeft))

        fun createTurnRightNode(name: String = "-"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(TurnRight))

        fun createTurnAroundNode(name: String = "|"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(TurnAround))

        fun createPitchDownNode(name: String = "&"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PitchDown))

        fun createPitchUpNode(name: String = "^"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PitchUp))

        fun createRollLeftNode(name: String = "\\"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(RollLeft))

        fun createRollRightNode(name: String = "/"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(RollRight))

        fun createCustomNode(name: String): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf())

        fun createPushStateNode(name: String = "["): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PushState))

        fun createPopStateNode(name: String = "]"): TurtleRewriterNode =
            TurtleRewriterNode(name, listOf(PopState))
    }
}