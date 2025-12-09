package com.example.tigro.util.account

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log


/**
 * PasswordManager allows users to set, verify, or destroy their PIN and nuclear PIN
 *
 * pin: used for login
 * nuclear pin: works for login as well, but deletes most user data. See AccountManager.kt for more
 * on use
 */
class PasswordManager(private val context: Context) {

    companion object {
        private const val PIN_KEY = "pin_key"
        private const val NUCLEAR_PIN_KEY = "nuclear_pin_key"
    }

    fun doesPinExist(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.contains(PIN_KEY)
    }

    fun setPin(pin: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putInt(PIN_KEY, pin).apply()
        Log.d("PIN", "PIN set to $pin")
    }

    fun verifyPin(enteredPin: Int): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val pin = sharedPreferences.getInt(PIN_KEY, -1)

        val isCorrect = pin == enteredPin
        Log.d("PIN", "Checking PIN... $isCorrect")
        return isCorrect
    }

    fun destroyPin() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().remove(PIN_KEY).apply()
        Log.d("PIN", "PIN destroyed")
    }

    fun setNuclearPin(pin: Int) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putInt(NUCLEAR_PIN_KEY, pin).apply()
        Log.d("PIN", "Nuclear PIN set to $pin")
    }

    fun verifyNuclearPin(enteredPin: Int): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val pin = sharedPreferences.getInt(NUCLEAR_PIN_KEY, -1)

        val isCorrect = pin == enteredPin
        Log.d("PIN", "Checking Nuclear PIN... $isCorrect")
        return isCorrect
    }

    fun destroyNuclearPin() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().remove(NUCLEAR_PIN_KEY).apply()
        Log.d("PIN", "Nuclear PIN destroyed")
    }
}