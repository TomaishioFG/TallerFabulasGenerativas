package com.example.pruebastgg.ui.ui.cajon

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.pruebastgg.R

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

import android.os.VibrationEffect
import android.os.Vibrator


data class Casilla(
    val id: String,
    val puertaIzquierda: String?,
    val puertaDerecha: String?,
    val historia: String,
    val esFinal: Boolean = false
)

object Tablero {
    val casillas: Map<String, Casilla> = mapOf(
        "A" to Casilla("A", "B", "C",
            "Te despiertas, aturdido, en una sala, con el suelo blanco. Esta situación te suena, crees que no es la primera vez que estás ahí. Estás sentado, con la espalda apoyada en una esquina, con cada hombro tocando una pared distinta." +
                    "Miras tus manos, confuso, viendo que tienes una brújula, y, como estás en una sala perfectamente cuadrada, te das cuenta que tienes el norte al frente, justo en la esquina opuesta a la tuya. " +
                    "Ves, en cada una de las paredes que convergen en la esquina norte, una puerta. Decides tomar una de ellas. \n¿Eliges irte por la derecha o por la izquierda?"),

        "B" to Casilla("B", "D", "E",
            "Entras en una sala, casi idéntica, aunque esta tiene el suelo negro. Sigues descolocado. ¿Qué haces allí? ¿Qué son esas salas? ¿Por qué tienes únicamente una brújula?" +
                    " Decides que no es momento para las preguntas, y que debes continuar. De nuevo, la sala tiene dos puertas, con la misma orientación. " +
                    "\n¿Eliges irte por la derecha o por la izquierda?"),
        "C" to Casilla("C", "E", "F",
            "Entras en una nueva sala, que es prácticamente idéntica, pero tiene el suelo negro. La disposición es la misma: dos puertas con la misma colocación." +
                    "Miras hacia atrás, pero la puerta por la que entraste ya no está. Solo puedes avanzar, no retroceder. Así que te planteas de nuevo." +
                    "\n¿Eliges irte por la derecha o por la izquierda? "),

        "D" to Casilla("D", "G", "H", "Llegas a una nueva sala. El suelo vuelve a ser blanco. Y vuelves a ver dos puertas, una hacia la derecha y otra hacia la izquierda." +
                " ¿Acaso esto no tiene final?¿Eres acaso fruto de algún experimento extraño? Demasiadas preguntas. Decides volver a la acción.\n" +
                "¿Eliges irte por la derecha o por la izquierda?"),
        "E" to Casilla("E", "H", "I", "Nueva sala, blanca, como la primera sala. Un blanco pulcro, sin una mota de polvo, sin rastro de nada." +
                "Solo, nuevamente, dos puertas. Empieza a frustrarte. ¿Es real esto que vives? ¿Por qué tienes la sensación de que no es la primera vez que estás aquí? ¿Por qué ahora, de repente, te ha entrado miedo?" +
                " Nuevamente, tienes que tomar una decisión, y  te asalta la duda. \n¿Eliges irte por la derecha o por la izquierda? "),
        "F" to Casilla("F", "I", "J", "Otra sala, claro. Blanca, como la primera. ¿Significaría eso algo? ¿Deberías seguir siempre hacia la derecha?" +
                " Piensas que probablemente sea la decisión más inteligente. ¿O tal vez no? ¿Tendrá la izquierda algo que ofrecerme? \n¿Eliges irte por la derecha o por la izquierda?\""),

        "G" to Casilla("G", "K", "L", "Sigues vivo."),
        "H" to Casilla("H", "L", "M", "Sigues vivo."),
        "I" to Casilla("I", "M", "N", "Lo esperado. Una nueva sala. Pero esta vez hay algo distinto. No es el suelo, que vuelve a ser negro. No. Esta vez no hay ninguna puerta." +
                "Te das la vuelta. No hay ninguna puerta, ni siquiera por la que has entrado. No hay nada. Absolutamente nada.", esFinal = true),
        "J" to Casilla("J", "N", "Ñ", "Son las XX:XX."),

        "K" to Casilla("K", "O", "P", "Sigues vivo."),
        "L" to Casilla("L", "P", "Q", "Mueres.", esFinal = true),
        "M" to Casilla("M", "Q", "R", "Sigues vivo."),
        "N" to Casilla("N", "R", "S", "Sigues vivo."),
        "Ñ" to Casilla("Ñ", "S", "T", "Sigues vivo."),

        "O" to Casilla("O", "U", "V", "Sigues vivo."),
        "P" to Casilla("P", "V", "W", "Mueres.", esFinal = true),
        "Q" to Casilla("Q", "W", "X", "Sigues vivo."),
        "R" to Casilla("R", "X", "Y", "Mueres.", esFinal = true),
        "S" to Casilla("S", "Y", "Z", "Sigues vivo."),
        "T" to Casilla("T", "Z", "AA", "Sigues vivo."),

        "U" to Casilla("U", "AB", "AC", "Sigues vivo."),
        "V" to Casilla("V", "AC", "AD", "Sigues vivo."),
        "W" to Casilla("W", "AD", "AE", "Mueres.", esFinal = true),
        "X" to Casilla("X", "AE", "AF", "Sigues vivo."),
        "Y" to Casilla("Y", "AF", "AG", "Sigues vivo."),
        "Z" to Casilla("Z", "AG", "AH", "Mueres", esFinal = true),
        "AA" to Casilla("AA", "AH", "AI", "Sigues vivo."),

        "AB" to Casilla("AB", "AO", "AJ", "Sigues vivo."),
        "AC" to Casilla("AC", "AJ", "AK", "Mueres.", esFinal = true),
        "AD" to Casilla("AD", "AK", "AL", "Mueres.", esFinal = true),
        "AE" to Casilla("AE", "AL", "AM", "Sigues vivo."),
        "AF" to Casilla("AF", "AM", "AN", "Mueres.", esFinal = true),
        "AG" to Casilla("AG", "AN", "AÑ", "Sigues vivo."),
        "AH" to Casilla("AH", "AÑ", "AO", "Sigues vivo."),
        "AI" to Casilla("AI", "AO", "AJ", "Mueres.", esFinal = true),

        "AJ" to Casilla("AJ", "AU", "AP", "Sigues vivo."),
        "AK" to Casilla("AK", "AP", "AQ", "Mueres.", esFinal = true),
        "AL" to Casilla("AL", "AQ", "AR", "Sigues vivo."),
        "AM" to Casilla("AM", "AR", "AS", "Mueres.", esFinal = true),
        "AN" to Casilla("AN", "AS", "AT", "Sigues vivo."),
        "AÑ" to Casilla("AÑ", "AT", "AU", "Mueres.", esFinal = true),
        "AO" to Casilla("AO", "AU", "AP", "Sigues vivo."),

        "AP" to Casilla("AP", "AZ", "AV", "Mueres.", esFinal = true),
        "AQ" to Casilla("AQ", "AV", "AW", "Mueres.", esFinal = true),
        "AR" to Casilla("AR", "AW", "AX", "Sigues vivo."),
        "AS" to Casilla("AS", "AX", "AY", "Mueres.", esFinal = true),
        "AT" to Casilla("AT", "AY", "AZ", "Sigues vivo."),
        "AU" to Casilla("AU", "AZ", "AV", "Sigues vivo."),

        "AV" to Casilla("AV", "BD", "BA", "Sigues vivo."),
        "AW" to Casilla("AW", "BA", "BB", "Mueres.", esFinal = true),
        "AX" to Casilla("AX", "BB", "BC", "Sigues vivo."),
        "AY" to Casilla("AY", "BC", "BD", "Mueres.", esFinal = true),
        "AZ" to Casilla("AZ", "BD", "BA", "Mueres.", esFinal = true),

        "BA" to Casilla("BA", "BG", "BE", "Mueres.", esFinal = true),
        "BB" to Casilla("BB", "BE", "BF", "Sigues vivo."),
        "BC" to Casilla("BC", "BF", "BG", "Mueres.", esFinal = true),
        "BD" to Casilla("BD", "BG", "BE", "Sigues vivo."),

        "BE" to Casilla("BE", "BI", "BH", "Sigues vivo."),
        "BF" to Casilla("BF", "BH", "BI", "Mueres.", esFinal = true),
        "BG" to Casilla("BG", "BI", "BH", "Mueres.", esFinal = true),

        "BH" to Casilla("BH", "BJ", "BJ", "Mueres.", esFinal = true),
        "BI" to Casilla("BI", "BJ", "BJ", "Sigues vivo."),

        "BJ" to Casilla("BJ", null, null, "¡LO HICISTE!", esFinal = true)
    )
}


