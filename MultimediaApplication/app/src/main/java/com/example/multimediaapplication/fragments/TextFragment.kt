package com.example.multimediaapplication.fragments

import android.app.AlertDialog
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.multimediaapplication.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class TextFragment: Fragment()
{
    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private var filePath: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        val view = inflater.inflate(R.layout.text_fragment, container, false)

        val editTextSize = view.findViewById<EditText>(R.id.setTextSize)
        val buttonApplySize = view.findViewById<Button>(R.id.buttonApplyTextSize)
        val createText = view.findViewById<EditText>(R.id.createText)
        val buttonBold = view.findViewById<ImageView>(R.id.buttonBold)
        val buttonItalic = view.findViewById<ImageView>(R.id.buttonItalic)
        val buttonSave = view.findViewById<Button>(R.id.buttonSave)
        val showText = view.findViewById<TextView>(R.id.showText)
        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        editText = createText
        textView = showText

        buttonApplySize.setOnClickListener {
            val size = editTextSize.text.toString()
            if (size.isNotEmpty()) {
                val textSizeInSp = size.toFloat()
                createText.textSize = textSizeInSp
            }
        }

        buttonBold.setOnClickListener {
            if(createText.typeface != null && createText.typeface.isBold)
            {
                createText.setTypeface(null, Typeface.NORMAL)
            }
            else
            {
                createText.setTypeface(null, Typeface.BOLD)
            }
        }

        buttonItalic.setOnClickListener {
            if(createText.typeface != null && createText.typeface.isItalic)
            {
                createText.setTypeface(null, Typeface.NORMAL)
            }
            else
            {
                createText.setTypeface(null, Typeface.ITALIC)
            }
        }

        buttonSave.setOnClickListener(){

            if(filePath==null)
            {
                showSaveFileDialog()
            }
            else {
                saveFileToPath(filePath!!)
            }
        }

        buttonCancel.setOnClickListener(){

            activity?.finish()
        }

        return view
    }

    private fun showSaveFileDialog() {
        val dialogTextFile = AlertDialog.Builder(requireContext())
        dialogTextFile.setTitle("Indicar el nombre del archivo de texto")
        val input = EditText(requireContext())
        dialogTextFile.setView(input)

        dialogTextFile.setPositiveButton("Guardar") { _, _ ->
            val fileName = input.text.toString()
            if (fileName.isNotEmpty()) {
                saveFileToDownloads(fileName)
            } else {
                Toast.makeText(requireContext(), "Debe ingresar un nombre de archivo", Toast.LENGTH_SHORT).show()
            }
        }
        dialogTextFile.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        dialogTextFile.show()
    }
    private fun saveFileToDownloads(fileName: String) {
        val textToSave = editText.text.toString()

        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, "$fileName.txt")

        try {
            FileOutputStream(file, false).use { outputStream ->
                outputStream.write(textToSave.toByteArray())
            }
            Toast.makeText(requireContext(), "Archivo guardado correctamente", Toast.LENGTH_SHORT).show()
            activity?.finish()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al guardar el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveFileToPath(filePath: String) {
        val textToSave = editText.text.toString()

        val uri = Uri.parse(filePath)

        try {

            context?.contentResolver?.openOutputStream(uri).use { outputStream ->

                outputStream?.write(textToSave.toByteArray())

            }
            Toast.makeText(requireContext(), "Archivo guardado correctamente", Toast.LENGTH_SHORT).show()
            activity?.finish()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error al guardar el archivo", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateText(text: String) {
        textView.text = text
    }

    fun updateEditText(text: String) {
        editText.setText(text)
    }

    fun disableText(){
        editText.isEnabled = false
    }

    fun setFilePath(filePath: String?){

        this.filePath = filePath

    }
}