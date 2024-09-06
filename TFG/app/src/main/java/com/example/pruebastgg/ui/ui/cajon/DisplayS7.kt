package com.example.pruebastgg.ui.ui.cajon

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pruebastgg.DBUtils
import com.example.pruebastgg.R
import kotlinx.coroutines.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import android.content.Context

class DisplayS7 : Fragment() {

    private lateinit var inputText: EditText
    private lateinit var resultText: TextView
    private lateinit var wordSelectionLayout: LinearLayout
    private lateinit var charCountText: TextView

    private val selectedWords = mutableListOf<String>()
    private val maxCharacters = 140 // Límite de caracteres

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_s7, container, false)
        val infoButton: ImageButton = view.findViewById(R.id.infoButtonS7)
        infoButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(Html.fromHtml("Método S+7", Html.FROM_HTML_MODE_COMPACT))
            builder.setMessage(
                Html.fromHtml(
                    "Este modo de uso requiere algo más de participación por parte del usuario (y unas normas más estrictas para su correcto funcionamiento, al ser una versión alpha)." +
                            "<br><br>El método S+7 consiste en reemplazar cada sustantivo (S) de un texto por el séptimo sustantivo encontrado a continuación del mismo en un diccionario. " +
                            "<br><br>En este caso, se trabaja con un diccionario con más de 60.000 sustantivos, pero únicamente están en forma en singular. Por ello, para probar el concepto del método, deben ponerse frases (de hasta 140 caracteres)" +
                            " que lleven los sustantivos en singular, y que no estén pegados a ningún signo de puntuación («,», «.», «?», «!», etc)." +
                            "<br><br>Después de escribir la frase, para evitar confusiones, el usuario deberá marcar los sustantivo sobre los que quiere aplicar el s+7." +
                            "<br><br><br>Ejemplo: Eres un crack → Eres un cráneo ✓" +
                            "<br>Ejemplo: Eres un crack. → Eres un crack. ✗" +
                            "<br>Ejemplo: Sois unos cracks → Sois unos cracks ✗",
                    Html.FROM_HTML_MODE_COMPACT
                )
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }

        inputText = view.findViewById(R.id.input_text)
        resultText = view.findViewById(R.id.result_text)
        wordSelectionLayout = view.findViewById(R.id.word_selection_layout)
        charCountText = view.findViewById(R.id.char_count_text)

        view.findViewById<Button>(R.id.process_button).setOnClickListener {
            hideKeyboard()
            val text = inputText.text.toString()
            presentWordSelection(text)
        }

        inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val charCount = s?.length ?: 0
                charCountText.text = "$charCount/$maxCharacters"
                if (charCount > maxCharacters) {
                    inputText.setText(s?.substring(0, maxCharacters))
                    inputText.setSelection(maxCharacters)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        return view
    }

    private fun presentWordSelection(text: String) {
        val spannable = SpannableString(text)
        val words = text.split(" ")

        var start = 0
        for (word in words) {
            val end = start + word.length
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    toggleWordSelection(word)
                    updateWordSpans(spannable, word, start, end)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            }
            spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(ForegroundColorSpan(Color.BLACK), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = end + 1
        }

        val textView = TextView(context)
        textView.id = View.generateViewId()
        textView.text = spannable
        textView.textSize = 18f
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        val scrollView = ScrollView(context)
        scrollView.addView(textView)

        wordSelectionLayout.removeAllViews()
        wordSelectionLayout.addView(scrollView)

        val processSelectionButton = view?.findViewById<Button>(R.id.process_selection_button)
        processSelectionButton?.visibility = View.VISIBLE
        processSelectionButton?.setOnClickListener {
            processSelectedWords(text)
        }
    }

    private fun updateWordSpans(spannable: SpannableString, word: String, start: Int, end: Int) {
        if (start >= 0 && end <= spannable.length && start < end) {
            val isSelected = selectedWords.contains(word)
            val backgroundColor = if (isSelected) Color.LTGRAY else Color.TRANSPARENT

            spannable.setSpan(BackgroundColorSpan(backgroundColor), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun toggleWordSelection(word: String) {
        if (selectedWords.contains(word)) {
            selectedWords.remove(word)
        } else {
            selectedWords.add(word)
        }
        val textView = (wordSelectionLayout.getChildAt(0) as? ScrollView)?.getChildAt(0) as? TextView
        textView?.let {
            val spannable = SpannableString(it.text)
            val words = it.text.split(" ")
            var start = 0
            for (currentWord in words) {
                val end = start + currentWord.length
                if (currentWord == word) {
                    updateWordSpans(spannable, currentWord, start, end)
                }
                start = end + 1
            }
            it.text = spannable
        }
    }

    private fun processSelectedWords(originalText: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val processedText = processText(originalText)
            withContext(Dispatchers.Main) {
                val arrow = "\u2193"
                val formattedText = "$originalText\n\n$arrow\n\n$processedText"

                resultText.text = formattedText
            }
        }
    }

    private suspend fun processText(text: String): String {
        val words = text.split(" ")
        val resultWords = Array(words.size) { "" }
        val wordMap = mutableMapOf<String, Int>()

        // Recolectar las palabras con sus índices
        val jobs = words.mapIndexed { index, word ->
            CoroutineScope(Dispatchers.IO).launch {
                val lowerCaseWord = word.lowercase()
                val resultWord = if (selectedWords.contains(word)) {
                    val replacedWord = asyncFetchAndReplace(lowerCaseWord) ?: lowerCaseWord
                    replaceWithOriginalCase(word, replacedWord)
                } else {
                    word
                }
                synchronized(resultWords) {
                    resultWords[index] = resultWord
                }
            }
        }

        jobs.forEach { it.join() }

        return resultWords.joinToString(" ")
    }

    private fun replaceWithOriginalCase(originalWord: String, replacedWord: String): String {
        return when {
            originalWord.isUpperCase() -> replacedWord.uppercase()
            originalWord.isCapitalized() -> replacedWord.replaceFirstChar { it.uppercaseChar() }
            else -> replacedWord
        }
    }

    private fun String.isUpperCase(): Boolean {
        return this == this.uppercase()
    }

    private fun String.isCapitalized(): Boolean {
        return this.isNotEmpty() && this[0].isUpperCase() && this.substring(1) == this.substring(1).lowercase()
    }

    private suspend fun asyncFetchAndReplace(word: String): String? {
        return withContext(Dispatchers.IO) {
            val connection: Connection? = try {
                DBUtils.getConnection("s7")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }

            connection?.use { conn ->
                findAndReplaceWord(conn, word)
            }
        }
    }

    private fun findAndReplaceWord(connection: Connection, word: String): String? {
        val query = "SELECT id, palabra FROM sustantivos WHERE LOWER(palabra) = ?"
        val stmt: PreparedStatement = connection.prepareStatement(query)
        stmt.setString(1, word.lowercase())

        val resultSet: ResultSet = stmt.executeQuery()

        if (resultSet.next()) {
            val id = resultSet.getInt("id")
            val nextId = id + 7
            val nextQuery = "SELECT palabra FROM sustantivos WHERE id = ?"
            val nextStmt: PreparedStatement = connection.prepareStatement(nextQuery)
            nextStmt.setInt(1, nextId)

            val nextResultSet: ResultSet = nextStmt.executeQuery()
            if (nextResultSet.next()) {
                return nextResultSet.getString("palabra")
            }
        }

        return null
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