class DisplayCuento : Fragment() {

    private lateinit var sceneTextView: TextView
    private lateinit var optionButtons: List<ImageButton>
    private var currentCasillaId: String = "A"
    private var salasVisitadas: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cuento, container, false)
        val infoButton: ImageButton = view.findViewById(R.id.infoButtonCuento)
        infoButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            val mensaje = "Número de salas visitadas: $salasVisitadas"
            builder.setTitle(Html.fromHtml("El millón de millones", Html.FROM_HTML_MODE_COMPACT))
            builder.setMessage(
                Html.fromHtml(
                    mensaje,
                    Html.FROM_HTML_MODE_COMPACT
                )
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }
        sceneTextView = view.findViewById(R.id.sceneTextView)
        optionButtons = listOf(
            view.findViewById<ImageButton>(R.id.optionButton1),
            view.findViewById<ImageButton>(R.id.optionButton2)
        )

        loadScene(currentCasillaId)

        return view
    }
    private fun obtenerHoraActual(): String {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun obtenerNivelBateria(): Int {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = requireContext().registerReceiver(null, intentFilter)
        val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1

        return if (level != -1 && scale != -1) {
            (level * 100) / scale
        } else {
            -1 // Error
        }
    }

    private fun vibrarDispositivo() {
        val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(3000) // Vibrar por 3 segundos
        }
    }

    private fun loadScene(casillaId: String) {
        val casilla = Tablero.casillas[casillaId] ?: return

        var texto = casilla.historia.replace("XX:XX", obtenerHoraActual())

        val nivelBateria = obtenerNivelBateria()
        texto = texto.replace("XX%", "$nivelBateria%")

        if (casillaId == "J") {
            vibrarDispositivo()
        }
        texto = "$texto"

        if (casilla.esFinal) {
            texto += "\n\nFIN"
            optionButtons.forEach { it.visibility = View.GONE }
        } else {
            optionButtons[0].setOnClickListener {
                currentCasillaId = mover(casillaId, 1) ?: currentCasillaId
                salasVisitadas++
                loadScene(currentCasillaId)
            }

            optionButtons[1].setOnClickListener {
                currentCasillaId = mover(casillaId, 2) ?: currentCasillaId
                salasVisitadas++
                loadScene(currentCasillaId)
            }

            optionButtons.forEach { it.visibility = View.VISIBLE }
        }

        sceneTextView.text = texto
    }


    private fun obtenerPuertasDisponibles(casillaId: String): Pair<String?, String?> {
        val casilla = Tablero.casillas[casillaId] ?: return null to null
        return casilla.puertaIzquierda to casilla.puertaDerecha
    }

    private fun mover(casillaIdActual: String, eleccion: Int): String? {
        val (izquierda, derecha) = obtenerPuertasDisponibles(casillaIdActual)
        return when (eleccion) {
            1 -> izquierda
            2 -> derecha
            else -> null
        }
    }
}
