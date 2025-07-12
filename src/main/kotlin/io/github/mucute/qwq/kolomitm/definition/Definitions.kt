package io.github.mucute.qwq.kolomitm.definition

import org.cloudburstmc.nbt.NbtMap
import org.cloudburstmc.nbt.NbtType
import org.cloudburstmc.nbt.NbtUtils
import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition
import org.cloudburstmc.protocol.bedrock.data.definitions.ItemDefinition
import org.cloudburstmc.protocol.common.DefinitionRegistry
import org.cloudburstmc.protocol.common.NamedDefinition
import org.cloudburstmc.protocol.common.SimpleDefinitionRegistry


object Definitions {

    val legacyIdMap: MutableMap<Int, String> = HashMap()

    var itemDefinitions: DefinitionRegistry<ItemDefinition> = SimpleDefinitionRegistry.builder<ItemDefinition>()
        .build()

    var blockDefinition: DefinitionRegistry<BlockDefinition> = UnknownBlockDefinitionRegistry()

    var blockDefinitionHashed: DefinitionRegistry<BlockDefinition> = blockDefinition

    var cameraDefinitions: DefinitionRegistry<NamedDefinition> = SimpleDefinitionRegistry.builder<NamedDefinition>()
        .build()

    fun loadBlockPalette() {
        Definitions::class.java.getResourceAsStream("block_palette")?.use {
            NbtUtils.createGZIPReader(it).use { nbtInputStream ->
                val tag = nbtInputStream.readTag()
                if (tag is NbtMap) {
                    blockDefinition = NbtBlockDefinitionRegistry(tag.getList("blocks", NbtType.COMPOUND), false)
                    blockDefinitionHashed = NbtBlockDefinitionRegistry(tag.getList("blocks", NbtType.COMPOUND), true)
                }
            }
        }
    }

}