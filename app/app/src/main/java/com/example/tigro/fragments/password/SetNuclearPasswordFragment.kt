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


class SetNuclearPasswordFragment : DialogFragment() {

    private lateinit var nuclearPasswordRegularPasswordEditText: EditText
    private lateinit var nuclearPasswordNewPasswordEditText: EditText
    private lateinit var nuclearPasswordConfirmPasswordEditText: EditText

    private lateinit var nuclearPasswordCancelBtn: Button
    private lateinit var nuclearPasswordApplyBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_set_nuclear_password, container, false)

        nuclearPasswordRegularPasswordEditText = view.findViewById(R.id.nuclearPasswordRegularPasswordEditText)
        nuclearPasswordNewPasswordEditText = view.findViewById(R.id.nuclearPasswordNewPasswordEditText)
        nuclearPasswordConfirmPasswordEditText = view.findViewById(R.id.nuclearPasswordConfirmPasswordEditText)

        nuclearPasswordCancelBtn = view.findViewById(R.id.nuclearPasswordCancelBtn)
        nuclearPasswordCancelBtn.setOnClickListener { nuclearPasswordCancelBtnClicked() }

        nuclearPasswordApplyBtn = view.findViewById(R.id.nuclearPasswordApplyBtn)
        nuclearPasswordApplyBtn.setOnClickListener { nuclearPasswordApplyBtnClicked() }
        return view
    }

    private fun nuclearPasswordCancelBtnClicked() {
        dismiss()
    }

    private fun nuclearPasswordApplyBtnClicked() {
        val regularPassword: Int = nuclearPasswordRegularPasswordEditText.text.toString().toInt()
        val nuclearPassword: Int = nuclearPasswordNewPasswordEditText.text.toString().toInt()
        val confirmPassword: Int = nuclearPasswordConfirmPasswordEditText.text.toString().toInt()
        val pm = PasswordManager(requireContext())

        if (pm.verifyPin(regularPassword)) {
            if (nuclearPassword == confirmPassword) {
                pm.setNuclearPin(confirmPassword)
                Toast.makeText(requireContext(), "Nuclear PIN set successfully", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                Toast.makeText(requireContext(), "PINs do not match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Invalid PIN", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        println("set nuclear password popup dismissed")
    }


}