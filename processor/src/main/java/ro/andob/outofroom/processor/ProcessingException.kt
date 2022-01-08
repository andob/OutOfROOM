package ro.andob.outofroom.processor

import java.lang.RuntimeException
import javax.lang.model.element.Element

class ProcessingException
(
    message : String?,
    val element : Element
) : RuntimeException(message)
