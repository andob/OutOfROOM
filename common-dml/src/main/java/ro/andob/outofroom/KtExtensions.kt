package ro.andob.outofroom

fun Boolean.toInt()  = if(this) 1  else 0
fun Boolean.toLong() = if(this) 1L else 0L

inline fun <TCursor : ICursor, R> TCursor.use(block : (TCursor) -> R) : R =
    this.let { cursor -> try { block(cursor) } finally { cursor.close() } }

inline fun <TStatement : IStatement, R> TStatement.use(block : (TStatement) -> R) : R =
    this.let { statement -> try { block(statement) } finally { statement.close() } }
