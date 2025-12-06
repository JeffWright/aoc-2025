package dev.jtbw.aoc2025.meta

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES-256-GCM encryption utility for encrypting files at rest.
 *
 * Uses AES-256 in GCM mode for authenticated encryption with a 128-bit authentication tag. Each
 * encryption operation generates a random 12-byte IV (initialization vector) which is prepended to
 * the ciphertext.
 *
 * File format: [12-byte IV][ciphertext + auth tag]
 */
object AesCrypto {
  private const val ALGORITHM = "AES/GCM/NoPadding"
  private const val IV_SIZE = 12 // 96 bits recommended for GCM
  private const val TAG_SIZE = 128 // 128-bit authentication tag

  /**
   * Derives a 256-bit AES key from a passphrase using SHA-256.
   *
   * @param passphrase the passphrase to derive the key from
   * @return a SecretKeySpec suitable for AES-256
   */
  private fun deriveKey(passphrase: String): SecretKeySpec {
    val digest = MessageDigest.getInstance("SHA-256")
    val hash = digest.digest(passphrase.toByteArray(Charsets.UTF_8))
    return SecretKeySpec(hash, "AES")
  }

  /**
   * Encrypts plaintext using AES-256-GCM.
   *
   * @param plaintext the text to encrypt
   * @param passphrase the encryption passphrase
   * @return encrypted bytes with IV prepended: [IV][ciphertext]
   */
  fun encrypt(plaintext: String, passphrase: String): ByteArray {
    val key = deriveKey(passphrase)
    val cipher = Cipher.getInstance(ALGORITHM)

    // Generate random IV
    val iv = ByteArray(IV_SIZE)
    SecureRandom().nextBytes(iv)

    val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
    cipher.init(Cipher.ENCRYPT_MODE, key, gcmSpec)

    val ciphertext = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))

    // Prepend IV to ciphertext
    return iv + ciphertext
  }

  /**
   * Decrypts ciphertext using AES-256-GCM.
   *
   * @param encrypted the encrypted bytes (with IV prepended)
   * @param passphrase the decryption passphrase
   * @return the decrypted plaintext
   * @throws javax.crypto.AEADBadTagException if authentication fails or wrong key
   */
  fun decrypt(encrypted: ByteArray, passphrase: String): String {
    val key = deriveKey(passphrase)
    val cipher = Cipher.getInstance(ALGORITHM)

    // Extract IV from beginning
    val iv = encrypted.sliceArray(0 until IV_SIZE)
    val ciphertext = encrypted.sliceArray(IV_SIZE until encrypted.size)

    val gcmSpec = GCMParameterSpec(TAG_SIZE, iv)
    cipher.init(Cipher.DECRYPT_MODE, key, gcmSpec)

    val plaintext = cipher.doFinal(ciphertext)
    return String(plaintext, Charsets.UTF_8)
  }
}