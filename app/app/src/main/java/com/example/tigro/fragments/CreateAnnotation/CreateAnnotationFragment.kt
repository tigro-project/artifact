package com.example.tigro.fragments.CreateAnnotation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.example.tigro.R
import com.example.tigro.util.annotation.SetAnnotation.TigroAnnotationListener
import com.example.tigro.util.annotation.TigroAnnotation
import java.io.InputStream
import java.time.LocalDateTime
import java.time.Month


class CreateAnnotationFragment : Fragment() {

    private var annotationListener: TigroAnnotationListener? = null

    private lateinit var editTextAnnotationTitle: EditText
    private lateinit var editTextAnnotation: EditText
    private lateinit var chooseImageButton: Button
    private lateinit var setExpirationFragmentContainer: FrameLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TigroAnnotationListener) {
            annotationListener = context
        } else {
            throw IllegalStateException("The activity must implement TigroAnnotationListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_submit_annotation, container, false)

        editTextAnnotationTitle = view.findViewById(R.id.editTextAnnotationTitle)
        editTextAnnotation = view.findViewById(R.id.editTextAnnotation)
        chooseImageButton = view.findViewById(R.id.chooseImageButton)
        setExpirationFragmentContainer = view.findViewById(R.id.setExpirationFragmentContainer)

        chooseImageButton.setOnClickListener { chooseImageButtonClicked() }


        // Expiration Fragment
        childFragmentManager.beginTransaction()
            .replace(setExpirationFragmentContainer.id, SetAnnotationExpirationFragment())
            .commit()

        return view
    }

    /**
     * Waits for the view to get created and all lateinit fields to be initialized,
     * then fills them if someone requested a share via the Android ShareSheet
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle shared content
        if (activity?.intent?.action == Intent.ACTION_SEND && activity?.intent?.type == "text/plain") {
            val sharedSubject = activity?.intent?.getStringExtra(Intent.EXTRA_SUBJECT) ?: ""
            val sharedText = activity?.intent?.getStringExtra(Intent.EXTRA_TEXT) ?: ""

            editTextAnnotationTitle.setText(sharedSubject)
            editTextAnnotation.setText(sharedText)
            Log.d("Reg", "new annotation shared with title $sharedSubject and text $sharedText" )
        }
    }

    private fun generateAnnotation(): TigroAnnotation {
        val label = editTextAnnotationTitle.text.toString()
        val img = ByteArray(0)
        val content = editTextAnnotation.text.toString()

        // generates the annotation expiration, either 2 weeks from present (default) or custom
        val expiration = LocalDateTime.of(2024, Month.JULY, 8, 14, 0)

        return TigroAnnotation(label, img, content, expiration.toString())
    }

    // TODO: It would be nice if there was a cleaner way to send the annotation
    override fun onDetach() {
        super.onDetach()
        val tempAnnotation: TigroAnnotation = generateAnnotation()
        annotationListener?.setAnnotation(tempAnnotation)
        annotationListener = null
    }

    private fun chooseImageButtonClicked() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

}