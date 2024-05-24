package com.example.multimediaapplication.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.scale
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageView
import com.example.multimediaapplication.R
import com.example.multimediaapplication.models.Filter
import com.example.multimediaapplication.models.FilterAdapter
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import ja.burhanrashid52.photoeditor.PhotoFilter
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


class ImageFragment: Fragment(), CropImageView.OnCropImageCompleteListener
{
    private var cropImageView: CropImageView? = null
    private var buttonCrop: Button? = null
    private var photoEditorView: PhotoEditorView? = null
    private var horizontalScroll: HorizontalScrollView? = null

    private var argImageUri: String = "imageUri"

    private var photoEditor : PhotoEditor? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        val view = inflater.inflate(R.layout.image_fragment, container, false)

        val buttonReturn = view?.findViewById<Button>(R.id.buttonReturn)
        buttonCrop = view?.findViewById(R.id.buttonCrop)
        cropImageView = view?.findViewById(R.id.cropImageView)
        photoEditorView = view?.findViewById(R.id.photoEditorView)
        horizontalScroll = view?.findViewById<HorizontalScrollView>(R.id.horizontalScroll)


        cropImageView?.setOnCropImageCompleteListener(this)


        buttonCrop?.setOnClickListener(){
            cropImageView?.croppedImageAsync()
        }



        buttonReturn?.setOnClickListener(){

            activity?.finish()
        }

        return view
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        if (result.error == null) {
            cropImageView?.setImageUriAsync(result.uriContent)
        }
    }

    fun viewImage(imageUri: String) {
        photoEditorView?.isVisible = false
        horizontalScroll?.isVisible = false

        cropImageView?.setImageUriAsync(Uri.parse(imageUri))
    }

    fun editImage(imageUri: String) {
        buttonCrop?.isVisible = false
        cropImageView?.isVisible = false


        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, Uri.parse(imageUri));
        val ratio = bitmap.width.toFloat() / bitmap.height.toFloat()
        photoEditorView?.source?.setImageBitmap(bitmap.scale((2000 * ratio).toInt(), 2000))

        //Use custom font using latest support library
        //Use custom font using latest support library
        val mTextRobotoTf = ResourcesCompat.getFont(requireContext(), R.font.roboto_medium)


        photoEditor = PhotoEditor.Builder(requireContext(), photoEditorView!!)
            .setPinchTextScalable(true)
            .setClipSourceImage(true)
            .setDefaultTextTypeface(mTextRobotoTf)
            .build()

        val filterDialog = addFilters()
        editImageButtonsSetup(filterDialog)

    }

    private fun addFilters() : AlertDialog.Builder {

        val filters = mutableListOf<Filter>()
        for (filter in PhotoFilter.values()) {
            filters.add(Filter(filter.name, R.drawable.mariposa))
        }
        val adapter = FilterAdapter(requireContext(), R.layout.filter_item, filters)

        val filterDialog = AlertDialog.Builder(requireContext())
        filterDialog.setTitle("Select Filter")

        filterDialog.setAdapter(adapter, DialogInterface.OnClickListener() {
            _, i ->
            val activeFilter = PhotoFilter.valueOf(filters[i].name)
            photoEditor?.setFilterEffect(activeFilter)
        })

        return filterDialog
    }

    private fun showSaveFileDialog() {
        val dialogTextFile = AlertDialog.Builder(requireContext())
        dialogTextFile.setTitle("Indicar el nombre de la imagen")
        val input = EditText(requireContext())
        dialogTextFile.setView(input)

        dialogTextFile.setPositiveButton("Guardar") { _, _ ->
            var fileName = input.text.toString()
            if (fileName.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val bitmap = photoEditor?.saveAsBitmap()
                    if (!fileName.endsWith(".jpg")){
                        fileName += ".jpg"
                    }
                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        fileName
                    )
                    try {
                        val out = FileOutputStream(file)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()
                        Toast.makeText(context, "Imagen guardada correctamente", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
                        activity?.finish()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Debe ingresar un nombre de archivo", Toast.LENGTH_SHORT).show()
            }
        }
        dialogTextFile.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        dialogTextFile.show()
    }

    private fun showTextDialog() {
        val dialogTextFile = AlertDialog.Builder(requireContext())
        dialogTextFile.setTitle("Escribe un texto")
        val input = EditText(requireContext())
        dialogTextFile.setView(input)

        dialogTextFile.setPositiveButton("Guardar") { _, _ ->
            val fileName = input.text.toString()
            photoEditor?.addText(fileName, Color.RED)

        }
        dialogTextFile.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        dialogTextFile.show()
    }

    private fun editImageButtonsSetup(filterDialog: AlertDialog.Builder) {

        val buttonBrush = view?.findViewById<ImageView>(R.id.buttonBrush)
        val buttonText = view?.findViewById<ImageView>(R.id.buttonText)
        val buttonEraser = view?.findViewById<ImageView>(R.id.buttonEraser)
        val buttonFilter = view?.findViewById<ImageView>(R.id.buttonFilter)
        val buttonUndo = view?.findViewById<ImageView>(R.id.buttonUndo)
        val buttonRedo = view?.findViewById<ImageView>(R.id.buttonRedo)
        val buttonSave = view?.findViewById<Button>(R.id.buttonSave)

        buttonSave?.isVisible = true

        buttonSave?.setOnClickListener()
        {
            showSaveFileDialog()
        }

        buttonBrush?.setOnClickListener()
        {
            photoEditor?.setBrushDrawingMode(true)
        }

        buttonText?.setOnClickListener()
        {
            showTextDialog()
        }

        buttonEraser?.setOnClickListener()
        {
            photoEditor?.brushEraser()
        }

        buttonFilter?.setOnClickListener()
        {
            filterDialog.show()
        }

        buttonUndo?.setOnClickListener()
        {
            photoEditor?.undo()
        }

        buttonRedo?.setOnClickListener()
        {
            photoEditor?.redo()
        }



    }
}