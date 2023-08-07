package ro.andob.outofroom.processor

import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.KClass

fun Elements.getElementFromKClass(typeUtils : Types, lambda : () -> KClass<*>) =
    getElementFromClass(typeUtils) { lambda.invoke().java }

fun Elements.getElementFromClass(typeUtils : Types, lambda : () -> Class<*>) : Element?
{
    try
    {
        getTypeElement(lambda.invoke().canonicalName)
        return null
    }
    catch (ex : MirroredTypeException)
    {
        val element = typeUtils.asElement(ex.typeMirror!!)!!
        if (element.toString().startsWith("java.") || 
            element.toString().startsWith("javax.") || 
            element.toString().startsWith("android.") || 
            element.toString().startsWith("androidx."))
            return null
        return element
    }
}
