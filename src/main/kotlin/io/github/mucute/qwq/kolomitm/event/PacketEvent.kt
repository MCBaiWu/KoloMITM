package io.github.mucute.qwq.kolomitm.event

import io.github.mucute.qwq.kolomitm.packet.PacketDirection
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket

open class PacketEvent<T : BedrockPacket>(
    val packet: T,
    val direction: PacketDirection
) : KoloEvent {

    internal var isConsumable = true

    var isConsumed = false
        private set

    fun consume() {
        if (isConsumable) {
            isConsumed = true
        }
    }

    override fun toString(): String {
        return "PacketEvent(packet=$packet, direction=$direction)"
    }

}