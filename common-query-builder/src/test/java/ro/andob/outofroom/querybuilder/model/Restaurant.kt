package ro.andob.outofroom.querybuilder.model

import com.yatatsu.fieldschema.annotations.FieldSchemaClass
import org.jetbrains.annotations.NotNull
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
@FieldSchemaClass
open class Restaurant
{
    @Id
    @Column
    @NotNull
    var id : Int = 0

    @Column
    var name : String = ""

    @Column
    var rating : Int = 0

    @Column
    var latitude : Double = 0.0

    @Column
    var longitude : Double = 0.0

    @Column
    var cityId : Int = 0

    constructor()

    constructor(id : Int, name : String, rating : Int, latitude : Double, longitude : Double)
    {
        this.id = id
        this.name = name
        this.rating = rating
        this.latitude = latitude
        this.longitude = longitude
    }
}