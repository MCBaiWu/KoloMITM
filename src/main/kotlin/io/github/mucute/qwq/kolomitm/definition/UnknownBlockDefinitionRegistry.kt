package io.github.mucute.qwq.kolomitm.definition

import org.cloudburstmc.protocol.bedrock.data.definitions.BlockDefinition
import org.cloudburstmc.protocol.common.DefinitionRegistry

class UnknownBlockDefinitionRegistry : DefinitionRegistry<BlockDefinition> {

    override fun getDefinition(runtimeId: Int): BlockDefinition {
        return UnknownDefinition(runtimeId)
    }

    override fun isRegistered(blockDefinition: BlockDefinition): Boolean {
        return true
    }

    @JvmRecord
    internal data class UnknownDefinition(val runtimeId: Int) : BlockDefinition {
        override fun getRuntimeId(): Int {
            return runtimeId
        }
    }
}
