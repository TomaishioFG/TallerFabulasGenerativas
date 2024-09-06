package com.example.pruebastgg.ui.ui.cajon

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pruebastgg.DBUtils
import com.example.pruebastgg.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class DisplayIariom : Fragment() {

    private lateinit var textView: TextView
    private lateinit var continueButton: Button
    private lateinit var controlsLayout: LinearLayout
    private lateinit var bottomImageView: ImageView
    private lateinit var scrollView: ScrollView
    private var currentScene: Int = 1
    private var optionButtons: List<Button> = emptyList()
    private var playerChoice: String? = null
    private var firstChoice: String? = null
    private var lastInsertedId: Int = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_iariom, container, false)

        textView = view.findViewById(R.id.storyTextView)
        continueButton = view.findViewById(R.id.continueButton)
        controlsLayout = view.findViewById(R.id.controlsLayout)
        bottomImageView = view.findViewById(R.id.bottomImageView)
        scrollView = view.findViewById(R.id.scrollView)

        continueButton.setOnClickListener {
            nextScene()
        }

        loadScene(currentScene)

        return view
    }

    private fun nextScene() {
        currentScene++
        loadScene(currentScene)
    }
    private fun loadScene(scene: Int) {
        clearScene()

        when (scene) {
            1 -> {
                textView.text = "Todo el pueblo está profundamente preocupado. La actividad pesquera ha cesado, y todos están buscando, a tiempo completo, a las jóvenes que desaparecieron hace ya dos días." +
                        "\nLa gente rumorea por todas partes lo que ha podido ocurrir. " +
                        "\n—Yo las vi con un libro de esos de cosas oscuras —dice una señora muy mayor, que se pasaba el día en la plaza del pequeño pueblo, que veía todos los días a las jóvenes chismear—. Hace días que no hablaban de mozos, como hacían antes. Ahora se escondían más, ocultando sus intenciones. Os digo yo que esas muchachas no están bien." +
                        "\n\n La gente se ha dividido en varios grupos de búsqueda: algunos recorren las playas, casi sin pausas, otros, han subido a las embarcaciones, con miedo de que hayan podido irse a nado, y no hayan podido regresar por las mareas, otros peinan los bosques de alrededor, con la esperanza de encontrarlas con vida." +
                        "\n Y después estás tú: tú eres un gran conocedor de las montañas cercanas, de las cuevas que las atraviesan, de los lugares que el resto de gente no se atreve a visitar por miedo a las antiguas leyendas, leyendas de las que tú, personalmente, no te crees nada." +
                        " Nunca has visto allí nada especialmente alarmante: pequeños huesos de animales, el resultado de las cacerías de algunos de los felinos que utilizan las galerías como hogar, pero desde luego, no has visto nada turbio; no has visto rituales, no has visto" +
                        " presencias, no has visto nada, nunca. \n Pero precisamente, por eso mismo, te toca volver, solo, pues nadie quiere ir contigo; todos tienen miedo, y más ahora, que la sensación de alerta y peligro está tan a flor de piel. Ahora hasta tú tienes algo de respeto por lo que pueda haber allí dentro."
                continueButton.visibility = View.VISIBLE
                bottomImageView.setImageResource(R.drawable.escene1)
                addViewSafely(continueButton)
                scrollView.scrollTo(0, 0)
            }
            2 -> {
                textView.text = "Allá vas. Te calzas, coges tus bártulos, y te pones en camino. Vas a la montaña y, por el camino, te para uno de los vecinos." +
                        "\n—¿A buscar a las chicas? —te pregunta, claramente confundido y preocupado." +
                        "\n—Así es —contestaste, cortante, sin querer perder mucho tiempo hablando con él. Todo el tiempo era crucial: cuanto más tiempo pasaba, más probable era que a las jóvenes les pasara algo." +
                        "\n—Ten mucho cuidado, por favor. Ya sabes lo que dicen de allí arriba... Juraría, además, haber visto subir a alguien más." +
                        "\n\nTe despediste de él; no tenía sentido que mintiese con aquello, pero poca gente se atrevería a subir allí, lo que te preocupó más todavía, haciendo que acelerases el paso, hasta llegar a la entrada principal a las grutas." +
                        " Allí, sacas el farolillo, algo indispensable para cualquiera que entre en esa zona; lo enciendes, suspiras fuertemente y entras. «Recuerda, lo haces por ellas», te dices, poco convencido."
                continueButton.visibility = View.VISIBLE
                bottomImageView.setImageResource(R.drawable.escene2)
                addViewSafely(continueButton)
                scrollView.scrollTo(0, 0)
            }
            3 -> {
                continueButton.visibility = View.GONE
                loadConversation()
                scrollView.scrollTo(0, 0)
            }
            4 -> {
                textView.text = "No sabes si has hecho lo correcto, pero no puedes darle más vueltas. Si las jóvenes están ahí y necesitan tu ayuda, debes apresurarte." +
                        " Corres, recorriendo el largo y claustrofóbico pasillo, hasta que te encuentras con una gran cueva, y allí las ves." +
                        "Allí están las jóvenes, todas ataviadas con largos y sencillos, pero preciosos, vestidos blancos, complementados con unos sombreros de paja blancos y manchadas, todas ellas, de sangre. Estaban tiradas en el suelo, como si estuviesen durmiendo, plácidamente, pero algo dentro de ti te decía que no estaban dormidas, aunque su serena expresión dijese lo contrario." +
                        "\nTe fijaste en la distribución: estaban alrededor de una de ellas; la que parecía la más dulce y más pura de todas, estaba estirada, con más sangre por encima que las demás. Estaba atada, de pies y manos, con las manos descansándole sobre la parte baja del vientre, sobre un libro, de tapa negra. A su alrededor, un circulo de piedras, y sus amigas, inconscientes o muertas." +
                        "\n\nCuando te acercas a ella, abre los ojos: solo se ve una línea de color azul muy claro, que rodea a la pupila, negra como el abismo, que ocupa la mayor parte de los ojos, que miran hacia arriba, fíjamente." +
                        "\n—¿Estás bien? —preguntas, con voz temblorosa, aunque es una pregunta retórica, pues claramente, la joven está muy herida, necesita atención urgente." +
                        "\n—Aléjate de ellas —dice la joven, con una voz grave, que no se corresponde al cuerpo de la joven, que retumba en tus oídos." +
                        "\nRecuerdas lo que dijo la anciana: esas niñas habían estado jugando con algo oscuro." +
                        "\n—He venido a ayudar, voy a liberarte —dices." +
                        "\n—Mátame... —dice la niña, esta vez con un hilo de su tierna vocecilla—. El ritual no debe ser completado, ha sido un error —termina, girando la cabeza hacia sus compañeras, y emitiendo un grito, de dolor, un grito de auxilio." +
                        "\nTodo en la habitación empieza a temblar: un fuerte olor a azufre inunda la sala. Tú no crees en esas cosas, pero sabes que algo malo va a pasar. Debes tomar una decisión, y debes tomarla ya." +
                        " Sacas el cuchillo, y tu mente empieza a bailar: no sabes si debes hacerle caso y acabar con ella o liberarla y buscar ayuda para ella y las demás. "
                showOptions("Liberarla y buscar ayuda", "Acabar con su sufrimiento")
                bottomImageView.setImageResource(R.drawable.escene4)
                scrollView.scrollTo(0, 0)
            }
            5 -> {
                textView.text = when (playerChoice) {
                    "Liberarla y buscar ayuda" -> "Haces caso omiso a su petición: no te sientes con el derecho de quitarle la vida. Cortas las cuerdas, te guardas el cuchillo en la funda, en el cinturón, y ayudas a la joven a incorporarse." +
                            "Ella, se abraza a ti; sientes que has hecho lo correcto, sientes sus brazos, débiles pero agradecidos, alrededor de tu cintura. Os quedáis abrazados un breve instante, que se siente eterno." +
                            "\nCuando se separa de ti, te giras, para intentar despertar a las otras chicas, si es que están dormidas. Y cuando te giras, escuchas con horror a tus espaldas, unos gritos ahogados, acompañados de inconfundible gorgoteo de la sangre." +
                            "\nVes a la joven, con tu cuchillo en el cuello, tras quitarse la vida. La abrazas de nuevo, mientras lloras, y te empapas de tu lágrimas y su sangre. Y tras otro largo rato abrazados, cuando no puedes derramar más lágrimas, dejas allí su cuerpo, y sales" +
                            " de allí, buscando ayuda para las otras jóvenes, que siguen tiradas, como si el tiempo se hubiese detenido a su alrededor."
                    "Acabar con su sufrimiento" -> "Decides que lo correcto es acabar con su sufrimiento. Te lo ha pedido, y tú has visto lo mal que está. Te convences a ti mismo, pues en el fondo no crees que sea lo correcto, pero te convences, diciéndote que, aunque la salvases, probablemente no sería capaz de salir con vida de allí. " +
                            "Cierras los ojos, y empuñando tu cuchillo, se lo clavas directamente en el corazón. La sangre sale a borbotones, manchando tus ropas, manchando más el suelo, manchándolo todo a vuestro alrededor. El suelo empieza a temblar de nuevo, con más intensidad." +
                            "Durante unos segundos, no puedes ni moverte. Estás paralizado por lo que acabas de hacer, y la situación te supera, pero una mirada a tu alrededor te reactiva: aun hay más chicas, que siguen tiradas. Quizás a ellas sí puedas ayudarlas." +
                            "\nTe levantas y sales de allí, sin mirar atrás, para no tener que ver el crimen que acabas de cometer."
                    else -> "Escena 5: [Texto explicando la historia...]"
                }
                continueButton.visibility = View.VISIBLE
                addViewSafely(continueButton)
                bottomImageView.setImageResource(R.drawable.escene5)
                scrollView.scrollTo(0, 0)
            }
            6 -> {
                clearScene()
                textView.text = "Recorres el camino inverso. Vuelves por el mismo pasillo, que si a la ida te había parecido largo, ahora parece que no se va a acabar." +
                        " Pero a medio camino, empiezas a escuchar pasos, y te cruzas con un hombre, de tu misma estatura. El hombre, visiblemente asustado, te interpela:"
                showTextInputOptions()
                bottomImageView.setImageResource(R.drawable.placeholder)
                continueButton.visibility = View.GONE
                bottomImageView.setImageResource(R.drawable.escene6)
                scrollView.scrollTo(0, 0)
            }
            7 -> {
                clearScene()
                textView.text = "Y aquí es donde acaba tu historia. Al menos por ahora. \nTu final depende del siguiente jugador: en tu conversación con el hombre extraño del túnel, tú has leído las respuestas del anterior jugador, y, sin saberlo, has decidido si le creías o no, si merecía vivir o no. \nAhora, el siguiente jugador elegirá si creerte, si vives o mueres, como tú has elegido por el jugador anterior. \nTu destino está en sus manos.\n\n"
                showNameAndEmailInput()
                //bottomImageView.visibility = View.GONE
                bottomImageView.setImageResource(R.drawable.escene7)
                continueButton.visibility = View.GONE
                scrollView.scrollTo(0, 0)
            }
            8 -> {
                textView.text = "Fin.\nGracias por jugar."
                continueButton.visibility = View.GONE
                bottomImageView.setImageResource(R.drawable.escene8)
            }
        }
    }

    private fun clearScene() {
        controlsLayout.removeAllViews()
        optionButtons.forEach { it.visibility = View.GONE }
        optionButtons = emptyList()
        (view?.findViewById<ImageView>(R.id.bottomImageView))?.setVisibility(View.VISIBLE)
    }

    private fun addViewSafely(view: View) {
        if (view.parent == null) {
            controlsLayout.addView(view)
        }
    }

    private fun loadConversation() {
        CoroutineScope(Dispatchers.IO).launch {
            val connection: Connection? = DBUtils.getConnection("iariom")

            try {
                val query = """
                    SELECT frase, pregunta FROM juego 
                    WHERE IDjuego = (SELECT MAX(IDjuego) FROM juego) 
                    ORDER BY pregunta DESC 
                    LIMIT 3
                """
                val preparedStatement: PreparedStatement? = connection?.prepareStatement(query)
                val resultSet: ResultSet? = preparedStatement?.executeQuery()

                val responses = mutableListOf<String>()
                while (resultSet?.next() == true) {
                    responses.add(resultSet.getString("frase"))
                }

                responses.reverse()

                val dialogText = """
                    Avanzas por el laberíntico interior de las cuevas; las conoces bien, pero no sabes a dónde dirigirte, porque no sabes por dónde podría haber ido un inexperto e imprudente grupo de adolescentes.
                    Empiezas a agobiarte, el aire se vuelve más pesado sobre ti y te cuesta respirar; sigues avanzando, recordándote a ti mismo que lo estás haciendo porque hay familias preocupadas, y que tú eres su única esperanza, si es que están allí dentro.
                    Recorres varios de los túneles, y empiezas a escuchar algo más de jaleo, más ruidos de los que acostumbras a escuchar ahí dentro. Crees haber escuchado gritos, de una chica, pero ya no sabes si es tu imaginación o si verdaderamente lo has escuchado. Te asustas, y sacas el cuchillo que llevas contigo, pues te da algo más de seguridad, pero se te pasa pronto; escuchas pasos, que se acercan, y se acercan, y se acercan.
                    Por el corredor que estás recorriendo aparece de frente un hombre, de tu estatura. Con la luz de tu lámpara no se termina de apreciar muy bien su rostro, pero sí ves algo preocupante en él: está cubierto de sangre, y lleva un cuchillo en la mano.
                    — He escuchado gritos. ¿Qué ha pasado? —le preguntas.
                    — ${responses.getOrNull(0) ?: "No hay respuesta"}
                    — ¿Por qué llevas un cuchillo? —sigues interrogándole, más nervioso, si cabe.
                    — ${responses.getOrNull(1) ?: "No hay respuesta"}
                    — ¿Y por qué estás manchado de sangre?
                    — ${responses.getOrNull(2) ?: "No hay respuesta"}
                    
                    No sabes si creerle, no sabes si dice o no dice la verdad, no puedes saber qué ha ocurrido. Debes tomar una decisión.
                """.trimIndent()
                bottomImageView.setImageResource(R.drawable.escene3)
                withContext(Dispatchers.Main) {
                    textView.text = dialogText
                    showOptions("Dejarle pasar", "Matarlo")
                }

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                connection?.close()
            }
        }
    }

    private fun showOptions(option1: String, option2: String) {
        continueButton.visibility = View.GONE

        val button1 = Button(requireContext()).apply {
            text = option1
            setOnClickListener {
                if (firstChoice == null) {
                    firstChoice = when (option1) {
                        "Dejarle pasar" -> "Dejó pasar"
                        "Matarlo" -> "Matar"
                        else -> null
                    }
                }
                playerChoice = option1
                clearScene()
                nextScene()
            }
        }

        val button2 = Button(requireContext()).apply {
            text = option2
            setOnClickListener {
                if (firstChoice == null) {
                    firstChoice = when (option2) {
                        "Dejarle pasar" -> "Dejó pasar"
                        "Matarlo" -> "Matar"
                        else -> null
                    }
                }
                playerChoice = option2
                clearScene()
                nextScene()
            }
        }

        optionButtons = listOf(button1, button2)
        addViewSafely(button1)
        addViewSafely(button2)
    }

    private fun showTextInputOptions() {
        val layout = view?.findViewById<LinearLayout>(R.id.controlsLayout)
        layout?.removeAllViews()

        val textView1 = TextView(requireContext()).apply {
            text = "—He escuchado gritos. ¿Qué ha pasado? —te pregunta."
            setBackgroundColor(Color.parseColor("#f0f0f0"))
            setTextColor(Color.BLACK)
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        val editText1 = EditText(requireContext()).apply {
            hint = "Escribe aquí tu respuesta"
        }

        val textView2 = TextView(requireContext()).apply {
            text = "— ¿Por qué llevas un cuchillo? —te cuestiona, de nuevo, visiblemente nervioso."
            setBackgroundColor(Color.parseColor("#f0f0f0"))
            setTextColor(Color.BLACK)
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        val editText2 = EditText(requireContext()).apply {
            hint = "Escribe aquí tu respuesta"
        }

        val textView3 = TextView(requireContext()).apply {
            text = "—¿Y por qué estás manchado de sangre?"
            setBackgroundColor(Color.parseColor("#f0f0f0"))
            setTextColor(Color.BLACK)
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        val editText3 = EditText(requireContext()).apply {
            hint = "Escribe aquí tu respuesta"
        }

        val submitButton = Button(requireContext()).apply {
            text = "Continuar"
            isEnabled = false
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                submitButton.isEnabled = editText1.text.isNotEmpty() &&
                        editText2.text.isNotEmpty() &&
                        editText3.text.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        editText1.addTextChangedListener(textWatcher)
        editText2.addTextChangedListener(textWatcher)
        editText3.addTextChangedListener(textWatcher)

        submitButton.setOnClickListener {
            if (editText1.text.isNotEmpty() && editText2.text.isNotEmpty() && editText3.text.isNotEmpty()) {
                savePlayerResponses(editText1.text.toString(), editText2.text.toString(), editText3.text.toString())
                nextScene()
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        layout?.apply {
            addView(textView1)
            addView(editText1)
            addView(textView2)
            addView(editText2)
            addView(textView3)
            addView(editText3)
            addView(submitButton)
        }
    }


    private fun showNameAndEmailInput() {
        val nameTextView = TextView(requireContext()).apply {
            text = "Introduce tu nombre:"
            setBackgroundColor(Color.parseColor("#f0f0f0"))
            setTextColor(Color.BLACK)
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        val nameEditText = EditText(requireContext()).apply {
            hint = "Escribe aquí tu nombre"
        }

        val emailTextView = TextView(requireContext()).apply {
            text = "Introduce tu correo si quieres conocer tu destino:"
            setBackgroundColor(Color.parseColor("#f0f0f0"))
            setTextColor(Color.BLACK)
            textSize = 18f
            setPadding(16, 16, 16, 16)
        }
        val emailEditText = EditText(requireContext()).apply {
            hint = "Escribe aquí tu correo"
        }

        val finishButton = Button(requireContext()).apply {
            text = "Enviar"
            isEnabled = false // Inicialmente deshabilitado
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                finishButton.isEnabled = nameEditText.text.isNotEmpty() && emailEditText.text.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        nameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)

        finishButton.setOnClickListener {
            if (nameEditText.text.isNotEmpty() && emailEditText.text.isNotEmpty()) {
                savePlayerNameAndEmail(nameEditText.text.toString(), emailEditText.text.toString())
                nextScene()
            } else {
                Toast.makeText(requireContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        addViewSafely(nameTextView)
        addViewSafely(nameEditText)
        addViewSafely(emailTextView)
        addViewSafely(emailEditText)
        addViewSafely(finishButton)
    }



    private fun savePlayerResponses(response1: String, response2: String, response3: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection: Connection? = DBUtils.getConnection("iariom")

            try {
                val query = "INSERT INTO juego (IDjuego, frase, pregunta, eleccion) VALUES (?, ?, ?, ?)"
                val maxIdQuery = "SELECT MAX(IDjuego) FROM juego"

                val maxIdStatement = connection?.createStatement()
                val resultSet = maxIdStatement?.executeQuery(maxIdQuery)
                var newId = 1
                if (resultSet?.next() == true) {
                    newId = resultSet.getInt(1) + 1
                }
                lastInsertedId = newId

                val preparedStatement: PreparedStatement? = connection?.prepareStatement(query)

                preparedStatement?.apply {
                    setInt(1, newId)
                    setString(2, response1)
                    setInt(3, 1)
                    setString(4, firstChoice ?: "")
                    executeUpdate()

                    setString(2, response2)
                    setInt(3, 2)
                    setString(4, firstChoice ?: "")
                    executeUpdate()

                    setString(2, response3)
                    setInt(3, 3)
                    setString(4, firstChoice ?: "")
                    executeUpdate()
                }


                triggerUpdateDecision(newId - 1)

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                connection?.close()
            }
        }
    }

    private fun triggerUpdateDecision(previousId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection: Connection? = DBUtils.getConnection("iariom")

            try {
                val decision = when (firstChoice) {
                    "Matar" -> "Murió"
                    "Dejó pasar" -> "Vivió"
                    else -> null
                }

                decision?.let {
                    val updateQuery = "UPDATE juego SET decision = ? WHERE IDjuego = ?"
                    val preparedStatement: PreparedStatement? = connection?.prepareStatement(updateQuery)

                    preparedStatement?.apply {
                        setString(1, it)
                        setInt(2, previousId)
                        executeUpdate()
                    }
                }

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                connection?.close()
            }
        }
    }


    private fun savePlayerNameAndEmail(name: String, email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val connection: Connection? = DBUtils.getConnection("iariom")

            try {
                val query = "UPDATE juego SET nombre = ?, correo = ? WHERE IDjuego = ?"
                val preparedStatement: PreparedStatement? = connection?.prepareStatement(query)

                preparedStatement?.apply {
                    setString(1, name)
                    setString(2, email)
                    setInt(3, lastInsertedId)
                    executeUpdate()
                }

            } catch (e: SQLException) {
                e.printStackTrace()
            } finally {
                connection?.close()
            }
        }
    }
}