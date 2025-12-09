package com.example.tigro.util.crypto

import android.security.keystore.KeyProperties
import android.security.keystore.KeyProtection
import com.example.tigro.AES_ALG
import com.example.tigro.HMAC_ALG
import com.example.tigro.util.crypto.keyset.SecretKeySet
import java.security.KeyStore
import java.security.KeyStore.ProtectionParameter
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


class KeyLoader {

    private val keyStore: KeyStore
    init {
        keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)
    }
    fun loadKeySet(alias: String, keySet: SecretKeySet) {
        // Note: see other approaches Swetabh found here https://github.com/tigro-project/tigro-app/commit/bab5e3ed0203329b3c0ff978cbb030a401eab013
        // When creating the new contact, store the shared key in the Android KeyStore with
        // the specified alias. Store this alias with the contact.
        loadKey("$alias-T", keySet.tokenKey, HMAC_ALG)
        loadKey("$alias-E", keySet.encryptKey, AES_ALG)
    }

    /**
     * loads a key into the keystore
     * @param fullAlias - the alias that specific key will have. for example, if my contact alias is 'alice', the token key would be 'alice-t'
     * @param key - key to load in
     * @param algorithm - for encryption keys, use AES_ALG. For hmac keys, use HMAC_ALG
     */
    private fun loadKey(fullAlias: String, key: SecretKey, algorithm: String) {
        val keyEntry = KeyStore.SecretKeyEntry(SecretKeySpec(key.encoded, algorithm))
        val prot: ProtectionParameter = KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT or
            KeyProperties.PURPOSE_DECRYPT or KeyProperties.PURPOSE_SIGN)
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(false)
            .build();
        keyStore.setEntry(fullAlias, keyEntry, prot)
    }

    fun getKeySet(alias: String): SecretKeySet {
//        return FakeKeyLoader.getFakeKeySet(alias)
        val t: SecretKey? = keyStore.getKey("$alias-T", null) as? SecretKey
        val e: SecretKey? = keyStore.getKey("$alias-E", null) as? SecretKey
        if (t == null || e == null) {
            throw Exception("Failed to get keys from the KeyStore. Bad Alias?")
        }
        return SecretKeySet(t, e)
    }

}