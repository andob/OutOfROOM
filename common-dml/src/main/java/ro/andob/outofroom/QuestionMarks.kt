package ro.andob.outofroom

fun <T> questionMarks(items : Collection<T>?) : String = questionMarks(times = items?.size?:1)

fun questionMarks(times : Int = 1) : String = ArrayUtils.createQuestionMarks(times)
