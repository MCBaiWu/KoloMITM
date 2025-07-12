package io.github.mucute.qwq.kolomitm.event

class DisconnectEvent(val reason: String) : KoloEvent {

    override fun toString(): String {
        return "DisconnectEvent(reason=$reason)"
    }

}