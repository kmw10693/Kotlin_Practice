val number2 = """([+-]?[.\d]+)"""
val groupMD2 = """$number2([*/])$number2""".toRegex()
val plus2 = number2.toRegex()
val trim2 = """[^.\d-+*/()]""".toRegex()
val bracket2 = """\(([^)]+)\)""".toRegex()

fun reduceBracket(v:String):String{
    var str = v.replace(trim2, "")
    while(bracket2.containsMatchIn(str)) str = str.replace(bracket2){"${calc2(it.groupValues[1])}"}
    return str
}

fun calc2(v:String):Double{
    var str = reduceBracket(v)
    while(groupMD2.containsMatchIn(str)){
        str = str.replace(groupMD2){
            val (_, left, op, right) = it.groupValues
            val leftValue = left.toDouble()
            val rightValue = right.toDouble()
            "${when(op){
                "*" -> leftValue * rightValue
                "/" -> leftValue / rightValue
                else -> throw Throwable("invalid operator $op")
            }}"
        }
    }
    return plus2.findAll(str).fold(0.0){acc, curr-> acc + curr.groupValues[1].toDouble()}
}
