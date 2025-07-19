package io.github.mucute.qwq.kolomitm.definition

import io.github.mucute.qwq.kolomitm.util.BlockPaletteUtils
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition
import org.cloudburstmc.protocol.common.DefinitionRegistry


class NbtBlockDefinitionRegistry(definitions: MutableList<NbtMap>, hashed: Boolean) :
    DefinitionRegistry<BlockDefinition> {

    private val definitions: Int2ObjectMap<NbtBlockDefinition?> = Int2ObjectOpenHashMap<NbtBlockDefinition?>()

    init {
        var counter = 0
        for (definition in definitions) {
            val runtimeId = if (hashed) BlockPaletteUtils.createHash(definition) else counter++
            this.definitions.put(runtimeId, NbtBlockDefinition(runtimeId, definition))
        }
    }

    override fun getDefinition(runtimeId: Int): BlockDefinition? {
        return definitions.get(runtimeId)
    }

    override fun isRegistered(definition: BlockDefinition?): Boolean {
        return definitions.get(definition!!.getRuntimeId()) === definition
    }

    @JvmRecord
    data class NbtBlockDefinition(val runtimeId: Int, val tag: NbtMap?) : BlockDefinition {
        override fun getRuntimeId(): Int {
            return runtimeId
        }
    }

}