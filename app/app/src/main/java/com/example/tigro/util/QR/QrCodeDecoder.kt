package com.example.tigro.util.QR

import com.example.tigro.util.crypto.KeyManager
import com.example.tigro.util.crypto.keyset.PublicKeySet
import com.example.tigro.util.crypto.keyset.PrivateKeySet
import com.example.tigro.util.crypto.keyset.SecretKeySet

/**
 * Takes in raw QR input and parses it into usable cryptographic data
 */
class QrCodeDecoder() {


    /**
     *
     * Decodes raw QR string and generates sharedSecret with our Private Keys (in KeyManager) and
     * the other Public Values passed in from the QR code
     * @param ourPrivateKeySet - private value
     * @param toDecode - string payload to decode, other party's public values
     * @return a KeySet with shared values for token, encrypt
     */
    fun decode(ourPrivateKeySet: PrivateKeySet, toDecode: String): SecretKeySet {

        // Note: keeping this class (instead of moving everything to activity) in case extra
        // deserialization/verification needs to happen here. For example, could add a QR wrapper
        // with metadata that gets serialized as well
        if (false) {
            // TODO: need to handle QR issues, maybe with deserialize?
            throw Exception("QrCodeDecoder: issue reading QR")
        }

        val otherKeySet: PublicKeySet = PublicKeySet.deserialize(toDecode);
        return KeyManager().generateAllSharedSecret(ourPrivateKeySet, otherKeySet);
    }
}