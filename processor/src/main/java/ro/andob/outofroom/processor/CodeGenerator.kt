package ro.andob.outofroom.processor

import javax.annotation.processing.Filer
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.util.Elements

sealed class CodeGenerator
{
    abstract fun getSourceCode() : String
    abstract fun generate()

    class QueryArgumentConverterCodeGenerator
    (
        private val filer : Filer,
        private val elementUtils : Elements,
        private val outputClassName : String,
        private val insertDataExtensionMethods : List<ExecutableElement>,
        private val insertDataExtensionMethodsParentClass : Element,
    ) : CodeGenerator()
    {
        override fun getSourceCode() = """
package ${elementUtils.getPackageOf(insertDataExtensionMethodsParentClass)};

import java.util.HashMap;
import java.util.Map;
import ro.andob.outofroom.Column;
import ro.andob.outofroom.InsertData;
import ro.andob.outofroom.InsertDataExtensionFunction;
import ro.andob.outofroom.QueryArgumentConverter;
import ro.andob.outofroom.SQLType;

public class QueryArgumentConverterImpl implements QueryArgumentConverter
{
    private final Map<Class<?>, InsertDataExtensionFunction<Object>> extensionFunctions = new HashMap<>();

    @SuppressWarnings("unchecked")
    private <T> void addExtensionFunction(Class<T> type, InsertDataExtensionFunction<T> extensionFunction)
    {
        extensionFunctions.put(type, (InsertDataExtensionFunction<Object>)extensionFunction);
    }

    public QueryArgumentConverterImpl()
    {
${insertDataExtensionMethods.map { extensionMethod -> 
    extensionMethod.parameters[2].asType().toString().split("<").first().trim().let { targetType -> 
        "addExtensionFunction($targetType.class, ${insertDataExtensionMethodsParentClass}::${extensionMethod.simpleName});"
    }
}.joinToString(separator = "\n", transform = { "        ${it.trim()}" })}
    }

    @Override
    public Object convert(Object object)
    {
        if (object == null)
            return object;

        InsertDataExtensionFunction<Object> extensionFunction = extensionFunctions.get(object.getClass());
        if (extensionFunction != null)
        {
            InterceptingInsertData insertData = new InterceptingInsertData();
            extensionFunction.putObject(insertData, dummyColumn, object);
            return insertData.interceptedValue;
        }

        return object;
    }

    private final Column dummyColumn = new Column("dummy", SQLType.Null, false);

    private static final class InterceptingInsertData implements InsertData
    {
        public Object interceptedValue = null;

        @Override
        public boolean hasKey(Column column)
        {
            return false;
        }

        @Override
        public void putBoolean(Column column, Boolean value)
        {
            interceptedValue = value;
        }

        @Override
        public void putDouble(Column column, Double value)
        {
            interceptedValue = value;
        }

        @Override
        public void putFloat(Column column, Float value)
        {
            interceptedValue = value;
        }

        @Override
        public void putInt(Column column, Integer value)
        {
            interceptedValue = value;
        }

        @Override
        public void putLong(Column column, Long value)
        {
            interceptedValue = value;
        }

        @Override
        public void putString(Column column, String value)
        {
            interceptedValue = value;
        }
        
        @Override
        public void putBytes(Column column, byte[] value)
        {
            interceptedValue = value;
        }
    }
}
"""

        override fun generate()
        {
            val sourceFile = filer.createSourceFile(outputClassName)
            val sourceCode = getSourceCode()

            sourceFile.openWriter().use { it.write(sourceCode) }
        }
    }
}