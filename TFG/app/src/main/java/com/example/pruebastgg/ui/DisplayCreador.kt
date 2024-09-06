package com.example.pruebastgg.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.pruebastgg.R
import com.example.pruebastgg.databinding.FragmentCreadorBinding

class DisplayCreador : Fragment() {

    private var _binding: FragmentCreadorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreadorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupGeneroButtons()

        binding.radioGroupNarrador.setOnCheckedChangeListener { _, _ ->
            showSecondarySections()
        }

        binding.btnGenerar.setOnClickListener {
            val generosSecundarios = getSelectedGenerosSecundarios()
            val tramas = getSelectedTramas()
            val narrador = getSelectedNarrador()
            val generoPrincipal = getSelectedGeneroPrincipal()

            val intent = Intent(activity, DisplayCreadorHistoria::class.java).apply {
                putStringArrayListExtra("GENEROS_SECUNDARIOS", ArrayList(generosSecundarios))
                putStringArrayListExtra("TRAMAS", ArrayList(tramas))
                putExtra("NARRADOR", narrador)
                putExtra("GENERO_PRINCIPAL", generoPrincipal)
            }
            startActivity(intent)
        }

        val btnAdd: ImageButton = view.findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val fragment = DisplayCreadorExtremo()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.infoButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(Html.fromHtml(("¿Qué ofrece el modo <b>Creador</b>?"), Html.FROM_HTML_MODE_COMPACT))
            builder.setMessage(
                Html.fromHtml(
                    "<br>El modo <b>Creador</b> ofrece la posibilidad de crear historias coherentes de manera ordenada y a gusto del consumidor." +
                            "<br><br>Para ello, cuenta con un selector para un <i>género principal</i>, otro para el tipo de <i>persona narrativa</i>, selectores para los <i>géneros secundarios</i> y selectores varios para el <i>tipo de trama*</i> de la historia. " +
                            "<br>La historia se generará al clicar, al menos, uno de cada sección, y darle al botón de generar. <br>Una misma selección puede dar lugar a varias historias diferentes, gracias a los algoritmos del funcionamiento interno de la aplicación." +
                            "<br><br>El botón <b>(+)</b> lleva al modo <b>Creador Extremo</b>, que permite un aún mayor nivel de personalización de las historias." +
                            "<br><br><br><br>*Los tipos de tramas están sacados de los estudios realizados por <i>Christopher Booker</i> y <i>Ronald Tobías</i>.",
                    Html.FROM_HTML_MODE_COMPACT
                )
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }
}

    private fun setupGeneroButtons() {
        val generoButtons = listOf(binding.radioFantasia, binding.radioCienciaFiccion)

        val generoButtonClickListener = View.OnClickListener { v ->
            for (button in generoButtons) {
                button.isChecked = false
            }

            val selectedButton = v as CompoundButton
            selectedButton.isChecked = true
            showNarratorSection()
            updateTramasVisibility()
        }

        for (button in generoButtons) {
            button.setOnClickListener(generoButtonClickListener)
        }
    }

    private fun showNarratorSection() {
        binding.textViewNarrador.visibility = View.VISIBLE
        binding.radioGroupNarrador.visibility = View.VISIBLE
    }

    private fun showSecondarySections() {
        binding.textViewGenerosSecundarios.visibility = View.VISIBLE
        binding.gridLayoutGenerosSecundarios.visibility = View.VISIBLE
        binding.textViewTramas.visibility = View.VISIBLE
        binding.gridLayoutTramas.visibility = View.VISIBLE
        binding.btnGenerar.visibility = View.VISIBLE
    }

    private fun getSelectedGenerosSecundarios(): List<String> {
        val generosSecundarios = mutableListOf<String>()
        if (binding.checkTerror.isChecked) generosSecundarios.add("terror")
        if (binding.checkRomance.isChecked) generosSecundarios.add("romance")
        if (binding.checkAventuras.isChecked) generosSecundarios.add("aventuras")
        return generosSecundarios
    }

    private fun getSelectedTramas(): List<String> {
        val tramas = mutableListOf<String>()
        if (binding.checkBusqueda.isChecked) tramas.add("búsqueda")
        if (binding.checkRescate.isChecked) tramas.add("rescate")
        //if (binding.checkPobrezaARiqueza.isChecked) tramas.add("pobreza a riqueza")
        //if (binding.checkRenacimiento.isChecked) tramas.add("renacimiento")
        if (binding.checkSuperandoMonstruo.isChecked) tramas.add("monstruo")
        //if (binding.checkTragedia.isChecked) tramas.add("tragedia")
        //if (binding.checkViajeIdaVuelta.isChecked) tramas.add("viaje")
        return tramas
    }

    private fun getSelectedNarrador(): String {
        return when (binding.radioGroupNarrador.checkedRadioButtonId) {
            R.id.radioPrimera -> "primera"
            R.id.radioSegunda -> "segunda"
            R.id.radioTercera -> "tercera"
            else -> "tercera"
        }
    }

    private fun getSelectedGeneroPrincipal(): String {
        return when {
            binding.radioFantasia.isChecked -> "fantasia"
            binding.radioCienciaFiccion.isChecked -> "cienciaficcion"
            //binding.radioWestern.isChecked -> "western"
            //binding.radioPiratas.isChecked -> "piratas"
            else -> "fantasia"
        }
    }

    private fun updateTramasVisibility() {
        when {
            binding.radioFantasia.isChecked -> {
                binding.checkBusqueda.visibility = View.VISIBLE
                binding.checkRescate.visibility = View.VISIBLE
                binding.checkPobrezaARiqueza.visibility = View.GONE
                binding.checkRenacimiento.visibility = View.GONE
                binding.checkSuperandoMonstruo.visibility = View.GONE
                binding.checkTragedia.visibility = View.GONE
                binding.checkViajeIdaVuelta.visibility = View.GONE
            }
            binding.radioCienciaFiccion.isChecked -> {
                binding.checkBusqueda.visibility = View.GONE
                binding.checkRescate.visibility = View.GONE
                binding.checkPobrezaARiqueza.visibility = View.GONE
                binding.checkRenacimiento.visibility = View.GONE
                binding.checkSuperandoMonstruo.visibility = View.VISIBLE
                binding.checkTragedia.visibility = View.GONE
                binding.checkViajeIdaVuelta.visibility = View.GONE
            }
            else -> {
                binding.checkBusqueda.visibility = View.VISIBLE
                binding.checkRescate.visibility = View.VISIBLE
                binding.checkPobrezaARiqueza.visibility = View.VISIBLE
                binding.checkRenacimiento.visibility = View.VISIBLE
                binding.checkSuperandoMonstruo.visibility = View.VISIBLE
                binding.checkTragedia.visibility = View.VISIBLE
                binding.checkViajeIdaVuelta.visibility = View.VISIBLE
            }
        }
    }
}
