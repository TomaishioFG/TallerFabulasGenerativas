package com.example.pruebastgg.ui.ui.cajon

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pruebastgg.R

class FinalResultActivity : AppCompatActivity() {

    private lateinit var textViewFinalResult: TextView
    private lateinit var buttonCopy: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_historia)
        textViewFinalResult = findViewById(R.id.textViewResult)
        val buttonCopy: ImageButton = findViewById(R.id.buttonCopy)
        textViewFinalResult.textSize = 24f

        val typeface = Typeface.createFromAsset(assets, "fonts/handwritten.ttf")
        textViewFinalResult.typeface = typeface

        val completeText = intent.getStringExtra("FINAL_TEXT") ?: ""
        Log.d("FinalResultActivity", "Texto completo recibido: $completeText")
        textViewFinalResult.text = completeText
        buttonCopy.visibility = View.VISIBLE
        buttonCopy.setOnClickListener {
            copyToClipboard(textViewFinalResult.text.toString())
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }
}
