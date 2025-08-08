package io.github.mucute.qwq.kolomitm.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.cloudburstmc.protocol.common.util.Color
import java.io.IOException


class ColorSerializer : StdSerializer<Color?>(Color::class.java) {

    @Throws(IOException::class)
    override fun serialize(color: Color?, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider?) {
        if (color == null) {
            jsonGenerator.writeNull()
        } else {
            jsonGenerator.writeStartObject()
            jsonGenerator.writeNumberField("a", color.alpha)
            jsonGenerator.writeNumberField("r", color.red)
            jsonGenerator.writeNumberField("g", color.green)
            jsonGenerator.writeNumberField("b", color.blue)
            jsonGenerator.writeEndObject()
        }
    }

}