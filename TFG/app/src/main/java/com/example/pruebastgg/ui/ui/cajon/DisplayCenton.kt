package com.example.pruebastgg.ui.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pruebastgg.DBUtils
import com.example.pruebastgg.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DisplayCenton : Fragment() {

    private lateinit var textView: TextView
    private lateinit var buttonCopy: ImageButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_display_historia, container, false)
        textView = view.findViewById(R.id.textViewResult)
        buttonCopy = view.findViewById(R.id.buttonCopy)

        textView.textSize = 24f

        val typeface = Typeface.createFromAsset(requireContext().assets, "fonts/handwritten.ttf")
        textView.typeface = typeface

        buttonCopy.setOnClickListener {
            copyToClipboard(textView.text.toString())
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRandomPhrases()
    }

    private fun fetchRandomPhrases() {
        Thread {
            try {
                val conn = DBUtils.getConnection("centon")
                if (conn != null) {
                    val sql = "SELECT frase, libro FROM centon ORDER BY random() LIMIT 13"
                    val statement: PreparedStatement = conn.prepareStatement(sql)
                    val resultSet: ResultSet = statement.executeQuery()

                    val frases = mutableListOf<Pair<String, String>>()
                    val libros = mutableSetOf<String>()

                    while (resultSet.next()) {
                        val frase = resultSet.getString("frase")
                        val libro = resultSet.getString("libro")
                        frases.add(Pair(frase, libro))
                        libros.add(libro)
                    }
                    resultSet.close()
                    statement.close()
                    conn.close()

                    frases.shuffle()

                    val frasesStringBuilder = StringBuilder()
                    frases.forEach { (frase, libro) ->
                        frasesStringBuilder.append(frase).append("\n")
                    }

                    frasesStringBuilder.append("\nLibros:\n")
                    frasesStringBuilder.append(libros.joinToString(", "))

                    requireActivity().runOnUiThread {
                        showTextGradually(textView, frasesStringBuilder.toString(), 1, 5)
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showTextGradually(textView: TextView, text: String, delay: Long, charsPerIteration: Int) {
        val handler = Handler(Looper.getMainLooper())
        var index = 0

        val runnable = object : Runnable {
            override fun run() {
                if (index < text.length) {
                    val endIndex = (index + charsPerIteration).coerceAtMost(text.length)
                    textView.text = text.substring(0, endIndex)
                    index = endIndex
                    handler.postDelayed(this, delay)
                } else {
                    view?.findViewById<ImageButton>(R.id.buttonCopy)?.visibility = View.VISIBLE
                }
            }
        }

        handler.post(runnable)
    }
    private fun copyToClipboard(text: String) {
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()
    }
}
