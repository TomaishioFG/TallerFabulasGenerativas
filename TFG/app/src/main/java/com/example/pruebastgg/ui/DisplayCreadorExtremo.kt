package com.example.pruebastgg.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.pruebastgg.DBUtils
import com.example.pruebastgg.R
import com.example.pruebastgg.databinding.FragmentCreadorBinding
import java.sql.SQLException

class DisplayCreadorExtremo : Fragment() {

    private lateinit var buttonGenerate: Button
    private lateinit var linearLayoutPersona: LinearLayout
    private lateinit var linearLayoutGenero: LinearLayout
    private lateinit var linearLayoutBotones: LinearLayout
    private lateinit var infoButton: ImageView
    private val tiposDeTrama = listOf(
        "Búsqueda", "Rescate", "Pobreza a riqueza", "Renacimiento",
        "Superando al monstruo", "Tragedia", "Viaje ida y vuelta"
    )
    private val tramaAbreviaciones = mapOf(
        "Búsqueda" to "Busqu",
        "Rescate" to "Resca",
        "Pobreza a riqueza" to "Pobre",
        "Renacimiento" to "Renac",
        "Superando al monstruo" to "Monst",
        "Tragedia" to "Tra",
        "Viaje ida y vuelta" to "Viaje"
    )

    private val tramaBD = mapOf(
        "Búsqueda" to "búsqueda",
        "Rescate" to "rescate",
        "Pobreza a riqueza" to "pobreza",
        "Renacimiento" to "renacimiento",
        "Superando al monstruo" to "monstruo",
        "Tragedia" to "tragedia",
        "Viaje ida y vuelta" to "viaje"
    )

