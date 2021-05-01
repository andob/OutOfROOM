package ro.andob.outofroom.querybuilder

interface IQueryBuilderFilter
{
    val search : String?
    val offset : Int
    val limit : Int
}
