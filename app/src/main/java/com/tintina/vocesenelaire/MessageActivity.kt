package com.tintina.vocesenelaire

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MessageActivity : AppCompatActivity() {

    private var photoUri: Uri? = null
    private lateinit var ivPhoto: ImageView

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            photoUri = uri
            ivPhoto.setImageURI(uri)
            ivPhoto.visibility = ImageView.VISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        supportActionBar?.title = "Enviar Mensaje"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etMensaje = findViewById<EditText>(R.id.etMensaje)
        ivPhoto = findViewById(R.id.ivPhoto)

        findViewById<Button>(R.id.btnAdjuntarFoto).setOnClickListener {
            pickImage.launch("image/*")
        }

        findViewById<Button>(R.id.btnEnviar).setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val mensaje = etMensaje.text.toString().trim()

            if (nombre.isEmpty() || mensaje.isEmpty()) {
                Toast.makeText(this, "Completá tu nombre y el mensaje", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val subject = "Mensaje de $nombre - App ${Constants.RADIO_NAME}"

            val intent = if (photoUri != null) {
                Intent(Intent.ACTION_SEND).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_STREAM, photoUri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
            } else {
                Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                }
            }

            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constants.CONTACT_EMAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, mensaje)

            try {
                startActivity(Intent.createChooser(intent, "Enviar con..."))
            } catch (e: Exception) {
                Toast.makeText(this, "No encontramos una app de email instalada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
