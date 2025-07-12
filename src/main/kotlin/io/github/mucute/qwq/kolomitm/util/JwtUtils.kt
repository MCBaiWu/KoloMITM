package io.github.mucute.qwq.kolomitm.util

import org.jose4j.jws.JsonWebSignature
import java.security.PublicKey

fun verifyJwt(jwt: String, key: PublicKey): Boolean {
    val jws = JsonWebSignature()
    jws.key = key
    jws.compactSerialization = jwt

    return jws.verifySignature()
}