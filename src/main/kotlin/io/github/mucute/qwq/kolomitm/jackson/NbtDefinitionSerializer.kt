package io.github.mucute.qwq.kolomitm.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.github.mucute.qwq.kolomitm.definition.NbtBlockDefinitionRegistry.NbtBlockDefinition
import org.cloudburstmc.nbt.NBTOutputStream
import org.cloudburstmc.nbt.util.stream.LittleEndianDataOutputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*


class NbtDefinitionSerializer : StdSerializer<NbtBlockDefinition>(NbtBlockDefinition::class.java) {

    @Throws(IOException::class)
    override fun serialize(value: NbtBlockDefinition, gen: JsonGenerator, provider: SerializerProvider?) {
        gen.writeStartObject()
        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                NBTOutputStream(LittleEndianDataOutputStream(byteArrayOutputStream)).use { stream ->
                    stream.writeTag(value.tag)
                    gen.writeObjectField(
                        "block_state_b64",
                        Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
                    )
                }
            }
        } catch (e: IOException) {
            System.err.println("Failed to serialize NBT block definition: ${e.stackTraceToString()}")
            gen.writeNull()
        } finally {
            gen.writeEndObject()
        }
    }

}