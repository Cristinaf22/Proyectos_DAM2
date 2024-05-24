package com.example.multimediaapplication.activities

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.multimediaapplication.R
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val buttonCreateText = findViewById<Button>(R.id.buttonCreateText)
        val buttonCaptureImage = findViewById<Button>(R.id.buttonCaptureImage)
        val buttonRecordVideo = findViewById<Button>(R.id.buttonRecordVideo)
        val buttonRecordSound = findViewById<Button>(R.id.buttonRecordSound)
        val buttonShowText = findViewById<Button>(R.id.buttonShowText)
        val buttonShowImage = findViewById<Button>(R.id.buttonShowImage)
        val buttonShowAudio = findViewById<Button>(R.id.buttonShowAudio)
        val buttonShowVideo = findViewById<Button>(R.id.buttonShowVideo)
        val buttonEditText = findViewById<Button>(R.id.buttonEditText)
        val buttonEditImage = findViewById<Button>(R.id.buttonEditImage)
        val buttonMaps = findViewById<ImageView>(R.id.buttonMaps)
        val buttonChrome = findViewById<ImageView>(R.id.buttonChrome)
        val buttonWhatsapp = findViewById<ImageView>(R.id.buttonWhatsapp)


        buttonCreateText.setOnClickListener()
        {
            val intent = Intent(this, TextActivity::class.java)
            startActivity(intent)
        }

        buttonCaptureImage.setOnClickListener()
        {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)
        }

        buttonRecordVideo.setOnClickListener()
        {
            val cameraIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            resultLauncher.launch(cameraIntent)
        }

        buttonRecordSound.setOnClickListener()
        {
            //abrir la grabadora y grabar algún sonido, ponerle un nombre(dialogo) y guardarlo
            val recordAudioIntent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
            //resultLauncher.launch(recordAudioIntent)
            if (recordAudioIntent.resolveActivity(packageManager) != null) {
                resultAudioLauncher.launch(recordAudioIntent)
            } else {
                Toast.makeText(this, "No hay ninguna aplicación disponible para grabar audio", Toast.LENGTH_SHORT).show()
            }
        }

        buttonShowText.setOnClickListener()
        {
            //https://medium.com/@abdulhamidrpn/how-to-choose-a-file-from-a-specific-folder-in-android-13-kotlin-f539b24e7c5a
            //abrir la carpeta de files/Download y el contenido del archivo de texto escogido
            // colocarlo en el textView

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            openFileLauncher.launch(intent)

        }

        buttonShowImage.setOnClickListener()
        {
            //abrir la carpeta de files/Pictures y la imagen escogida
            // colocarlo en el imageView
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            openFileImageLauncher.launch(intent)
        }

        buttonShowAudio.setOnClickListener()
        {
            //abrir la carpeta de files/Music y el archivo escogido
            // reproducirlo
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "audio/*"
            openAudioLauncher.launch(intent)
        }

        buttonShowVideo.setOnClickListener()
        {
            //abrir la carpeta de files/Movies y el archivo escogido
            // reproducirlo
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
            openVideoLauncher.launch(intent)
        }

        buttonEditText.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "text/plain"
            openFileLauncherEdit.launch(intent)
        }

        buttonEditImage.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            openFileEditImageLauncher.launch(intent)
        }

        buttonMaps.setOnClickListener {
            showAddressDialog()
        }

        buttonChrome.setOnClickListener {
            showArticleDialog()
        }

        buttonWhatsapp.setOnClickListener {
            showUrlDialog()
        }

    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val bitmap = result.data?.extras?.get("data") as Bitmap?
                    val videoUri = result.data?.data

                    val inputEditTextField = EditText(this)

                    val dialogo: AlertDialog.Builder = AlertDialog.Builder(this)
                    dialogo.setTitle("Importante")
                    dialogo.setMessage("Escribe el nombre del archivo")
                    dialogo.setView(inputEditTextField)
                    dialogo.setCancelable(false)
                    dialogo.setPositiveButton("OK") { _, _ ->
                        if(bitmap != null) {
                            saveImageToFile(inputEditTextField.text.toString(), bitmap)
                        } else if (videoUri != null)
                        {
                            saveVideoToFile(inputEditTextField.text.toString(), videoUri!!)
                        }
                    }
                        .setNegativeButton("Cancel", null)
                        .create()
                    dialogo.show()
                }
            }
        }

    private val resultAudioLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result?.data != null) {
                    val audioUri = result.data?.data

                    val inputEditTextField = EditText(this)

                    val dialogo: AlertDialog.Builder = AlertDialog.Builder(this)
                    dialogo.setTitle("Importante")
                    dialogo.setMessage("Escribe el nombre del archivo")
                    dialogo.setView(inputEditTextField)
                    dialogo.setCancelable(false)
                    dialogo.setPositiveButton("OK") { _, _ ->
                        if (audioUri != null)
                        {
                            saveAudioToFile(inputEditTextField.text.toString(), audioUri!!)
                        }
                    }
                        .setNegativeButton("Cancel", null)
                        .create()
                    dialogo.show()
                }
            }
        }

    private val openFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val uri: Uri = data.data ?: return@registerForActivityResult
                val fileContent = readFileContent(uri)
                val intent = Intent(this, TextActivity::class.java)
                intent.putExtra("fileContent", fileContent)
                intent.putExtra("mode", "view")
                startActivity(intent)
            }
        }
    }

    private val openFileLauncherEdit = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val uri: Uri = data.data ?: return@registerForActivityResult
                val fileContent = readFileContent(uri)
                val intent = Intent(this, TextActivity::class.java)
                intent.putExtra("fileContent", fileContent)
                intent.putExtra("mode", "edit")
                intent.putExtra("filePath", uri.toString())
                startActivity(intent)
            }
        }
    }

    private val openFileImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val imageUri: Uri? = data?.data
                val intent = Intent(this, ImageActivity::class.java)
                intent.putExtra("imageUri", imageUri.toString())
                intent.putExtra("mode", "view")
                startActivity(intent)
            }
        }
    }

    private val openFileEditImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val imageUri: Uri? = data?.data
                val intent = Intent(this, ImageActivity::class.java)
                intent.putExtra("imageUri", imageUri.toString())
                intent.putExtra("mode", "edit")
                startActivity(intent)
            }
        }
    }

    private val openAudioLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val uri: Uri? = data.data
                val intent = Intent(this, AudioActivity::class.java)
                intent.putExtra("path", uri.toString())
                startActivity(intent)
            }
        }
    }

    private val openVideoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val uri: Uri? = data.data
                val intent = Intent(this, VideoActivity::class.java)
                intent.putExtra("path", uri.toString())
                startActivity(intent)
            }
        }
    }

    private fun readFileContent(uri: Uri): String {
        val contentResolver: ContentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val stringBuilder = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            reader.close()
        }
        return stringBuilder.toString()
    }

    private fun saveImageToFile(userInput : String, bitmap: Bitmap) {
        var filename = userInput
        if (!userInput.endsWith(".jpg")){
            filename += ".jpg"
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            filename
        )
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
            Toast.makeText(this, "Imagen guardada correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveVideoToFile(userInput : String, videoUri: Uri)
    {
        var filename = userInput
        if (!userInput.endsWith(".mp4")){
            filename += ".mp4"
        }
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),
            filename
        )
        try {
            val inputStream = contentResolver.openInputStream(videoUri!!)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Toast.makeText(this, "Video guardado correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAudioToFile(userInput : String, audioUri: Uri)
    {
        var filename = userInput
        if (!userInput.endsWith(".mp3")){
            filename += ".mp3"
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            filename
        )
        try {
            val inputStream = contentResolver.openInputStream(audioUri!!)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Toast.makeText(this, "Audio guardado correctamente", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el audio", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddressDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ingrese la dirección")

        // EditText para ingresar la dirección
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val address = input.text.toString()
            openGoogleMaps(address)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun openGoogleMaps(address: String) {
        // Construir la URI de Google Maps con la dirección ingresada
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        // Comprobar si hay aplicaciones que puedan manejar esta intención
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else {
            // Si no hay aplicaciones que puedan manejar la intención, mostrar un mensaje de error
            Toast.makeText(this, "No se encontró la aplicación Google Maps", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showArticleDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ingrese el artículo de Wikipedia")

        // EditText para ingresar el artículo
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val article = input.text.toString()
            openWikipediaArticleInChrome(article)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun openWikipediaArticleInChrome(article: String) {
        val wikipediaUrl = "https://es.wikipedia.org/wiki/$article"
        val chromePackageName = "com.android.chrome"
        val chromeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
        chromeIntent.setPackage(chromePackageName)

        if (chromeIntent.resolveActivity(packageManager) != null) {
            startActivity(chromeIntent)
        } else {
            // Si Chrome no está instalado, abrir en el navegador predeterminado
            val defaultBrowserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaUrl))
            startActivity(defaultBrowserIntent)
        }
    }

    private fun showUrlDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Ingrese la URL")

        // EditText para ingresar la URL
        val input = EditText(this)
        builder.setView(input)

        builder.setPositiveButton("Aceptar") { dialog, _ ->
            val url = input.text.toString()
            shareUrlOnWhatsApp(url)
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun shareUrlOnWhatsApp(url: String) {
        // Verificar si la URL es válida
        if (url.isNotEmpty()) {
            // Formar el mensaje con la URL
            val message = "¡Mira este enlace! $url"

            // Crear un Intent para compartir en WhatsApp
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.`package` = "com.whatsapp" // Filtro para abrir solo WhatsApp

            // Verificar si WhatsApp está instalado en el dispositivo
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Si WhatsApp no está instalado, mostrar un mensaje de error
                Toast.makeText(this, "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Si la URL está vacía, mostrar un mensaje de error
            Toast.makeText(this, "La URL no puede estar vacía", Toast.LENGTH_SHORT).show()
        }
    }

}