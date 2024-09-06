package com.example.pruebastgg

import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.pruebastgg.databinding.ActivityMainBinding
import com.example.pruebastgg.ui.DisplayAzar
import com.example.pruebastgg.ui.DisplayCreador
import com.example.pruebastgg.ui.DisplayFicOulipo

class Inicio : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        const val TITLE_HOME = "TFG > INICIO"
        const val TITLE_CREATOR = "TFG > INICIO > CREADOR"
        const val TITLE_RANDOM = "TFG > INICIO > AZAR"
        const val TITLE_FIC_OULIPO = "TFG > INICIO > CAJÓN DE SASTRE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        //binding.root.setBackgroundResource(R.drawable.aha2)

        setContentView(binding.root)

        showMainScreen()

        binding.buttonGoToCreador.setOnClickListener {
            showFragment(DisplayCreador(), TITLE_CREATOR)
        }

        binding.buttonGoToAzar.setOnClickListener {
            showFragment(DisplayAzar(), TITLE_RANDOM)
        }

        binding.buttonFic.setOnClickListener {
            showFragment(DisplayFicOulipo(), TITLE_FIC_OULIPO)
        }

        binding.infoButton.setOnClickListener {
            val builder = AlertDialog.Builder(this@Inicio)
            builder.setTitle("¿Qué son los tres modos?")
            builder.setMessage(
                Html.fromHtml(
                    "<br>El modo <b>Creador</b> ofrece la posibilidad de crear historias coherentes de manera ordenada y a gusto del consumidor." +
                            "<br><br>El modo <b>Azar</b> ofrece una experiencia caótica, en la que las historias no siguen ningún criterio y responden a la más pura aleatoriedad, de géneros, tramas y personas narrativas. " +
                            "<br><br>El <b>Cajón de Sastre</b> ofrece una recopilación de ideas y conceptos de la literatura potencial y creativa, puestos en práctica gracias al uso de la informática.",
                    Html.FROM_HTML_MODE_COMPACT
                )
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }
    }

    private fun showMainScreen() {
        binding.buttonGoToCreador.visibility = View.VISIBLE
        binding.buttonGoToAzar.visibility = View.VISIBLE
        binding.buttonFic.visibility = View.VISIBLE
        binding.infoButton.visibility = View.VISIBLE
        binding.appLogo.visibility = View.VISIBLE

        supportActionBar?.title = TITLE_HOME
    }

    private fun hideButtons() {
        binding.buttonGoToCreador.visibility = View.GONE
        binding.buttonGoToAzar.visibility = View.GONE
        binding.buttonFic.visibility = View.GONE
        binding.infoButton.visibility = View.GONE
        binding.appLogo.visibility = View.GONE
    }

    private fun showFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(title)
            .commit()
        hideButtons()

        supportActionBar?.title = title
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
            val fragmentTitle = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 2).name
            supportActionBar?.title = fragmentTitle
        } else {
            showMainScreen()
            super.onBackPressed()
        }
    }
}