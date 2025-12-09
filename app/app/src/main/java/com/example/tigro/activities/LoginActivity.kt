package com.example.tigro.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.tigro.BuildConfig
import com.example.tigro.R
import com.example.tigro.data.Contact
import com.example.tigro.database.AppDatabase
import com.example.tigro.database.AppDatabaseSingleton
import com.example.tigro.util.account.AccountManager
import com.example.tigro.util.account.PasswordManager
import com.example.tigro.util.crypto.KeyLoader
import com.example.tigro.util.crypto.keyset.SecretKeySet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey


// TODO: IMPORTANT: pin is currently plaintext and unsafe
// TODO: protect against brute force attacks with some timeout - sharedPreferences?
// TODO: make pin screen for new user

/**
 * Comes from: startup (production only)
 * Leads to: MainActivity
 *
 * logs in users, either with pin or nuclear pin
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var loginSetPinEditText: EditText
    private lateinit var loginEditText: EditText

    private val pm: PasswordManager = PasswordManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginSetPinEditText = findViewById(R.id.loginSetPinEditText)
        loginEditText = findViewById(R.id.loginEditText)

        val hasPin: Boolean = checkPinSetup()
        loginEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Perform the submit action/check action
                if (hasPin) {
                    submitPin()
                } else {
                    setNewPin()
                }
                true
            } else {
                false
            }
        }
    }

    private fun checkPinSetup(): Boolean {
        if (pm.doesPinExist()) {
            loginSetPinEditText.visibility = View.GONE
            loginEditText.hint = "Enter PIN"
            return true
        } else {
            loginSetPinEditText.visibility = View.VISIBLE
            loginEditText.hint = "Confirm PIN"
            return false
        }
    }

    /**
     * Sets PIN for an account that has just been created
     */
    private fun setNewPin() {
        val enteredPin: Int = loginSetPinEditText.text.toString().toInt()
        val confirmPin: Int = loginEditText.text.toString().toInt()

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(loginEditText.windowToken, 0)

        if (enteredPin == confirmPin) {
            pm.setPin(enteredPin)
            login()

            if (BuildConfig.FLAVOR == "demo") {
                loadDemoAccounts()
            }
        } else {
            Toast.makeText(this, "PINs do not match", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadDemoAccounts() {
        val contactA = Contact("Bob (decoy)", "This is bob", "bob", 0)
        val contactB = Contact("Alice (decoy)", "this is alice", "alice", 1)
        val contactC = Contact("Eve (decoy)", "this is eve", "eve", 2)
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(128)
        val k1 = keyGenerator.generateKey()
        val fakeKeySet = SecretKeySet(k1, k1)
        KeyLoader().loadKeySet("bob", fakeKeySet)
        KeyLoader().loadKeySet("alice", fakeKeySet)
        KeyLoader().loadKeySet("eve", fakeKeySet)

        val db: AppDatabase = AppDatabaseSingleton.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            db.contactDao().insertContact(contactA)
            db.contactDao().insertContact(contactB)
            db.contactDao().insertContact(contactC)
        }
    }

    private fun submitPin() {
        // hides keyboard
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(loginEditText.windowToken, 0)

        val enteredPin: Int = loginEditText.text.toString().toInt()
        val isNormalPin: Boolean = pm.verifyPin(enteredPin)
        val isNuclearPin: Boolean = pm.verifyNuclearPin(enteredPin)

        if (isNormalPin || isNuclearPin) {
            if (isNuclearPin) {
                // wipe database
                AccountManager(this).wipeAccountData()
                Log.d("SECURITY", "nuclear pin entered, wiping DB")
            }
            login()
        } else {
            Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show()
        }
    }


    private fun login() {
        Toast.makeText(this, "Successful Login", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}