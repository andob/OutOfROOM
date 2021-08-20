## OutOfRoom

OutOfRoom is a Database Abstraction Layer developed and used to replace the code written with ROOM ORM from my projects. This library is not an ORM and does not pretend to become one. It's just a simple tool to keep ORM-less persistence code clean and organized.

### Why?

I have stopped using ORMs. Speeds up initial development, but on large project, ORMs become a bottleneck, too many hacks need to be done if you use very specific SQL features. ORMs also prevents the developer from the ordeal of having to write adapting code between the relational paradigm and object oriented paradigm. Not using an ORM will yield to (some) minimal boilerplate code, yet flexibility advantages are enormous. You can find numerous articles and opinions about this online.

Library goals:

- being the simplest thing possible, while providing a clean approach to persist data
- decoupling database schema from models (getting away from annotations in models)
- ability to use system SQLite or [the latest SQLite version provided by requery](https://github.com/requery/sqlite-android)
- providing maximum flexibility to the developer

### How to import?

```groovy
allprojects {
    repositories {
        maven { url 'https://maven.andob.info/repository/open_source/' }
    }
}
```

To use with system's SQLite:

```groovy
dependencies {
    implementation 'ro.andob.outofroom:common-ddl:1.1.4'
    implementation 'ro.andob.outofroom:common-dml:1.1.4'
    implementation 'ro.andob.outofroom:common-query-builder:1.1.4'
    implementation 'ro.andob.outofroom:binding-system-sqlite:1.1.4'
}
```

To use with [the latest SQLite version provided by requery](https://github.com/requery/sqlite-android):

```groovy
dependencies {
    implementation 'ro.andob.outofroom:common-ddl:1.1.4'
    implementation 'ro.andob.outofroom:common-dml:1.1.4'
    implementation 'ro.andob.outofroom:common-query-builder:1.1.4'
    implementation 'ro.andob.outofroom:binding-latest-sqlite:1.1.4'
    implementation 'com.github.requery:sqlite-android:3.35.5'
    implementation 'androidx.sqlite:sqlite-ktx:2.1.0'
}
```

### Defining the models

Define your models as simple POJOs, without any annotations:

```kotlin
data class Note
(
    val id : String,
    val title : String,
    val contents : String,
    val color : NoteColor,
)
```

```kotlin
enum class NoteColor(val id : Int)
{
    White(1),
    Yellow(2),
    Green(3),
}
```

### Defining the database schema

```kotlin
class NotesDatabaseSchema : Schema()
{
    val noteTable = NoteTable()
    class NoteTable : Table(name = "Note")
    {
        val id = Column(name = "id", type = SQLType.Text, notNull = true)
        val title = Column(name = "title", type = SQLType.Text)
        val contents = Column(name = "contents", type = SQLType.Text)
        val color = Column(name = "color", type = SQLType.Integer)

        override val primaryKey get() = PrimaryKey(id)
        override val foreignKeys get() = listOf<ForeignKey>()
    }
    
    //todo more tables...

    override val indices get() = listOf<Index>(
        Index(table = noteTable, column = noteTable.title),
        Index(table = noteTable, column = noteTable.contents)
    )
}
```

API reference:

- ``Schema`` class represents the database schema, containing tables and indices. This class also have a propery ``tables`` that will return a list of all the tables defined inside.
- ``Table`` class represents a database table, containing columns, primary and foreign keys. Table classes have a property ``columns`` that will return a list of all the columns inside the table. There is also a method ``toCreateTableSQL`` that returns a string with the equivalent ``create table ...`` SQL command.
- ``Column`` class represents a column from a table. Columns are defined by their name and type. By default columns are nullable, but you can add ``notNull = true``.
- ``SQLType`` enum contains SQLite type definitions: Integer, Real, Text, Blob.
- ``PrimaryKey`` can be simple: ``PrimaryKey(id)``, composed: ``PrimaryKey(id, name)``  or simple with autoincrement: ``PrimaryKey.AutoIncrement(id)``
- ``Index`` class represents a table index. Indices can also be unique (just add ``unique = true`` to the index definition to make it unique). There is also a method ``toCreateIndexSQL`` that returns a string with the equivalent ``create index ...`` SQL command.

### Defining the database open helper

```kotlin
class NotesDatabaseOpenHelper
(
    private val schema : NotesDatabaseSchema
) : SQLiteOpenHelper
(
    /*context*/ App.context,
    /*name*/ "notes.db",
    /*cursorFactory*/ null,
    /*version*/ 1,
    /*onCorruption*/ { throw Error("Detected a corrupt database!") }
)
{
    override fun onCreate(db : SQLiteDatabase)
    {
        for (table in schema.tables)
            db.execSQL(table.toCreateTableSQL())

        for (index in schema.indices)
            db.execSQL(index.toCreateIndexSQL())
    }

    override fun onConfigure(db : SQLiteDatabase)
    {
        super.onConfigure(db)
        db.enableWriteAheadLogging()
    }

    override fun onUpgrade(db : SQLiteDatabase?, oldVersion : Int, newVersion : Int)
    {
        //todo to migrate, use a migration manager such as Flyway or write your own
    }
}
```

### Defining the database / DAO container object

```kotlin
object NotesDatabase
{
    private val openHelper = NotesDatabaseOpenHelper(schema)

    private val entityManager get() = openHelper.toEntityManager()
    private val schema get() = NotesDatabaseSchema()

    fun noteDao() = NoteDao(schema, entityManager)
    //more DAOs
}
```

Define DAO classes similar to the ones defined with ROOM.

```kotlin
class NoteDao
(
    private val schema : NotesDatabaseSchema,
    private val entityManager : EntityManager,
)
{
    //DAO methods...
}
```

### Defining adapter methods inside DAOs

One must define adapter methods that will convert:

- `populateInsertData(insertData : InsertData, note : Note)` from model into data to be inserted.
- ``parseQueryResult(queryResult : QueryResult) : Note`` from data resulted from query into model. A QueryResult object will be equivalent to a ROW of a result table of a query.

```kotlin
class NoteDao
(
    private val schema : NotesDatabaseSchema,
    private val entityManager : EntityManager,
)
{
    private fun populateInsertData(insertData : InsertData, note : Note)
    {
        insertData.putString(schema.noteTable.id, note.id)
        insertData.putString(schema.noteTable.title, note.title)
        insertData.putString(schema.noteTable.contents, note.contents)
        insertData.putNoteColor(schema.noteTable.color, note.color)
    }

    private fun parseQueryResult(queryResult : QueryResult) : Note
    {
        return Note(
            id = queryResult.getString(schema.noteTable.id)!!,
            title = queryResult.getString(schema.noteTable.title)?:"",
            contents = queryResult.getString(schema.noteTable.contents)?:"",
            color = queryResult.getNoteColor(schema.noteTable.color),
        )
    }
}
```

InsertData and QueryResult classes have getter / setter methods such as:

```kotlin
InsertData:
fun putBoolean(column : Column, value : Boolean?) { ... }
fun putDouble(column : Column, value : Double?) { ... }
fun putFloat(column : Column, value : Float?) { ... }
fun putInt(column : Column, value : Int?) { ... }
fun putLong(column : Column, value : Long?) { ... }
fun putString(column : Column, value : String?) { ... }

QueryResult:
fun getBoolean(column : Column) : Boolean { ... }
fun getDouble(column : Column) : Double? { ... }
fun getFloat(column : Column) : Float? { ... }
fun getInt(column : Column) : Int? { ... }
fun getLong(column : Column) : Long? { ... }
fun getString(column : Column) : String? { ... }

//converts first cell of the row (QueryResult) into:
fun toBoolean() : Boolean { ... }
fun toDouble() : Double { ... }
fun toFloat() : Float { ... }
//...
```

Of course not all class field types are primitives or strings. You can define custom "FieldAdapters" by just creating extension methods on ``InsertData`` and ``QueryResult`` classes. This would be equivalent to ROOM converter methods:

```kotlin
//file NotesDatabaseFieldAdapters.kt
private fun findNoteColor(id : Int) : NoteColor? = NoteColor.values().find { it.id==id }
fun InsertData.putNoteColor(column : Column, noteColor : NoteColor) = putInt(column, noteColor.id)
fun QueryResult.getNoteColor(column : Column) = getInt(column)?.let(::findNoteColor)?:NoteColor.White
```

### DAO - inserting / updating data

Use ``entityManager.insert()`` to insert or update data:

```kotlin
class NoteDao
(
    private val schema : NotesDatabaseSchema,
    private val entityManager : EntityManager,
)
{
    fun insert(note : Note, or : InsertOr = InsertOr.Fail)
    {
        entityManager.insert(or = or,
            table = schema.noteTable,
            columns = schema.noteTable.columns,
            adapter = { insertData -> populateInsertData(insertData, note) })
    }

    fun update(note : Note) =
        insert(note, or = InsertOr.Replace)

    private fun populateInsertData(insertData : InsertData, note : Note) ...
    private fun parseQueryResult(queryResult : QueryResult) : Note ...
}
```

Usage:

```kotlin
val note = Note(
    id = UUID.randomUUID().toString(),
    title = "test", contents = "test",
    color = NoteColor.White)

NotesDatabase.noteDao().insert(note)
NotesDatabase.noteDao().update(note)
```

### DAO - executing SQL commands

Use ``entityManager.exec()`` to execute SQL statements that don't have a result.

```kotlin
class NoteDao
(
    private val schema : NotesDatabaseSchema,
    private val entityManager : EntityManager,
)
{
    fun delete(note : Note)
    {
        entityManager.exec(
            sql = """delete from ${schema.noteTable}
                     where ${schema.noteTable.id} = ?""",
            arguments = arrayOf(note.id))
    }
    
    fun deleteAll()
    {
        entityManager.exec("delete from ${schema.noteTable}")
    }

    private fun populateInsertData(insertData : InsertData, note : Note) ...
    private fun parseQueryResult(queryResult : QueryResult) : Note ...
}
```

Note: to pass arguments, use prepared statement syntax (? inside the query, then ``arguments = arrayOf(...)``). This is useful for security reasons, to prevent SQL injection.

Usage:

```kotlin
NotesDatabase.noteDao().delete(note)
NotesDatabase.noteDao().deleteAll()
```

### DAO - fetching data

Use ``entityManager.query`` to execute SQL statements that have a result:

```kotlin
class NoteDao
(
    private val schema : NotesDatabaseSchema,
    private val entityManager : EntityManager,
)
{
    fun getAll() : List<Note>
    {
        return entityManager.query(
            sql = "select * from ${schema.noteTable}",
            adapter = ::parseQueryResult)
    }

    fun getById(noteId : String) : Note?
    {
        return entityManager.query(
            sql = """select * from ${schema.noteTable}
                     where ${schema.noteTable.id} = ?
                     limit 1""",
            arguments = arrayOf(noteId),
            adapter = ::parseQueryResult
        ).firstOrNull()
    }

    fun getByIds(noteIds : List<String>) : List<Note>
    {
        return entityManager.query(
            sql = """select * from ${schema.noteTable}
                     where ${schema.noteTable.id} in (${questionMarks(noteIds)})""",
            arguments = noteIds.toTypedArray(),
            adapter = ::parseQueryResult)
    }
    
    fun count() : Int
    {
        return entityManager.query(
            sql = "select count(*) from ${schema.noteTable}",
            adapter = { queryResult -> queryResult.toInt() }
        ).firstOrNull()?:0
    }

    private fun populateInsertData(insertData : InsertData, note : Note) ...
    private fun parseQueryResult(queryResult : QueryResult) : Note ...
}
```

Note: to pass arguments, use prepared statement syntax (? inside the query, then ``arguments = arrayOf(...)``). This is useful for security reasons, to prevent SQL injection.

Note: to pass a list of arguments (as in ``where .. in ..`` clause), use prepared statement syntax (questionMarks() to generate a string of ?,?,?..., then ``arguments = ...``). This is useful for security reasons, to prevent SQL injection.

Usage:

```kotlin
val allNotes = NotesDatabase.noteDao().getAll()
val someNote = NotesDatabase.noteDao().getById(note.id)
val someNotes = NotesDatabase.noteDao().getByIds(listOf(note.id))
val noteCount = NotesDatabase().noteDao().count()
```

### DAO - using the query builder

This library also contains a "Query Builder", a clone of my library [ROOM-Dynamic-Dao](https://github.com/andob/ROOM-Dynamic-Dao). With it, you can convert filter objects into select SQL commands. Please read the tutorial from the [ROOM-Dynamic-Dao](https://github.com/andob/ROOM-Dynamic-Dao) documentation. The syntax is very similar:

```kotlin
data class NoteFilter
(
    override val limit : Int,
    override val offset : Int,
    override val search : String?
) : IQueryBuilderFilter
```

```kotlin
class NoteDao
(
    private val schema : NotesDatabaseSchema,
    private val entityManager : EntityManager,
)
{
    fun getFiltered(noteFilter : NoteFilter) : List<Note>
    {
        return entityManager.query(
            sql = NoteQueryBuilder(schema, noteFilter).build(),
            adapter = ::parseQueryResult)
    }

    private fun populateInsertData(insertData : InsertData, note : Note) ...
    private fun parseQueryResult(queryResult : QueryResult) : Note ...
}
```

```kotlin
class NoteQueryBuilder
(
    private val schema : NotesDatabaseSchema,
    filter : NoteFilter,
) : QueryBuilder<NoteFilter>(filter)
{
    override fun table() = schema.noteTable

    override fun where(conditions : QueryWhereConditions) : String
    {
        if (filter.search != null)
        {
            conditions.addSearchConditions(
                search = filter.search, columns = arrayOf(
                    schema.noteTable.title,
                    schema.noteTable.contents,
                ))
        }

        return conditions.mergeWithAnd()
    }
}
```

```kotlin
val notes = NotesDatabase.noteDao().getFiltered(NoteFilter(search = "test", limit = 100, offset = 0))
```

### Using system SQLite vs Latest SQLite from Requery

To use this library with system SQLite (the SQLite library bundled in the Android operating system), just import relevant components:

```
    implementation 'ro.andob.outofroom:binding-system-sqlite:1.1.4'
```

```
import ro.andob.outofroom.system_sqlite.toEntityManager
```

```
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
```

To use this library with the latest SQLite version provided by requery, just import relevant components:

```
    implementation 'ro.andob.outofroom:binding-latest-sqlite:1.1.4'
```

```
import ro.andob.outofroom.latest_sqlite.toEntityManager
```

```
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteOpenHelper
```

By using the requery SQLite compatibility library, a version of the SQLite library will be bundled with your app. This will yield in larger APK size. Advantages of using latest SQLite: speed, security fixes, all your app users will use the exact same SQLite version across a wide range of devices.

### Migrating from ROOM

This library does not provide an automatic tool to migrate from ROOM. The recommended way to migrate is:

- Write unit tests on the entire persistence layer (on ALL methods from all DAOs)
- Replace ROOM with OutOfRoom, rewrite code keeping DAO API (method signatures) intact
- Run persistence unit tests again, fix the errors

### License

```
Copyright 2021 Andrei Dobrescu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```