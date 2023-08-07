package ro.andob.outofroom

object ArrayUtils
{
    @JvmStatic
    fun convertObjectArrayToStringArray(argumentConverter : QueryArgumentConverter, objects : Array<Any?>?) : Array<String>
    {
        if (objects==null)
            return arrayOf()

        val strings = Array(size = objects.size, init = { "" })
        for (i in objects.indices)
        {
            objects[i]?.let(argumentConverter::convert)
                ?.let { convertedObject -> strings[i] = convertedObject.toString() }
        }

        return strings
    }

    @JvmStatic
    fun createQuestionMarks(times : Int) : String
    {
        if (times<=1)
            return "?"

        val builder = StringBuilder()
        for (i in 0 until times)
        {
            builder.append('?')
            if (i!=times-1)
                builder.append(',')
        }

        return builder.toString()
    }
}
