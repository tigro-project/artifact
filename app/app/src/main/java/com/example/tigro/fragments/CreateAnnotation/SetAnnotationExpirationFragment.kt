package com.example.tigro.fragments.CreateAnnotation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import com.example.tigro.R
import com.example.tigro.util.annotation.SetAnnotation.TigroAnnotationListener
import java.time.LocalDateTime

class SetAnnotationExpirationFragment : Fragment() {

    private var annotationListener: TigroAnnotationListener? = null

    private lateinit var checkboxDefault: CheckBox  // default (2 weeks) checkbox
    private lateinit var checkboxAdditionalOption: CheckBox // custom time checkbox

    private lateinit var setAnnotationExpirationDuration: EditText  // duration
    private lateinit var spinnerTimeUnit: Spinner   // hours, days, weeks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TigroAnnotationListener) {
            annotationListener = context
        } else {
            throw IllegalStateException("The activity must implement TigroAnnotationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_set_annotation_expiration, container, false)

        val checkboxClickListener = View.OnClickListener { view ->
            if (view is CheckBox) {
                if (view == checkboxDefault) {
                    checkboxAdditionalOption.isChecked = false
                } else if (view == checkboxAdditionalOption) {
                    checkboxDefault.isChecked = false
                }
            }
        }

        checkboxDefault = view.findViewById(R.id.checkboxDefault)
        checkboxAdditionalOption = view.findViewById(R.id.checkboxAdditionalOption)
        checkboxDefault.setOnClickListener(checkboxClickListener)
        checkboxAdditionalOption.setOnClickListener(checkboxClickListener)

        setAnnotationExpirationDuration = view.findViewById(R.id.setAnnotationExpirationDuration)

        spinnerTimeUnit = view.findViewById(R.id.spinnerTimeUnit)
        return view
    }

    private fun generateAnnotationExpiration(): LocalDateTime {
        var newDateTime = LocalDateTime.now()

        if (checkboxDefault.isChecked) {
            newDateTime = newDateTime.plusWeeks(2)
        } else {
            val duration: Long = setAnnotationExpirationDuration.text.toString().toLong()
            // TODO: error checking with toInt()

            when (spinnerTimeUnit.selectedItem.toString()) {
                "Hours" -> newDateTime = newDateTime.plusHours(duration)
                "Days" -> newDateTime = newDateTime.plusDays(duration)
                "Weeks" -> newDateTime = newDateTime.plusWeeks(duration)
            }
        }
        return newDateTime
    }

    // TODO: It would be nice if there was a cleaner way to send the annotation
    override fun onDetach() {
        super.onDetach()
        val tempExpiration: LocalDateTime = generateAnnotationExpiration()
        annotationListener?.setAnnotationExpiration(tempExpiration)
        annotationListener = null
    }
}