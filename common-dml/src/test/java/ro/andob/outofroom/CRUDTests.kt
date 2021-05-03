package ro.andob.outofroom

import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import ro.andob.outofroom.model.Note
import ro.andob.outofroom.model.StringId
import kotlin.random.Random

class CRUDTests
{
    private val randomizer = Random(System.currentTimeMillis())
    private fun Random.nextString() = StringId.newRandomUUID<Any>().toString()

    private fun Random.nextNote() = Note(
        id = StringId.newRandomUUID(),
        title = nextString(),
        message = nextString(),
        someInt = nextInt(1, 100),
        someLong = nextLong(1L, 100L),
        someFloat = nextInt(1, 100).toFloat()/100.0f,
        someDouble = nextInt(1, 100).toDouble()/100.0,
        someBoolean = nextBoolean(),
    )

    @Test
    fun testCreate()
    {
        TestDatabase.noteDao.deleteAll()
        assert(TestDatabase.noteDao.getAll().isEmpty())

        val note1=randomizer.nextNote()
        TestDatabase.noteDao.insert(note1)
        assertEquals(1, TestDatabase.noteDao.getAll().size)
        assertEquals(note1, TestDatabase.noteDao.getAll().first())

        try { TestDatabase.noteDao.insert(note1); fail() }
        catch (ex : Exception) { ex.printStackTrace() }

        val note2=randomizer.nextNote()
        TestDatabase.noteDao.insert(note2)
        assertEquals(2, TestDatabase.noteDao.getAll().size)
        assertEquals(note1, TestDatabase.noteDao.getAll().find { it.id==note1.id }!!)
        assertEquals(note2, TestDatabase.noteDao.getAll().find { it.id==note2.id }!!)
    }

    @Test
    fun testRead()
    {
        TestDatabase.noteDao.deleteAll()
        assert(TestDatabase.noteDao.getAll().isEmpty())

        val notes=(1..100).map { randomizer.nextNote() }
        notes.forEach { note -> TestDatabase.noteDao.insert(note) }

        assertEquals(100, TestDatabase.noteDao.getAll().size)
        assertEquals(notes, TestDatabase.noteDao.getAll())

        repeat(times = 100) {
            val note=notes.random()
            val noteFromDB=TestDatabase.noteDao.getById(note.id)
            assertEquals(note, noteFromDB)
        }
    }

    @Test
    fun testUpdate()
    {
        TestDatabase.noteDao.deleteAll()
        assert(TestDatabase.noteDao.getAll().isEmpty())

        val note1=randomizer.nextNote()
        TestDatabase.noteDao.insert(note1)
        assertEquals(1, TestDatabase.noteDao.getAll().size)
        assertEquals(note1, TestDatabase.noteDao.getAll().first())

        val note2=note1.copy(title = randomizer.nextString(), message = randomizer.nextString())
        TestDatabase.noteDao.update(note2)
        assertEquals(1, TestDatabase.noteDao.getAll().size)
        assertEquals(note2, TestDatabase.noteDao.getAll().first())
    }

    @Test
    fun testDelete()
    {
        TestDatabase.noteDao.deleteAll()
        assert(TestDatabase.noteDao.getAll().isEmpty())

        val note=randomizer.nextNote()
        TestDatabase.noteDao.insert(note)
        assertEquals(1, TestDatabase.noteDao.getAll().size)
        assertEquals(note, TestDatabase.noteDao.getAll().first())

        TestDatabase.noteDao.delete(note)
        assert(TestDatabase.noteDao.getAll().isEmpty())
    }
}
