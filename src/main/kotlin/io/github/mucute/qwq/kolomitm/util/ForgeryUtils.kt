package io.github.mucute.qwq.kolomitm.util

import net.raphimc.minecraftauth.step.bedrock.StepMCChain.MCChain
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession.FullBedrockSession
import org.jose4j.json.internal.json_simple.JSONObject
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.consumer.InvalidJwtException
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.jwx.HeaderParameterNames
import org.jose4j.lang.JoseException
import java.net.InetSocketAddress
import java.security.KeyFactory
import java.security.KeyPair
import java.security.interfaces.ECPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

object ForgeryUtils {

    private const val MOJANG_PUBLIC_KEY =
        "MHYwEAYHKoZIzj0CAQYFK4EEACIDYgAECRXueJeTDqNRRgJi/vlRufByu/2G0i2Ebt6YMar5QX/R0DIIyrJMcUpruK4QveTfJSTp3Shlq4Gk34cD/4GUWwkv0DVuzeuB+tXija7HBxii03NHDbPAD0AKnLr2wdAp"

    fun forgeOfflineAuthData(pair: KeyPair, extraDataJSONObject: JSONObject?): String {
        val publicKeyBase64 = Base64.getEncoder().encodeToString(pair.public.encoded)

        val timestamp = System.currentTimeMillis()
        val nbf = Date(timestamp - TimeUnit.SECONDS.toMillis(1))
        val exp = Date(timestamp + TimeUnit.DAYS.toMillis(1))

        val claimsSet = JwtClaims()
        claimsSet.notBefore = NumericDate.fromMilliseconds(nbf.time)
        claimsSet.expirationTime = NumericDate.fromMilliseconds(exp.time)
        claimsSet.issuedAt = NumericDate.fromMilliseconds(exp.time)
        claimsSet.issuer = "self"
        claimsSet.setClaim("certificateAuthority", true)
        claimsSet.setClaim("extraData", extraDataJSONObject)
        claimsSet.setClaim("identityPublicKey", publicKeyBase64)

        val jws = JsonWebSignature()
        jws.payload = claimsSet.toJson()
        jws.key = pair.private
        jws.algorithmHeaderValue = "ES384"
        jws.setHeader(HeaderParameterNames.X509_URL, publicKeyBase64)

        try {
            return jws.compactSerialization
        } catch (e: JoseException) {
            throw RuntimeException(e)
        }
    }

    @Throws(InvalidJwtException::class, JoseException::class)
    fun forgeOnlineAuthData(mcChain: MCChain, mojangPublicKey: ECPublicKey?): List<String> {
        val publicBase64Key = Base64.getEncoder().encodeToString(mcChain.publicKey.encoded)

        // adapted from https://github.com/RaphiMC/ViaBedrock/blob/a771149fe4492e4f1393cad66758313067840fcc/src/main/java/net/raphimc/viabedrock/protocol/packets/LoginPackets.java#L276-L291
        val consumer = JwtConsumerBuilder()
            .setAllowedClockSkewInSeconds(60)
            .setVerificationKey(mojangPublicKey)
            .build()

        val mojangJws = consumer.process(mcChain.mojangJwt).joseObjects[0] as JsonWebSignature

        val claimsSet = JwtClaims()
        claimsSet.setClaim("certificateAuthority", true)
        claimsSet.setClaim("identityPublicKey", mojangJws.getHeader("x5u"))
        claimsSet.setExpirationTimeMinutesInTheFuture((2 * 24 * 60).toFloat()) // 2 days
        claimsSet.setNotBeforeMinutesInThePast(1f)

        val selfSignedJws = JsonWebSignature()
        selfSignedJws.payload = claimsSet.toJson()
        selfSignedJws.key = mcChain.privateKey
        selfSignedJws.algorithmHeaderValue = "ES384"
        selfSignedJws.setHeader(HeaderParameterNames.X509_URL, publicBase64Key)

        val selfSignedJwt = selfSignedJws.compactSerialization

        return ArrayList(listOf(selfSignedJwt, mcChain.mojangJwt, mcChain.identityJwt))
    }

    fun forgeOfflineSkinData(pair: KeyPair, skinDataJSONObject: JSONObject): String {
        val publicKeyBase64 = Base64.getEncoder().encodeToString(pair.public.encoded)

        val jws = JsonWebSignature()
        jws.algorithmHeaderValue = "ES384"
        jws.setHeader(HeaderParameterNames.X509_URL, publicKeyBase64)
        jws.payload = skinDataJSONObject.toJSONString()
        jws.key = pair.private

        try {
            return jws.compactSerialization
        } catch (e: JoseException) {
            throw RuntimeException(e)
        }
    }

    fun forgeOnlineSkinData(
        bedrockSession: FullBedrockSession,
        skinDataJSONObject: JSONObject,
        serverAddress: InetSocketAddress
    ): String {
        val publicKeyBase64 = Base64.getEncoder().encodeToString(bedrockSession.mcChain.publicKey.encoded)

        val overrideData = HashMap<String, Any>()
        overrideData["PlayFabId"] = bedrockSession.playFabToken.playFabId.lowercase()
        overrideData["DeviceId"] = bedrockSession.mcChain.xblXsts.initialXblSession.xblDeviceToken.deviceId
        overrideData["DeviceOS"] = 1
        overrideData["ThirdPartyName"] = bedrockSession.mcChain.displayName
        overrideData["ServerAddress"] = serverAddress.hostString + ":" + serverAddress.port

        skinDataJSONObject.putAll(overrideData)

        val jws = JsonWebSignature()
        jws.algorithmHeaderValue = "ES384"
        jws.setHeader(HeaderParameterNames.X509_URL, publicKeyBase64)
        jws.payload = skinDataJSONObject.toJSONString()
        jws.key = bedrockSession.mcChain.privateKey

        try {
            return jws.compactSerialization
        } catch (e: JoseException) {
            throw RuntimeException(e)
        }
    }

    fun forgeMojangPublicKey(): ECPublicKey {
        try {
            return KeyFactory.getInstance("EC").generatePublic(
                X509EncodedKeySpec(
                    Base64.getDecoder().decode(
                        MOJANG_PUBLIC_KEY
                    )
                )
            ) as ECPublicKey
        } catch (e: Throwable) {
            throw RuntimeException("Could not initialize the required cryptography for online login", e)
        }
    }

}
