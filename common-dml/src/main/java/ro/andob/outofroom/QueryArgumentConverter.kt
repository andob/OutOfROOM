package ro.andob.outofroom

import kotlin.reflect.KClass

fun interface QueryArgumentConverter
{
    fun convert(`object` : Any?) : Any?

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Generator
    (
        val insertDataExtensionMethodsParentClass : KClass<*> = Void::class
    )
}