    private val labels = listOf("Terror", "Romance", "Aventuras")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_creador_extremo, container, false)

        linearLayoutGenero = view.findViewById(R.id.linearLayoutGenero)
        linearLayoutPersona = view.findViewById(R.id.linearLayoutPersona)
        linearLayoutBotones = view.findViewById(R.id.linearLayoutBotones)
        buttonGenerate = view.findViewById(R.id.buttonGenerate)
        infoButton = view.findViewById(R.id.infoButton)

        val radioGroupGenero = createRadioGroup(listOf("Fantasía", "Sci-Fi"))
        val radioGroupPersona = createRadioGroup(listOf("Primera Persona", "Segunda Persona", "Tercera Persona"))
        linearLayoutGenero.addView(radioGroupGenero)
        linearLayoutPersona.addView(radioGroupPersona)

        setupButtons()

        radioGroupGenero.setOnCheckedChangeListener { _, checkedId ->
            val selectedGenero = (requireView().findViewById<RadioButton>(checkedId)).text.toString()
            updateTramasForGenero(getTramasForGenero(selectedGenero))
        }

        buttonGenerate.setOnClickListener {
            val persona = getSelectedPersona()
            val genero = getSelectedGenero()
            val baseDeDatos = getSelectedBaseDeDatos()
            if (persona.isNotEmpty() && genero.isNotEmpty() && baseDeDatos.isNotEmpty()) {
                val consultasSQL = generarConsultasSQL(persona, genero)
                Log.d("DisplayCreadorExtremo", "Consulta SQL generada: $consultasSQL")
                fetchRandomPhrases(baseDeDatos, consultasSQL)
            } else {
                Toast.makeText(context, "Por favor, seleccione todas las opciones", Toast.LENGTH_SHORT).show()
            }
        }

        infoButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(Html.fromHtml("¿Qué ofrece el modo <b>Creador Extremo</b>?", Html.FROM_HTML_MODE_COMPACT))
            builder.setMessage(
                Html.fromHtml(
                    "<br>Los modos Creador y Azar generan historias de 20 frases, cada uno a su manera." +
                            "<br>El modo <b>Creador Extremo</b> ofrece la posibilidad de elegir el género secundario de cada una de las frases" +
                            ", así como de elegir, para cada oración, el tipo de trama. " +
                            "<br>Ofrece una posibilidad de personalización mayor, sin espacio alguno para el azar.",
                    Html.FROM_HTML_MODE_COMPACT
                )
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }

        return view
    }

    private fun createRadioGroup(labels: List<String>): RadioGroup {
        val radioGroup = RadioGroup(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                orientation = RadioGroup.HORIZONTAL
            }
        }
        for (label in labels) {
            val radioButton = RadioButton(context).apply {
                text = label
                setBackgroundResource(R.drawable.custom_radio_button_background)
                gravity = android.view.Gravity.CENTER
                buttonDrawable = null
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
                ).apply {
                    setMargins(8, 8, 8, 8)
                }
            }
            radioGroup.addView(radioButton)
        }
        return radioGroup
    }

    private fun createSpinner(labels: List<String>): Spinner {
        val spinner = Spinner(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 8, 8, 8)
            }
            adapter = CustomArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, labels).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
        }
        return spinner
    }

    private fun getSelectedPersona(): String {
        val radioGroup = linearLayoutPersona.getChildAt(0) as RadioGroup
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val selectedRadioButton = requireView().findViewById<RadioButton>(selectedId)
            when (selectedRadioButton.text.toString()) {
                "Primera Persona" -> "primera"
                "Segunda Persona" -> "segunda"
                "Tercera Persona" -> "tercera"
                else -> ""
            }
        } else {
            ""
        }
    }

    private fun getSelectedGenero(): String {
        val radioGroup = linearLayoutGenero.getChildAt(0) as RadioGroup
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val selectedRadioButton = requireView().findViewById<RadioButton>(selectedId)
            when (selectedRadioButton.text.toString()) {
                "Fantasía" -> "fantasia"
                "Sci-Fi" -> "cienciaficcion"
                //"Western" -> "western"
                //"Piratas" -> "piratas"
                else -> ""
            }
        } else {
            ""
        }
    }

    private fun getSelectedBaseDeDatos(): String {
        val radioGroup = linearLayoutGenero.getChildAt(0) as RadioGroup
        val selectedId = radioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val selectedRadioButton = requireView().findViewById<RadioButton>(selectedId)
            when (selectedRadioButton.text.toString()) {
                "Fantasía" -> "fantasia"
                "Sci-Fi" -> "cienciaficcion"
                //"Western" -> "western"
                //"Piratas" -> "piratas"
                else -> ""
            }
        } else {
            ""
        }
    }

    private fun setupButtons() {
        for (i in 1..20) {
            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
            }

            val spinnerTrama = createSpinner(tiposDeTrama).apply {
                setSelection(0)
            }
            val radioGroupGenero = createRadioGroup(labels)

            layout.addView(spinnerTrama)
            layout.addView(radioGroupGenero)

            linearLayoutBotones.addView(layout)
        }
    }

    private fun getTramasForGenero(genero: String): List<String> {
        return when (genero) {
            "Fantasía" -> listOf("Búsqueda", "Rescate")
            "Sci-Fi" -> listOf("Superando al monstruo")
            //"Western" -> listOf("Superando al monstruo", "Tragedia")
            "//Piratas" -> listOf("Viaje ida y vuelta", "Rescate")
            else -> tiposDeTrama
        }
    }

    private fun updateTramasForGenero(tramas: List<String>) {
        // Update all spinners in linearLayoutBotones
        for (i in 0 until linearLayoutBotones.childCount) {
            val rowLayout = linearLayoutBotones.getChildAt(i) as LinearLayout
            val spinnerTrama = rowLayout.getChildAt(0) as Spinner
            val adapter = CustomArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tramas).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spinnerTrama.adapter = adapter
        }
    }

    private fun generarConsultasSQL(persona: String, generoSecundario: String): String {
        val consultas = StringBuilder()
        for (i in 0 until linearLayoutBotones.childCount) {
            val rowLayout = linearLayoutBotones.getChildAt(i) as LinearLayout
            val spinnerTrama = rowLayout.getChildAt(0) as Spinner
            val radioGroupGenero = rowLayout.getChildAt(1) as RadioGroup

            val selectedTramaCompleta = spinnerTrama.selectedItem.toString()
            val selectedTramaBD = tramaBD[selectedTramaCompleta] ?: selectedTramaCompleta.lowercase()
            val selectedGeneroButtonId = radioGroupGenero.checkedRadioButtonId
            val selectedGeneroButton = if (selectedGeneroButtonId != -1) {
                requireView().findViewById<RadioButton>(selectedGeneroButtonId)
            } else {
                null
            }

            val selectedGenero = selectedGeneroButton?.text?.toString()?.lowercase() ?: ""
            if (selectedGenero.isEmpty()) {
                Log.e("DisplayCreadorExtremo", "No se seleccionó ningún género para el número $i")
                continue
            }

            consultas.append("(SELECT frase FROM $selectedGenero ")
                .append("WHERE numero = ").append(i + 1)
                .append(" AND trama = '").append(selectedTramaBD)
                .append("' AND persona = '").append(persona).append("') ")

            if (i < linearLayoutBotones.childCount - 1) {
                consultas.append("UNION ALL ")
            }
        }
        return consultas.toString()
    }


    private fun fetchRandomPhrases(baseDeDatos: String, consultasSQL: String) {
        Thread {
            try {
                val conn = DBUtils.getConnection(baseDeDatos)
                if (conn != null) {
                    val sql = consultasSQL
                    Log.d("DisplayCreadorExtremo", "Ejecutando consulta SQL: $sql")

                    val statement = conn.prepareStatement(sql)
                    val resultSet = statement.executeQuery()
                    val frases = StringBuilder()
                    while (resultSet.next()) {
                        val frase = resultSet.getString("frase")
                        frases.append(frase).append("\n")
                    }
                    resultSet.close()
                    statement.close()
                    conn.close()

                    requireActivity().runOnUiThread {
                        mostrarResultado(frases.toString())
                    }
                } else {
                    Log.e("DisplayCreadorExtremo", "Conexión a la base de datos fallida.")
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun mostrarResultado(resultado: String) {
        val intent = Intent(requireContext(), DisplayCreadorHistoriaExtrema::class.java)
        intent.putExtra("STORY_TEXT", resultado)
        startActivity(intent)
    }

    inner class CustomArrayAdapter(context: android.content.Context, resource: Int, objects: List<String>)
        : ArrayAdapter<String>(context, resource, objects) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            val tramaCompleta = getItem(position)
            val abreviatura = tramaAbreviaciones[tramaCompleta] ?: ""
            (view as TextView).text = abreviatura
            return view
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getDropDownView(position, convertView, parent)
            (view as TextView).text = getItem(position)
            return view
        }
    }
}