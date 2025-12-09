package com.example.tigro.fragments.CreateAnnotation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.tigro.activities.MainActivity
import com.example.tigro.R
import com.example.tigro.util.overwrite.OverwriteListener

class LostAnnotationWarningFragment : DialogFragment() {

    private lateinit var cancelBtn: Button
    private lateinit var continueBtn: Button

    private var listener: OverwriteListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lost_annotation_warning, container, false)
        // Initialize and set up the buttons and text views

        cancelBtn = view.findViewById(R.id.cancelAnnotationFragBtn)
        continueBtn = view.findViewById(R.id.continueAnnotationFragBtn)
        cancelBtn.setOnClickListener { cancelBtnClicked() }
        continueBtn.setOnClickListener { continueBtnClicked() }


        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OverwriteListener) {
            listener = context
        } else {
            throw IllegalArgumentException("Hosting activity must implement OverwriteListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // Cancel button dismisses popup
    private fun cancelBtnClicked() {
        dismiss()
    }

    // Continue button dismisses popup and returns to main screen
    // Overwrites user input data
    private fun continueBtnClicked() {
        listener?.onRequestOverwrite()
        startActivity(Intent(requireContext(), MainActivity::class.java))
        dismiss()
    }
}