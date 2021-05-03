package ro.andob.outofroom

class ForeignKey
(
    private val sourceColumn : Column,
    private val destinationTable : Table,
    private val destinationColumn : Column,
)
{
    override fun toString() = "foreign key (`$sourceColumn`) references `$destinationTable`(`$destinationColumn`)"
}
