package com.example.pruebastgg.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.pruebastgg.R

class DisplayCreadorHistoriaExtrema : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_historia)

        val storyTextView: TextView = findViewById(R.id.textViewResult)
        val story = intent.getStringExtra("STORY_TEXT")
        val buttonCopy: ImageButton = findViewById(R.id.buttonCopy)
        //val progressBar: ProgressBar = findViewById(R.id.progressBar)

        //progressBar.visibility = View.GONE

        val typeface = Typeface.createFromAsset(assets, "fonts/handwritten.ttf")
        storyTextView.typeface = typeface

        if (story != null) {
            showTextGradually(storyTextView, story, 1, 5) // Ajusta los valores para escribir más rápido
        }
        buttonCopy.setOnClickListener {
            copyToClipboard(storyTextView.text.toString())
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }

    private fun showTextGradually(textView: TextView, text: String, delay: Long, charsPerIteration: Int) {
        val handler = Handler(Looper.getMainLooper())
        var index = 0
        val buttonCopy: ImageButton = findViewById(R.id.buttonCopy)

        val runnable = object : Runnable {
            override fun run() {
                if (index < text.length) {
                    val endIndex = (index + charsPerIteration).coerceAtMost(text.length)
                    textView.text = text.substring(0, endIndex)
                    index = endIndex
                    handler.postDelayed(this, delay)
                } else {
                    buttonCopy.visibility = View.VISIBLE
                }
            }
        }

        handler.post(runnable)
    }

}
