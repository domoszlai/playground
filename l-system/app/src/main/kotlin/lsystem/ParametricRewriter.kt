package lsystem

import kotlin.reflect.KClass
import kotlin.reflect.cast

private data class ParametricRule<T>(
    val symbol: KClass<*>, val condition: ((Any)->Boolean), val replacement: ((Any)->List<T>))

class ParametricRewriter<B:Any> {

    private var rules: Map<KClass<*>, List<ParametricRule<B>>> = mapOf()

    fun <T:B> addRule(symbol: KClass<T>, condition: ((T)->Boolean) = {true}, replacement: ((T)->List<B>)) {

        val rule = ParametricRule(
            symbol,
            fun (any: Any): Boolean {
                val t = symbol.cast(any)
                return condition(t)
            },
            fun (any: Any): List<B> {
                val t = symbol.cast(any)
                return replacement(t)
            })

        rules = rules + (symbol to rules.getOrDefault(symbol, listOf()) + rule)
    }

    fun <T:B> addRule(symbol: KClass<T>, replacement: List<B>) {

        val rule = ParametricRule(
            symbol,
            fun (_): Boolean {
                return true
            },
            fun (_): List<B> {
                return replacement
            })

        rules = rules + (symbol to rules.getOrDefault(symbol, listOf()) + rule)
    }

    fun <T:B> addRule(rule: Pair<KClass<T>, List<B>>)
        = addRule(rule.first, rule.second)

    private fun rewrite(symbol: B) : List<B> {
        val match = rules.getOrDefault(symbol.javaClass.kotlin, listOf())
        for(rule in match){
            if(rule.condition(symbol)){
                return rule.replacement(symbol)
            }
        }
        return listOf(symbol)
    }

    fun rewrite(axiom: List<B>, n: Int) : List<B>
        = (1..n).fold(axiom){axiom, _ -> axiom.flatMap {rewrite(it)}}
}