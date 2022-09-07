val trim = """[^.\d-+*/()]""".toRegex()

fun trim(v:String):String{
    return v.replace(trim, "")
}
fun repMtoPM(v:String) = v.replace("-", "+-")
val groupMD = """((?:\+|\+-)?[.\d]+)([*/])((?:\+|\+-)?[.\d]+)""".toRegex()

// 차원을 줄이는 행위는 fold
// findall 은 sequence를 리턴
// Passing Trail Lambdas
fun foldGroup(v:String):Double = groupMD.findAll(v).fold(0.0){ acc, curr->
    val (_, left, op, right) = curr.groupValues
    val leftValue = left.replace("+", "").toDouble()
    val rightValue = right.replace("+", "").toDouble()
    val result = when(op){
        "*"->leftValue * rightValue
        "/"->leftValue / rightValue
        else-> throw Throwable("invalid operator $op")
    }
    acc + result
}

fun calc(v:String) = foldGroup( repMtoPM(trim(v)))


