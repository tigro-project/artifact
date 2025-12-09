package com.example.tigro.util.crypto

import com.example.tigro.AES_ALG
import com.example.tigro.ECDH_ALG
import com.example.tigro.EC_ALG
import com.example.tigro.util.crypto.keyset.PrivateKeySet
import com.example.tigro.util.crypto.keyset.PublicKeySet
import com.example.tigro.util.crypto.keyset.SecretKeySet
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.KeyAgreement
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class KeyManager {


    /**
     * One-time generate public and private values to be used with DH
     * @return Pair of Keysets, where first is public values and second is private values
     */
    fun generateEphemeralEcdhKeySets(): Pair<PublicKeySet, PrivateKeySet> {
        val kpg: KeyPairGenerator = KeyPairGenerator.getInstance(EC_ALG)
        kpg.initialize(256)

        val publicKeys: ArrayList<PublicKey> = ArrayList()
        val privateKeys: ArrayList<PrivateKey> = ArrayList()
        for (i in 0 until 2) {
            val kp: KeyPair = kpg.generateKeyPair()
            publicKeys.add(kp.public)
            privateKeys.add(kp.private)
        }
        val pubKeySet: PublicKeySet = PublicKeySet(publicKeys[0], publicKeys[1])
        val privKeySet: PrivateKeySet = PrivateKeySet(privateKeys[0], privateKeys[1])
        return Pair(pubKeySet, privKeySet)
    }

    fun generateAllSharedSecret(ourSecretKeyset: PrivateKeySet, otherPublicKeyset: PublicKeySet): SecretKeySet {

        val sharedToken = generateSharedSecret(ourSecretKeyset.tokenKey, otherPublicKeyset.tokenKey)
        val sharedEncrypt = generateSharedSecret(ourSecretKeyset.encryptKey, otherPublicKeyset.encryptKey)

        return SecretKeySet(sharedToken, sharedEncrypt)
    }

    /**
     * Generates a single shared secret as a ByteArray
     * @param otherPublicKey - other public key
     * @param ourSecretKey - our private key
     */
    private fun generateSharedSecret(ourSecretKey: PrivateKey, otherPublicKey: PublicKey): SecretKey {
        // Perform key agreement
        val ka = KeyAgreement.getInstance(ECDH_ALG)
        ka.init(ourSecretKey)
        ka.doPhase(otherPublicKey, true)

        // generate shared secret - not actually used for encryption because it has a noticeable structure
        var sharedSecretRaw = ka.generateSecret()

        // Derive a key from the shared secret and both public keys
        val hash = MessageDigest.getInstance("SHA-256")
        hash.update(sharedSecretRaw)

        return SecretKeySpec(hash.digest(), AES_ALG)
    }

}