package io.github.mucute.qwq.kolomitm.util

class Trigger<T>(
    initialValue: T
) {

    private var value = initialValue

    private var previousValue = initialValue

    fun value(): T {
        return this.value
    }

    fun update(value: T) {
        this.value = value
    }

    fun consume() {
        this.previousValue = this.value
    }

    fun isTriggered(): Boolean {
        return this.previousValue != this.value
    }

}