import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation

@Target(AnnotationTarget.PROPERTY)
annotation class Ex
@Target(AnnotationTarget.PROPERTY)
annotation class Name(val name: String)

class Json2(@Ex val a:Int, @Name("title") val b:String)

fun stringify(value: Any) = StringBuilder().run{
    jsonValue(value)
    toString()
}

fun <T> Iterable<T>.joinTo(sep:()->Unit, transform:(T)->Unit){
    forEachIndexed { count, element ->
        if(count != 0) sep()
        transform(element)
    }
}

fun StringBuilder.jsonValue(value:Any?){
    when(value) {
        null -> append("null")
        is String -> jsonString(value)
        is Boolean, is Number -> append("$value")
        is List<*> -> jsonList(value)
        else -> jsonObject(value)
    }
}

private fun StringBuilder.jsonList(target:List<*>){
    wrap('[', ']') {
        target.joinTo({ append(',') }) {
            jsonValue(it)
        }
    }
}

private fun StringBuilder.jsonString(v: String) = append("""${v.replace("\"", "\\\"")}""")

private fun StringBuilder.jsonObject(target: Any) {
    wrap('{', '}') {
        target::class.members.filterIsInstance<KProperty<*>>().joinTo(::comma) {
            jsonValue(it.findAnnotation<Name>()?.name ?: it.name)
            append('.')
            jsonValue(it.getter.call(target))
        }
    }
}

fun StringBuilder.comma(){
    append(',')
}

fun StringBuilder.wrap(begin:Char, end:Char, block:StringBuilder.()->Unit){
    append(begin)
    block()
    append(end)
}

