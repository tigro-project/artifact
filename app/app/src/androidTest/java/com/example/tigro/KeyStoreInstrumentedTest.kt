package com.example.tigro


import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.tigro.util.crypto.EdxDriver
import com.example.tigro.util.crypto.KeyLoader
import com.example.tigro.util.crypto.KeyManager
import com.example.tigro.util.crypto.keyset.SecretKeySet
import com.google.protobuf.ByteString
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class KeyStoreInstrumentedTest {

    private val loader = KeyLoader()
    private val shared1: SecretKeySet = makeSameSharedSecret()

    @Before
    fun setup() {
        loader.loadKeySet("set-1", shared1)
        loader.loadKeySet("set-2", shared1)  // toggle if desired
        loader.loadKeySet("set-3", shared1)  // toggle if desired
        loader.loadKeySet("set-4", shared1)
    }

    @Test
    fun testLoadAndGetBasic() {
        loader.loadKeySet("set-load-get-basic", shared1)
        val out = loader.getKeySet("set-load-get-basic")
        Assert.assertTrue(testKeySetEqual(shared1, out))
    }

    @Test
    fun testGetOnly() {
        val out = loader.getKeySet("set-1")
        Assert.assertTrue(testKeySetEqual(shared1, out))

        val outOptional = loader.getKeySet("set-2")
        Assert.assertTrue(testKeySetEqual(shared1, outOptional))
    }

    @Test
    fun testGetNoKeyExists() {
        Assert.assertThrows(Exception::class.java) {
            val out = loader.getKeySet("set-NOKEY")
        }
    }

    @Test
    fun testHashWithKeyLoaded() {
        val payload: ByteString = ByteString.copyFromUtf8("payload")
        val out = loader.getKeySet("set-4")
        val hashed = EdxDriver().pibaseToken(out.tokenKey, payload)
    }

    /**
     * Helper to generate the same SecretKeySet over different tests
     */
    private fun makeSameSharedSecret(): SecretKeySet {
//        val kp1 = KeyManager().generateEphemeralEcdhKeySets()
//        val kp2 = KeyManager().generateEphemeralEcdhKeySets()
//        val shared1: SecretKeySet = KeyManager().generateAllSharedSecret(kp1.second, kp2.first)
//        println(shared1.serialize())
        val key = "{\"poidKeyBytes\":[-96,-92,93,123,66,37,-37,-51,-121,45,-63,3,-99,-56,88,-18,32,69,-97,46,-69,29,62,-60,-78,75,77,51,64,47,-85,65],\"tokenKeyBytes\":[-63,-41,-112,-67,10,-92,-35,-29,8,-9,-55,18,63,-8,19,84,-41,2,51,-126,-36,-92,97,-51,-123,49,-10,6,46,-72,-37,-93],\"encryptKeyBytes\":[108,83,-114,18,-48,-108,-95,39,-116,-46,-52,104,-100,-5,35,60,108,64,-82,123,-69,-3,119,-32,-14,-107,-10,-107,-105,20,10,72]}"
        return SecretKeySet.deserialize(key)
    }

    /**
     * Helper to test if two KeySets are equal based on their ability to encrypt and decrypt together
     * We cannot test equality explicitly because android does not give access to the raw bytes returned
     * by the KeyStore
     */
    private fun testKeySetEqual(k1: SecretKeySet, k2: SecretKeySet): Boolean {
        val payload: ByteString = ByteString.copyFromUtf8("payload")
        val out = EdxDriver().skeEncrypt(k1.encryptKey, payload)
        val decrypted = EdxDriver().skeDecrypt(k2.encryptKey, out)
        return (payload == decrypted)
    }

}