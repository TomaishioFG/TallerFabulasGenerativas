package com.example.pruebastgg.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.pruebastgg.R
import com.example.pruebastgg.ui.ui.cajon.DisplayCadaver
import com.example.pruebastgg.ui.ui.cajon.DisplayS7
import com.example.pruebastgg.ui.ui.cajon.DisplayCuento
import com.example.pruebastgg.ui.ui.cajon.DisplayIariom
import com.example.pruebastgg.ui.ui.DisplayCenton

class DisplayFicOulipo : Fragment() {

    private var lastOpenedTextView: TextView? = null
    private var lastOpenedDescView: TextView? = null
    private var lastOpenedIconView: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fic_oulipo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.cadaver).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, DisplayCadaver())
                addToBackStack(null)
            }
        }
        view.findViewById<Button>(R.id.cuento).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, DisplayCuento())
                addToBackStack(null)
            }
        }
        view.findViewById<Button>(R.id.s7).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, DisplayS7())
                addToBackStack(null)
            }
        }
        view.findViewById<Button>(R.id.centon).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, DisplayCenton())
                addToBackStack(null)
            }
        }
        view.findViewById<Button>(R.id.iariom).setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fragment_container, DisplayIariom())
                addToBackStack(null)
            }
        }

        val infoButton: ImageButton = view.findViewById(R.id.infoButton)
        infoButton.setOnClickListener {
            showInfoDialog()
        }
    }

    private fun showInfoDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_info, null)

        setupExpandableSection(dialogView, R.id.info_cadaver, R.id.desc_cadaver, R.id.icon_cadaver)
        setupExpandableSection(dialogView, R.id.info_cuento, R.id.desc_cuento, R.id.icon_cuento)
        setupExpandableSection(dialogView, R.id.info_s7, R.id.desc_s7, R.id.icon_s7)
        setupExpandableSection(dialogView, R.id.info_centon, R.id.desc_centon, R.id.icon_centon)
        setupExpandableSection(dialogView, R.id.info_iariom, R.id.desc_iariom, R.id.icon_iariom)

        AlertDialog.Builder(requireContext())
            .setTitle("¿Qué contiene este Cajón de Sastre?")
            .setView(dialogView)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private fun setupExpandableSection(dialogView: View, textViewId: Int, descViewId: Int, iconViewId: Int) {
        val textView = dialogView.findViewById<TextView>(textViewId)
        val descView = dialogView.findViewById<TextView>(descViewId)
        val iconView = dialogView.findViewById<ImageView>(iconViewId)

        val clickListener = View.OnClickListener {
            toggleVisibility(descView, iconView)
        }

        textView.setOnClickListener(clickListener)
        iconView.setOnClickListener(clickListener)
    }

    private fun toggleVisibility(descView: TextView, iconView: ImageView) {
        if (lastOpenedTextView != null && lastOpenedTextView != descView) {
            lastOpenedDescView?.visibility = View.GONE
            lastOpenedIconView?.setImageResource(R.drawable.ic_arrow_down)
        }

        if (descView.visibility == View.GONE) {
            descView.visibility = View.VISIBLE
            iconView.setImageResource(R.drawable.ic_arrow_up)
            lastOpenedTextView = descView
            lastOpenedDescView = descView
            lastOpenedIconView = iconView
        } else {
            descView.visibility = View.GONE
            iconView.setImageResource(R.drawable.ic_arrow_down)
            lastOpenedTextView = null
            lastOpenedDescView = null
            lastOpenedIconView = null
        }
    }
}
