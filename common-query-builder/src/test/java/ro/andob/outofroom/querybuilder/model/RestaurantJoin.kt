package ro.andob.outofroom.querybuilder.model

import com.yatatsu.fieldschema.annotations.FieldSchemaClass

@FieldSchemaClass
class RestaurantJoin : Restaurant()
{
    val cityName : String = ""
    val countryName : String = ""
}
