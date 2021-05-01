package ro.andob.outofroom.querybuilder

fun String.removeUnnecessarySpaces() = this
    .trim()
    .replace("\n", " ")
    .replace("[ ]{2,}".toRegex(), " ")
    .replace(" ,", ",")
    .replace("( ", "(")
    .replace(" )", ")")
