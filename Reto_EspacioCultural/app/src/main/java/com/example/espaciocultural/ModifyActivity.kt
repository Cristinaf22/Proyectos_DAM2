package com.example.espaciocultural

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.espaciocultural.events.BaseEvent
import com.example.espaciocultural.events.DebateEvent
import com.example.espaciocultural.events.MovieEvent
import com.example.espaciocultural.events.OperaConcertEvent
import java.io.FileOutputStream
import java.nio.file.Files
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Dictionary
import kotlin.io.path.Path

class ModifyActivity : AppCompatActivity() {

    private val HIGH_RES_IMAGE_NAME = "_principal.png"
    private val LOW_RES_IMAGE_NAME = "_llista.png"

    private var calendar: Calendar = Calendar.getInstance()
    private var bitmap: Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)


        val image = findViewById<ImageView>(R.id.imgModify)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        val positionEvent = intent.getIntExtra("positionEvent", 0)
        val listEvents = BaseEvent.getEventList(filesDir.toString())
        val event = listEvents[positionEvent]

        showEventType(event.type)
        showEventFields(event)

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

        btnSave.setOnClickListener()
        {

            if(saveEvent(event.type, positionEvent, event.id)) {
                Toast.makeText(this, "Evento guardado correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Error creando evento", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            finish()
        }

        btnDelete.setOnClickListener()
        {
            BaseEvent.deleteEvent(positionEvent, filesDir.toString())
            deleteImages(event.imageLowRes, event.imageHighRes)
            Toast.makeText(this, "Evento eliminado correctamente", Toast.LENGTH_LONG).show()
            finish()
        }

        // Tiene que regresar a la activity anterior a la que pertenece*****
        btnCancel.setOnClickListener()
        {
            finish()
        }
    }

    private fun deleteImages(eventImageLowRes: String, eventImageHighRes: String)
    {
        val lowResPath = "$filesDir/img/$eventImageLowRes"
        Files.deleteIfExists(Path(lowResPath))

        val highResPath = "$filesDir/img/$eventImageHighRes"
        Files.deleteIfExists(Path(highResPath))
    }

    private fun saveEvent(eventType: String?, positionEvent: Int, id: Int) : Boolean {
        val nameEvent = findViewById<EditText>(R.id.eventName)
        if (nameEvent.text.isNullOrEmpty()) return false;
        val typeEvent = findViewById<EditText>(R.id.eventType)
        if (typeEvent.text.isNullOrEmpty()) return false;
        val languageEvent = findViewById<EditText>(R.id.eventLanguage)
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

        if (bitmap != null) {
            saveBitmapImage(id)
        }

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

        BaseEvent.updateEvent(event, positionEvent, filesDir.toString())

        return true
    }

    private fun saveBitmapImage(id: Int) {
        val highResPath = "$filesDir/img/$id$HIGH_RES_IMAGE_NAME"
        val lowResPath = "$filesDir/img/$id$LOW_RES_IMAGE_NAME"

        val highResOutStream = FileOutputStream(highResPath)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, highResOutStream)
        highResOutStream.flush()
        highResOutStream.close()

        val lowResBitmap = Bitmap.createScaledBitmap(bitmap!!, 200, 200, false)

        val lowResOutStream = FileOutputStream(lowResPath)
        lowResBitmap.compress(Bitmap.CompressFormat.PNG, 100, lowResOutStream)
        lowResOutStream.flush()
        lowResOutStream.close()
    }

    // Muestra los campos del tipo de evento que quieres crear
    private fun showEventFields(event: BaseEvent)
    {
        val nameEvent = findViewById<EditText>(R.id.eventName)
        nameEvent.setText(event.name)

        val imgEvent = findViewById<ImageView>(R.id.imgModify)
        val imgPath = filesDir.toString() + "/img/" + event.imageHighRes
        val bitmap = BitmapFactory.decodeFile(imgPath)
        imgEvent.setImageBitmap(bitmap)

        val typeEvent = findViewById<EditText>(R.id.eventType)
        typeEvent.setText(event.type)

        val date = findViewById<EditText>(R.id.dateEvent)
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
        date.setText(formatter.format(event.dateHour))

        val hour = findViewById<EditText>(R.id.hourEvent)
        val formatterHour = DateTimeFormatter.ofPattern("HH:mm")
        hour.setText(formatterHour.format(event.dateHour))

        val languageEvent = findViewById<EditText>(R.id.eventLanguage)
        languageEvent.setText(event.language)

        val numberedSeats = findViewById<CheckBox>(R.id.numCapacityEvent)
        numberedSeats.isChecked = event.numberedSeats

        val maxCapacity = findViewById<EditText>(R.id.maxCapacityEvent)
        maxCapacity.setText(event.maxCapacity.toString())

        val ticketPrice = findViewById<EditText>(R.id.priceEvent)
        ticketPrice.setText(event.ticketPrice.toString())

        val directorName = findViewById<EditText>(R.id.directorNameEvent)
        val directorNameEvent = findViewById<TextView>(R.id.directorName)
        val premierYear = findViewById<EditText>(R.id.premiereYearEvent)
        val premierYearEvent = findViewById<TextView>(R.id.premierYear)
        val mainActors = findViewById<EditText>(R.id.mainActorsListEvent)
        val mainActorsList = findViewById<TextView>(R.id.mainActorsList)
        val durationMovieEvent = findViewById<EditText>(R.id.durationMovieEvent)
        val movieDuration = findViewById<TextView>(R.id.movieDuration)
        val compositorNameEvent = findViewById<EditText>(R.id.compositorNameEvent)
        val compositorName = findViewById<TextView>(R.id.compositorName)
        val soloistListEvent = findViewById<EditText>(R.id.soloistListEvent)
        val soloistList = findViewById<TextView>(R.id.soloistList)
        val chorusNameEvent = findViewById<EditText>(R.id.chorusNameEvent)
        val chorusName = findViewById<TextView>(R.id.chorusName)
        val orchestraNameEvent = findViewById<EditText>(R.id.orchestraNameEvent)
        val orchestraName = findViewById<TextView>(R.id.orchestraName)
        val participantsListEvent = findViewById<EditText>(R.id.participantsListEvent)
        val participantsList = findViewById<TextView>(R.id.participantsList)

        when (event.type) {
            "Película" -> {
                // Sabemos que es una pelicula, asi que leemos el objecto como MovieEvent
                val movieEvent = event as MovieEvent
                directorName.setText(movieEvent.directorName)
                premierYear.setText(movieEvent.premiereYear.toString())
                mainActors.setText(movieEvent.mainActors.joinToString(","))
                durationMovieEvent.setText(movieEvent.movieLength.toString())
                directorName.visibility = View.VISIBLE
                directorNameEvent.visibility = View.VISIBLE
                premierYear.visibility = View.VISIBLE
                premierYearEvent.visibility = View.VISIBLE
                mainActors.visibility = View.VISIBLE
                mainActorsList.visibility = View.VISIBLE
                durationMovieEvent.visibility = View.VISIBLE
                movieDuration.visibility = View.VISIBLE
            }
            "Concierto de Ópera" -> {
                val operaConcertEvent = event as OperaConcertEvent
                compositorNameEvent.setText(operaConcertEvent.songwriter)
                soloistListEvent.setText(operaConcertEvent.principalSoloist.joinToString(","))
                chorusNameEvent.setText(operaConcertEvent.chorusName)
                orchestraNameEvent.setText(operaConcertEvent.orchestraName)
                compositorNameEvent.visibility = View.VISIBLE
                compositorName.visibility = View.VISIBLE
                soloistListEvent.visibility = View.VISIBLE
                soloistList.visibility = View.VISIBLE
                chorusNameEvent.visibility = View.VISIBLE
                chorusName.visibility = View.VISIBLE
                orchestraNameEvent.visibility = View.VISIBLE
                orchestraName.visibility = View.VISIBLE
            }
            "Debate" -> {
                val debateEvent = event as DebateEvent
                participantsListEvent.setText(debateEvent.participantList.joinToString(","))
                participantsListEvent.visibility = View.VISIBLE
                participantsList.visibility = View.VISIBLE
            }
        }
    }

    // Muestra el tipo de evento según el evento que se quiere modificar
    private fun showEventType(eventType: String?) {

        val eventTypeEditText = findViewById<EditText>(R.id.eventType)
        eventTypeEditText.setText(eventType)
        // Si el evento es diferente de "Otro" se puede editar, sino no se puede editar
        if (eventType == null) {
            eventTypeEditText.isEnabled = true
        } else {
            eventTypeEditText.isEnabled = false
        }

    }

}