package io.github.mucute.qwq.kolomitm.event.handler

import io.github.mucute.qwq.kolomitm.event.KoloEvent
import io.github.mucute.qwq.kolomitm.event.PacketEvent
import io.github.mucute.qwq.kolomitm.session.EventReceiver
import io.github.mucute.qwq.kolomitm.session.EventUnregister
import io.github.mucute.qwq.kolomitm.session.KoloSession
import io.github.mucute.qwq.kolomitm.util.event
import io.github.mucute.qwq.kolomitm.util.on
import io.github.mucute.qwq.kolomitm.util.packet
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket

interface EventHandler {

    val koloSession: KoloSession

    fun release() {}

}

fun EventHandler.publishEvent(event: KoloEvent): Boolean {
    return koloSession.publishEvent(event)
}

fun EventHandler.on(eventReceiver: EventReceiver): EventUnregister {
    return koloSession.on(eventReceiver)
}

inline fun <reified T : BedrockPacket> EventHandler.packet(
    crossinline eventReceiver: KoloSession.(PacketEvent<T>, EventUnregister) -> Unit
): EventUnregister {
    return koloSession.packet(eventReceiver)
}

inline fun <reified T : KoloEvent> EventHandler.event(
    crossinline eventReceiver: KoloSession.(T, EventUnregister) -> Unit
): EventUnregister {
    return koloSession.event(eventReceiver)
}