/* Licensed under AGPLv3 2024 - 2026 */
package de.flavormate.core.encryption.services

import de.flavormate.configuration.properties.FlavorMateProperties
import jakarta.enterprise.context.ApplicationScoped
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@ApplicationScoped
class EncryptionService(flavorMateProperties: FlavorMateProperties) {
  companion object {
    const val ENCRYPTION_ALGO: String = "AES/CBC/PKCS5Padding"
    const val IV_SIZE: Int = 16
  }

  private var key: SecretKeySpec

  init {
    val decodedKey = Base64.getDecoder().decode(flavorMateProperties.database().encryptionKey())
    this.key = SecretKeySpec(decodedKey, "AES")
  }

  fun encrypt(plaintext: String): String {
    try {
      val cipher = Cipher.getInstance(ENCRYPTION_ALGO)
      val iv = ByteArray(IV_SIZE)
      val random = SecureRandom()
      random.nextBytes(iv)

      val spec = IvParameterSpec(iv)
      cipher.init(Cipher.ENCRYPT_MODE, key, spec)
      val ciphertext = cipher.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))

      val result = ByteArray(iv.size + ciphertext.size)
      System.arraycopy(iv, 0, result, 0, iv.size)
      System.arraycopy(ciphertext, 0, result, iv.size, ciphertext.size)

      return Base64.getEncoder().encodeToString(result)
    } catch (e: Exception) {
      throw IllegalStateException("Encryption failed", e)
    }
  }

  fun decrypt(base64Encrypted: String?): String {
    try {
      val data: ByteArray = Base64.getDecoder().decode(base64Encrypted)
      val iv = data.copyOfRange(0, IV_SIZE)
      val ciphertext = data.copyOfRange(IV_SIZE, data.size)

      val cipher = Cipher.getInstance(ENCRYPTION_ALGO)
      val spec = IvParameterSpec(iv)
      cipher.init(Cipher.DECRYPT_MODE, key, spec)
      val plaintext = cipher.doFinal(ciphertext)

      return String(plaintext, StandardCharsets.UTF_8)
    } catch (e: Exception) {
      throw IllegalStateException("Decryption failed", e)
    }
  }
}
