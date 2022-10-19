package lsystem

private data class Rule<T>(val left: T, val right: List<T>)

class Rewriter<T> {

    private var rules: Map<T, List<T>> = mapOf()

    fun addRule(rule : Pair<T, List<T>>) {
        rules = rules + rule
    }

    fun rewrite(axiom: List<T>, n: Int)
        = (1..n).fold(axiom){axiom, _ -> axiom.flatMap {rules.getOrDefault(it, listOf(it))}}
}