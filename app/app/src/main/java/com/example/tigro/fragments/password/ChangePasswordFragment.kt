package com.example.tigro.fragments.password

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.tigro.R
import com.example.tigro.util.account.PasswordManager


class ChangePasswordFragment : DialogFragment() {

    private lateinit var changePasswordOldPasswordEditText: EditText
    private lateinit var changePasswordNewPasswordEditText: EditText
    private lateinit var changePasswordConfirmPasswordEditText: EditText

    private lateinit var changePasswordCancelBtn: Button
    private lateinit var changePasswordApplyBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_change_password, container, false)

        changePasswordOldPasswordEditText = view.findViewById(R.id.changePasswordOldPasswordEditText)
        changePasswordNewPasswordEditText = view.findViewById(R.id.changePasswordNewPasswordEditText)
        changePasswordConfirmPasswordEditText = view.findViewById(R.id.changePasswordConfirmPasswordEditText)

        changePasswordCancelBtn = view.findViewById(R.id.changePasswordCancelBtn)
        changePasswordCancelBtn.setOnClickListener { changePasswordCancelBtnClicked() }

        changePasswordApplyBtn = view.findViewById(R.id.changePasswordApplyBtn)
        changePasswordApplyBtn.setOnClickListener { changePasswordApplyBtnClicked() }

        return view
    }

    private fun changePasswordApplyBtnClicked() {
        val oldPassword: Int = changePasswordOldPasswordEditText.text.toString().toInt()
        val newPassword: Int = changePasswordNewPasswordEditText.text.toString().toInt()
        val confirmPassword: Int = changePasswordConfirmPasswordEditText.text.toString().toInt()
        val pm = PasswordManager(requireContext())

        if (pm.verifyPin(oldPassword)) {
            if (newPassword == confirmPassword) {
                pm.setPin(newPassword)
                Toast.makeText(requireContext(), "PIN changed successfully", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Invalid PIN", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changePasswordCancelBtnClicked() {
        dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        println("change password popup dismissed")
    }


}