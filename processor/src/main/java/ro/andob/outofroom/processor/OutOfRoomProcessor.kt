package ro.andob.outofroom.processor

import ro.andob.outofroom.Column
import ro.andob.outofroom.InsertData
import ro.andob.outofroom.QueryArgumentConverter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

class OutOfRoomProcessor : AbstractProcessor()
{
    private lateinit var elementUtils : Elements
    private lateinit var typeUtils : Types
    private lateinit var filer : Filer
    private lateinit var messager : Messager

    @Synchronized
    override fun init(processingEnvironment : ProcessingEnvironment)
    {
        super.init(processingEnvironment)

        this.elementUtils=processingEnvironment.elementUtils
        this.typeUtils=processingEnvironment.typeUtils
        this.filer=processingEnvironment.filer
        this.messager=processingEnvironment.messager
    }

    override fun getSupportedAnnotationTypes() = setOf(QueryArgumentConverter.Generator::class.java.canonicalName)
    override fun getSupportedSourceVersion() = SourceVersion.latest()!!

    override fun process(set : Set<TypeElement>, roundEnvironment : RoundEnvironment) : Boolean
    {
        try
        {
            val codeGenerators=mutableListOf<CodeGenerator>()

            for (annotatedElement in roundEnvironment.getElementsAnnotatedWith(QueryArgumentConverter.Generator::class.java))
            {
                if (annotatedElement.kind!=ElementKind.CLASS)
                    throw ProcessingException("Only classes can be annotated with ${QueryArgumentConverter.Generator::class.java}", annotatedElement);

                val insertDataExtensionMethodsParentElement=elementUtils.getElementFromKClass(typeUtils, annotatedElement.getAnnotation(QueryArgumentConverter.Generator::class.java)::insertDataExtensionMethodsParentClass)?:annotatedElement
                val implementationClassName="${elementUtils.getPackageOf(insertDataExtensionMethodsParentElement)}.${QueryArgumentConverter::class.java.simpleName}Impl"
                val insertDataExtensionMethods=mutableListOf<ExecutableElement>()

                for (enclosedElement in insertDataExtensionMethodsParentElement.enclosedElements)
                {
                    if (enclosedElement.kind==ElementKind.METHOD)
                    {
                        val isAbstract=enclosedElement.modifiers.any { modifier -> modifier==Modifier.ABSTRACT }
                        val isPublic=enclosedElement.modifiers.any { modifier -> modifier==Modifier.PUBLIC }
                        val parameters=(enclosedElement as ExecutableElement).parameters
                        if (!isAbstract&&isPublic&&parameters.size==3)
                        {
                            if (parameters[0].asType().toString()==InsertData::class.java.name
                                &&parameters[1].asType().toString()==Column::class.java.name
                                &&!parameters[2].asType().toString().startsWith("java.")
                                &&!parameters[2].asType().toString().startsWith("javax.")
                                &&!parameters[2].asType().toString().startsWith("android.")
                                &&!parameters[2].asType().toString().startsWith("androidx."))
                                insertDataExtensionMethods.add(enclosedElement)
                        }
                    }
                }

                codeGenerators.add(CodeGenerator.QueryArgumentConverterCodeGenerator(
                    filer = filer, elementUtils = elementUtils,
                    outputClassName = implementationClassName,
                    insertDataExtensionMethods = insertDataExtensionMethods,
                    insertDataExtensionMethodsParentClass = insertDataExtensionMethodsParentElement))
            }

            codeGenerators.forEach { it.generate() }
        }
        catch (ex : ProcessingException)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.message, ex.element)
        }
        catch (ex : Throwable)
        {
            messager.printMessage(Diagnostic.Kind.ERROR, ex.message)
        }

        return true
    }
}