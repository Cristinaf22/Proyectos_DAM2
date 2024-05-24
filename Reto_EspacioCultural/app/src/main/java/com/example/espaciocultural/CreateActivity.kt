package com.example.espaciocultural

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.espaciocultural.events.BaseEvent
import com.example.espaciocultural.events.DebateEvent
import com.example.espaciocultural.events.MovieEvent
import com.example.espaciocultural.events.OperaConcertEvent
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Locale


open class CreateActivity : AppCompatActivity() {

    private val HIGH_RES_IMAGE_NAME = "_principal.png"
    private val LOW_RES_IMAGE_NAME = "_llista.png"

    private var calendar: Calendar = Calendar.getInstance()
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val image = findViewById<ImageView>(R.id.imgEvent)
        val btnCreate = findViewById<Button>(R.id.btnCreate)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        setupEventDate()
        setupEventHour()

        val eventType = intent.getStringExtra("EventType")
        showEventType(eventType)
        showEventFields(eventType)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            //storage/1AE9-190B/Pictures/test
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(contentResolver, uri)
                    bitmap = ImageDecoder.decodeBitmap(source)
                }
                image.setImageBitmap(bitmap)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        image.setOnClickListener(){
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnCreate.setOnClickListener()
        {

            if(saveEvent(eventType)) {
                Toast.makeText(this, "Evento creado correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error creando evento", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            finish()
        }

        btnCancel.setOnClickListener()
        {
            finish()
        }

    }

    private fun saveEvent(eventType: String?) : Boolean {
        val nameEvent = findViewById<EditText>(R.id.eventName)
        if (nameEvent.text.isNullOrEmpty()) return false;
        val typeEvent = findViewById<EditText>(R.id.typeCreateEvent)
        if (typeEvent.text.isNullOrEmpty()) return false;
        val languageEvent = findViewById<EditText>(R.id.languageEvent)
        if (languageEvent.text.isNullOrEmpty()) return false;
        val numberedSeats = findViewById<CheckBox>(R.id.numCapacityEvent)
        val maxCapacity = findViewById<EditText>(R.id.maxCapacityEvent)
        if (maxCapacity.text.isNullOrEmpty()) return false;
        val ticketPrice = findViewById<EditText>(R.id.priceEvent)
        if (ticketPrice.text.isNullOrEmpty()) return false;
        val directorName = findViewById<EditText>(R.id.directorNameEvent)
        val premierYear = findViewById<EditText>(R.id.premiereYearEvent)
        val mainActors = findViewById<EditText>(R.id.mainActorsListEvent)
        val durationMovieEvent = findViewById<EditText>(R.id.durationMovieEvent)
        val compositorNameEvent = findViewById<EditText>(R.id.compositorNameEvent)
        val soloistListEvent = findViewById<EditText>(R.id.soloistListEvent)
        val chorusNameEvent = findViewById<EditText>(R.id.chorusNameEvent)
        val orchestraNameEvent = findViewById<EditText>(R.id.orchestraNameEvent)
        val participantsListEvent = findViewById<EditText>(R.id.participantsListEvent)

        val event : BaseEvent
        val id = BaseEvent.getNewEventId()

        saveBitmapImage(id)

        //Agregar película
        if (eventType == "Película")
        {
            if (directorName.text.isNullOrEmpty()) return false;
            if (premierYear.text.isNullOrEmpty()) return false;
            if (durationMovieEvent.text.isNullOrEmpty()) return false;
            if (mainActors.text.isNullOrEmpty()) return false;
            val mainActorsAsList = mainActors.text.toString().split(",")
            event = MovieEvent(
                id,
                nameEvent.text.toString(),
                id.toString() + LOW_RES_IMAGE_NAME,
                id.toString() + HIGH_RES_IMAGE_NAME,
                typeEvent.text.toString(),
                LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()),
                languageEvent.text.toString(),
                mutableMapOf(),
                mutableListOf(),
                numberedSeats.isChecked,
                maxCapacity.text.toString().toInt(),
                ticketPrice.text.toString().toDouble(),
                directorName.text.toString(),
                premierYear.text.toString().toInt(),
                mainActorsAsList,
                durationMovieEvent.text.toString().toInt()
            )
        } else if (eventType == "Concierto de Ópera")
        {
            if (compositorNameEvent.text.isNullOrEmpty()) return false;
            if (chorusNameEvent.text.isNullOrEmpty()) return false;
            if (orchestraNameEvent.text.isNullOrEmpty()) return false;
            if (soloistListEvent.text.isNullOrEmpty()) return false;
            val soloistsAsList = soloistListEvent.text.toString().split(",")
            event = OperaConcertEvent(
                id,
                nameEvent.text.toString(),
                id.toString() + LOW_RES_IMAGE_NAME,
                id.toString() + HIGH_RES_IMAGE_NAME,
                typeEvent.text.toString(),
                LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()),
                languageEvent.text.toString(),
                mutableMapOf(),
                mutableListOf(),
                numberedSeats.isChecked,
                maxCapacity.text.toString().toInt(),
                ticketPrice.text.toString().toDouble(),
                compositorNameEvent.text.toString(),
                soloistsAsList,
                chorusNameEvent.text.toString(),
                orchestraNameEvent.text.toString()
            )
        } else if (eventType == "Debate")
        {
            if (participantsListEvent.text.isNullOrEmpty()) return false;
            val participantsAsList = participantsListEvent.text.toString().split(",")
            event = DebateEvent(
                id,
                nameEvent.text.toString(),
                id.toString() + LOW_RES_IMAGE_NAME,
                id.toString() + HIGH_RES_IMAGE_NAME,
                typeEvent.text.toString(),
                LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()),
                languageEvent.text.toString(),
                mutableMapOf(),
                mutableListOf(),
                numberedSeats.isChecked,
                maxCapacity.text.toString().toInt(),
                ticketPrice.text.toString().toDouble(),
                participantsAsList
            )
        } else {
            event = BaseEvent(
                id,
                nameEvent.text.toString(),
                id.toString() + LOW_RES_IMAGE_NAME,
                id.toString() + HIGH_RES_IMAGE_NAME,
                typeEvent.text.toString(),
                LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault()),
                languageEvent.text.toString(),
                mutableMapOf(),
                mutableListOf(),
                numberedSeats.isChecked,
                maxCapacity.text.toString().toInt(),
                ticketPrice.text.toString().toDouble()
            )
        }

        BaseEvent.addEvent(event, filesDir.toString())

        return true
    }

    private fun saveBitmapImage(id: Int) {
        val highResPath = "$filesDir/img/$id$HIGH_RES_IMAGE_NAME"
        val lowResPath = "$filesDir/img/$id$LOW_RES_IMAGE_NAME"

        val highResOutStream = FileOutputStream(highResPath)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, highResOutStream)
        highResOutStream.flush()
        highResOutStream.close()

        val lowResBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false)

        val lowResOutStream = FileOutputStream(lowResPath)
        lowResBitmap.compress(Bitmap.CompressFormat.PNG, 100, lowResOutStream)
        lowResOutStream.flush()
        lowResOutStream.close()
    }

    // Muestra los campos del tipo de evento que quieres crear
    private fun showEventFields(eventType: String?)
    {
        val directorName = findViewById<EditText>(R.id.directorNameEvent)
        val premierYear = findViewById<EditText>(R.id.premiereYearEvent)
        val mainActors = findViewById<EditText>(R.id.mainActorsListEvent)
        val durationMovieEvent = findViewById<EditText>(R.id.durationMovieEvent)
        val compositorNameEvent = findViewById<EditText>(R.id.compositorNameEvent)
        val soloistListEvent = findViewById<EditText>(R.id.soloistListEvent)
        val chorusNameEvent = findViewById<EditText>(R.id.chorusNameEvent)
        val orchestraNameEvent = findViewById<EditText>(R.id.orchestraNameEvent)
        val participantsListEvent = findViewById<EditText>(R.id.participantsListEvent)

        when (eventType) {
            "Película" -> {
                directorName.visibility = View.VISIBLE
                premierYear.visibility = View.VISIBLE
                mainActors.visibility = View.VISIBLE
                durationMovieEvent.visibility = View.VISIBLE
            }
            "Concierto de Ópera" -> {
                compositorNameEvent.visibility = View.VISIBLE
                soloistListEvent.visibility = View.VISIBLE
                chorusNameEvent.visibility = View.VISIBLE
                orchestraNameEvent.visibility = View.VISIBLE
            }
            "Debate" -> {
                participantsListEvent.visibility = View.VISIBLE
            }
        }
    }

    // Muestra el tipo de evento según el evento que se quiere crear
    private fun showEventType(eventType: String?) {

        val eventTypeEditText = findViewById<EditText>(R.id.typeCreateEvent)
        eventTypeEditText.setText(eventType)
        // Si el evento es diferente de "Otro" se puede editar, sino no se puede editar
        if (eventType == null) {
            eventTypeEditText.isEnabled = true
        } else {
            eventTypeEditText.isEnabled = false
        }

    }

    private fun setupEventDate() {

        val date = findViewById<EditText>(R.id.dateEvent)

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd MMM yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            date.setText(sdf.format(calendar.time))
        }

        date.setOnClickListener() {
            DatePickerDialog(
                this@CreateActivity, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupEventHour() {
        val hour = findViewById<EditText>(R.id.hourEvent)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm"
            val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
            hour.setText(sdf.format(calendar.time))
        }

        hour.setOnClickListener(){
            TimePickerDialog(
                this@CreateActivity, timeSetListener,
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }


}