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
import com.example.pruebastgg.DBUtils
import com.example.pruebastgg.R
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DisplayCreadorHistoria : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var buttonCopy: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_historia)

        textViewResult = findViewById(R.id.textViewResult)
        buttonCopy = findViewById(R.id.buttonCopy)
        //progressBar = findViewById(R.id.progressBar)

        //progressBar.visibility = View.GONE
        val typeface = Typeface.createFromAsset(assets, "fonts/handwritten.ttf")
        textViewResult.typeface = typeface

        val generosSecundarios = intent.getStringArrayListExtra("GENEROS_SECUNDARIOS") ?: emptyList<String>()
        val tramas = intent.getStringArrayListExtra("TRAMAS") ?: emptyList<String>()
        val narrador = intent.getStringExtra("NARRADOR") ?: "tercera"
        val generoPrincipal = intent.getStringExtra("GENERO_PRINCIPAL") ?: "fantasia"

        fetchAndCombinePhrases(generosSecundarios, tramas, narrador, generoPrincipal)
        buttonCopy.setOnClickListener {
            copyToClipboard(textViewResult.text.toString())
        }
    }

    private fun copyToClipboard(text: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Texto copiado al portapapeles", Toast.LENGTH_SHORT).show()

    }

    private fun fetchAndCombinePhrases(generosSecundarios: List<String>, tramas: List<String>, narrador: String, generoPrincipal: String) {
        Thread {
            try {
                val conn = DBUtils.getConnection(generoPrincipal)
                if (conn != null) {
                    val allNumbers = (1..20).toList()
                    val numberAssignments = assignNumbersToGenres(allNumbers, generosSecundarios)

                    val query = buildJoinQuery(generosSecundarios, tramas, narrador, numberAssignments)
                    println("Executing query: $query")
                    val statement: PreparedStatement = conn.prepareStatement(query)
                    val resultSet: ResultSet = statement.executeQuery()
                    val allPhrases = mutableMapOf<Int, MutableList<String>>()

                    while (resultSet.next()) {
                        val numero = resultSet.getInt("numero")
                        val frase = resultSet.getString("frase")
                        if (numero !in allPhrases) {
                            allPhrases[numero] = mutableListOf()
                        }
                        allPhrases[numero]?.add(frase)
                    }
                    resultSet.close()
                    statement.close()
                    conn.close()

                    val combinedPhrases = draft(allPhrases)
                    val storyText = combinedPhrases.joinToString("\n")

                    runOnUiThread {
                        showTextGradually(textViewResult, storyText, 1, 5) // Ajusta los valores para escribir más rápido o más lento
                    }
                }
            } catch (e: SQLException) {
                e.printStackTrace()
                runOnUiThread {
                    progressBar.visibility = View.GONE
                }
            }
        }.start()
    }

    private fun assignNumbersToGenres(allNumbers: List<Int>, generosSecundarios: List<String>): Map<String, List<Int>> {
        val numberAssignments = mutableMapOf<String, MutableList<Int>>()
        val shuffledNumbers = allNumbers.shuffled()

        generosSecundarios.forEach { genero ->
            numberAssignments[genero] = mutableListOf()
        }

        shuffledNumbers.forEachIndexed { index, number ->
            val genero = generosSecundarios[index % generosSecundarios.size]
            numberAssignments[genero]?.add(number)
        }

        return numberAssignments
    }

    private fun buildJoinQuery(generosSecundarios: List<String>, tramas: List<String>, narrador: String, numberAssignments: Map<String, List<Int>>): String {
        val tramaConditions = tramas.joinToString(" OR ") { trama -> "trama = '$trama'" }

        val subqueries = generosSecundarios.map { genero ->
            val numbers = numberAssignments[genero]?.joinToString(",") { it.toString() } ?: ""
            "(SELECT numero, frase FROM $genero WHERE ($tramaConditions) AND persona = '$narrador' AND numero IN ($numbers))"
        }

        return subqueries.joinToString(" UNION ")
    }

    private fun draft(allPhrases: Map<Int, List<String>>): List<String> {
        return allPhrases.toSortedMap().map { (_, frases) ->
            frases.randomOrNull() ?: ""
        }
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
                    buttonCopy.visibility = View.VISIBLE
                }
            }
        }

        handler.post(runnable)
    }
}
