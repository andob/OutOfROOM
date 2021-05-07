package ro.andob.outofroom

fun <T> `?`(items : Collection<T>?) : String = `?`(times = items?.size?:1)

fun `?`(times : Int = 1) : String = ArrayUtils.createQuestionMarks(times)
