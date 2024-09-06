package com.example.pruebastgg.ui.ui.cajon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.pruebastgg.R

class DisplayCadaver : Fragment() {

    private lateinit var textViewPrevious: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var editTextInput: EditText
    private lateinit var buttonContinue: Button
    private lateinit var textViewCharCount: TextView
    private lateinit var infoButton: ImageButton

    private var phrases = mutableListOf<String>()
    private var maxPhrases = 5
    private var currentPlayer = 1
    private var currentRound = 1
    private var totalPlayers = 1
    private var totalRounds = 1

    private lateinit var sharedPreferences: SharedPreferences
    private val maxCharacters = 280

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cadaver, container, false)

        textViewPrevious = view.findViewById(R.id.textViewPrevious)
        scrollView = view.findViewById(R.id.scrollView)
        editTextInput = view.findViewById(R.id.editTextInput)
        buttonContinue = view.findViewById(R.id.buttonContinue)
        textViewCharCount = view.findViewById(R.id.textViewCharCount)
        infoButton = view.findViewById(R.id.infoButton)

        sharedPreferences = requireActivity().getSharedPreferences("com.example.pruebastgg.PREFERENCES", Context.MODE_PRIVATE)

        if (!sharedPreferences.getBoolean("DISPLAY_CADAVER_SHOWN", false)) {
            showExplanationDialog()
        }

        getNumberOfPlayersAndRounds()

        buttonContinue.setOnClickListener {
            handleContinue()
        }

        editTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charCount = s?.length ?: 0
                textViewCharCount.text = "$charCount/$maxCharacters"
                if (charCount > maxCharacters) {
                    editTextInput.setText(s?.substring(0, maxCharacters))
                    editTextInput.setSelection(maxCharacters)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        infoButton.setOnClickListener {
            val builder = android.app.AlertDialog.Builder(requireContext())
            builder.setTitle(Html.fromHtml("¿Cómo jugar al <b>Cadáver Exquisito</b>?", Html.FROM_HTML_MODE_COMPACT))
            builder.setMessage(
                Html.fromHtml(
                    "<br>Para jugar a esta versión del Cadáver Exquisito, cada jugador escribirá una frase, " +
                            "                    y, tras darle a continuar, le pasará el móvil al siguiente jugador. <br>El jugador con el móvil solo podrá leer la frase del jugador anterior" +
                            "                    y escribir una frase que continue la historia. <br>En el caso de dos jugadores, solo se leerán las 13 últimas palabras de la frase del jugador anterior.",
                    Html.FROM_HTML_MODE_COMPACT
                )
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }
        return view
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Instrucciones")
            .setMessage("Para jugar a esta versión del Cadáver Exquisito, cada jugador escribirá una frase," +
                    "y, tras darle a continuar, le pasará el móvil al siguiente jugador. El jugador con el móvil solo podrá leer la frase del jugador anterior" +
                    "y escribir una frase que continue la historia. En el caso de dos jugadores, solo se leerán las 13 últimas palabras de la frase del jugador anterior. ")
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("No volver a mostrar") { dialog, _ ->
                with(sharedPreferences.edit()) {
                    putBoolean("DISPLAY_CADAVER_SHOWN", true)
                    apply()
                }
                dialog.dismiss()
            }
            .show()
    }

    private fun getNumberOfPlayersAndRounds() {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_players_rounds_dialog, null)
        builder.setView(dialogView)

        val editTextPlayers = dialogView.findViewById<EditText>(R.id.editTextPlayers)
        val editTextRounds = dialogView.findViewById<EditText>(R.id.editTextRounds)

        builder.setPositiveButton("OK", null)

        val dialog = builder.create()
        dialog.setCancelable(false)
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setOnClickListener {
                val players = editTextPlayers.text.toString().toIntOrNull() ?: 0
                val rounds = editTextRounds.text.toString().toIntOrNull() ?: 0
                if (players < 2) {
                    editTextPlayers.error = "El número de jugadores debe ser, al menos, 2."
                } else if (rounds < 1) {
                    editTextRounds.error = "El número de rondas debe ser, al menos, 1."
                } else {
                    totalPlayers = players
                    totalRounds = editTextRounds.text.toString().toIntOrNull() ?: 1
                    maxPhrases = totalPlayers * totalRounds
                    updateRoundAndPlayerText()
                    dialog.dismiss()
                }
            }
        }

        dialog.show()
    }

    private fun updateRoundAndPlayerText() {
        textViewPrevious.text = "Ronda $currentRound - Jugador $currentPlayer\n"
    }

    private fun handleContinue() {
        val inputText = editTextInput.text.toString().trim()

        if (inputText.isNotEmpty() && phrases.size < maxPhrases) {
            phrases.add(inputText)
            editTextInput.text.clear()

            if (phrases.size < maxPhrases) {
                currentPlayer++
                if (currentPlayer > totalPlayers) {
                    currentPlayer = 1
                    currentRound++
                }
                updateRoundAndPlayerText()

                val displayWordsCount = if (totalPlayers == 2) 13 else Int.MAX_VALUE
                val lastPhrase = phrases.last()
                val words = lastPhrase.split(" ")
                val lastWords = words.takeLast(displayWordsCount).joinToString(" ")

                if (totalPlayers == 2) {
                    textViewPrevious.append("\n...$lastWords")
                } else {
                    textViewPrevious.append("\n$lastPhrase")
                }

                scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
            }

            if (phrases.size == maxPhrases - 1) {
                buttonContinue.text = "Ver resultado"
            } else if (phrases.size == maxPhrases) {
                showCompleteText()
            }
        }
    }

    private fun showCompleteText() {
        val completeText = phrases.joinToString("\n\n")
        val intent = Intent(requireContext(), FinalResultActivity::class.java)
        intent.putExtra("FINAL_TEXT", completeText)
        startActivity(intent)
    }
}
