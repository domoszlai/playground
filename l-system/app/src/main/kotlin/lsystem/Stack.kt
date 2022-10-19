package lsystem

typealias Stack<T> = List<T>

fun <T> Stack<T>.push(item: T): Stack<T> = this + item
fun <T> Stack<T>.pop(): Stack<T> = if (isNotEmpty()) this.dropLast(1) else this
fun <T> Stack<T>.peek(): T? = if (isNotEmpty()) this[lastIndex] else null
fun <T> Stack<T>.updateTop(upd: (item: T) -> T): Stack<T> {
    var top = peek()
    if(top != null){
        return pop().push(upd(top))
    }
    return this
}
