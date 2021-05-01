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
class City
{
    @Id
    @Column
    @NotNull
    val id : Int = 0

    @Column
    val name : String = ""

    @Column
    val countryId : Int = 0
}
