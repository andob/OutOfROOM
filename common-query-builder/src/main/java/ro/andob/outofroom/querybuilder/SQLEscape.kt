package ro.andob.outofroom.querybuilder

import ro.andob.outofroom.QueryArgumentConverter

object SQLEscape
{
    @JvmStatic
    fun escapeString(string : String) : String
    {
        try
        {
            return DatabaseUtils.sqlEscapeString(string)
        }
        catch (ex : Exception)
        {
            val escaper = StringBuilder()
            escaper.append('\'')
            if (string.indexOf('\'') != -1)
            {
                for (character in string.toCharArray())
                {
                    if (character=='\'')
                        escaper.append('\'')
                    escaper.append(character)
                }

            }
            else escaper.append(string)
            escaper.append('\'')
            return escaper.toString()
        }
    }

    @JvmStatic
    fun escapeAndUnquoteString(string : String) : String
    {
        val escapedString = SQLEscape.escapeString(string)
        val unquotedEscapedString = escapedString.substring(1, escapedString.length-1)
        return unquotedEscapedString
    }

    @JvmStatic
    fun escapeStringArray(values : Array<String>) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add(escapeString(token))
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeStringCollection(values : Collection<String>) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add(escapeString(token))
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : IntArray) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : LongArray) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : DoubleArray) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberArray(values : FloatArray) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add("$token")
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeNumberCollection(values : Collection<*>) : String
    {
        val tokens = mutableListOf<String>()
        for (token in values)
            tokens.add(token.toString())
        return "(${tokens.joinToString(separator = ", ")})"
    }

    @JvmStatic
    fun escapeBoolean(value : Boolean) : Int =
        if (value) 1 else 0

    @JvmStatic
    fun escapeArray(array : Array<*>, queryArgumentConverter : QueryArgumentConverter) =
        escapeCollection(array.toList(), queryArgumentConverter)

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    fun escapeCollection(collection : Collection<*>, queryArgumentConverter : QueryArgumentConverter) : String
    {
        if (collection.isEmpty())
            throw RuntimeException("Cannot escape empty collection")

        (collection.first() as? String)?.let {
            return escapeStringCollection(collection as Collection<String>)
        }

        (collection.first() as? Number)?.let {
            return escapeNumberCollection(collection as Collection<Number>)
        }

        val convertedCollection = collection.map(queryArgumentConverter::convert)
        (convertedCollection.first() as? String)?.let {
            return escapeStringCollection(convertedCollection as Collection<String>)
        }

        (convertedCollection.first() as? Number)?.let {
            return escapeNumberCollection(convertedCollection as Collection<Number>)
        }

        throw RuntimeException("Cannot escape ${collection.first()!!::class.java.name}")
    }
}