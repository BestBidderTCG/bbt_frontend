
package com.gps_g6.bestbiddertcg.core.auth

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.AtomicFile
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class EncryptedTokenStorage(
    context: Context
) : TokenStorage {

    companion object {
        private const val KEY_ALIAS = "bestbidder_session_key"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val IV_LENGTH = 12
        private const val TAG_LENGTH = 128
    }

    private val file = AtomicFile(
        File(context.noBackupFilesDir, "session_tokens.enc")
    )

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore")
        keyStore.load(null)

        val existingKey = keyStore.getKey(
            KEY_ALIAS,
            null
        ) as? SecretKey

        if (existingKey != null) {
            return existingKey
        }

        val generator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or
                    KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(
                KeyProperties.ENCRYPTION_PADDING_NONE
            )
            .setKeySize(256)
            .build()

        generator.init(spec)

        return generator.generateKey()
    }

    @Synchronized
    override fun saveTokens(tokens: SessionTokens) {
        val json = JSONObject().apply {
            put("accessToken", tokens.accessToken)
            put("refreshToken", tokens.refreshToken)
        }.toString()

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(
            Cipher.ENCRYPT_MODE,
            getSecretKey()
        )

        val encrypted = cipher.doFinal(
            json.toByteArray(Charsets.UTF_8)
        )

        val bytes = ByteArrayOutputStream().use { buffer ->
            DataOutputStream(buffer).use { output ->
                output.write(cipher.iv)
                output.write(encrypted)
            }
            buffer.toByteArray()
        }

        val stream = file.startWrite()

        try {
            stream.write(bytes)
            file.finishWrite(stream)
        } catch (e: Exception) {
            file.failWrite(stream)
            throw e
        }
    }

    @Synchronized
    override fun getTokens(): SessionTokens? {
        if (!file.baseFile.exists()) return null

        return try {
            val bytes = file.readFully()

            require(bytes.size > IV_LENGTH + 16) {
                "Invalid encrypted token data"
            }

            val input = DataInputStream(
                ByteArrayInputStream(bytes)
            )

            val iv = ByteArray(IV_LENGTH)
            input.readFully(iv)

            val encrypted = ByteArray(
                bytes.size - IV_LENGTH
            )
            input.readFully(encrypted)

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getSecretKey(),
                GCMParameterSpec(TAG_LENGTH, iv)
            )

            val decrypted = cipher.doFinal(encrypted)
            val json = JSONObject(
                decrypted.toString(Charsets.UTF_8)
            )

            SessionTokens(
                accessToken = json.getString("accessToken"),
                refreshToken = json.getString("refreshToken")
            )
        } catch (e: Exception) {
            clearTokens()
            null
        }
    }

    @Synchronized
    override fun clearTokens() {
        file.delete()
    }
}
