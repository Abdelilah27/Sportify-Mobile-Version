package com.app.sportify.utils

import java.security.MessageDigest

object FunUtil {
    fun String.toSHA256Hash(): String {
        val bytes = this.toString().toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}