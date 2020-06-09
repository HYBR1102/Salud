package com.example.salud.ui.medicamentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salud.R
import com.example.salud.recycler_view.Medicamento
import com.example.salud.recycler_view.MedicamentosAdapter
import com.example.salud.recycler_view.Singleton
import kotlinx.android.synthetic.main.fragment_medicamentos.*

class MedicamentosFragment : Fragment() {

    val onLongItemClickListener: (Int) -> Unit = { position ->

    }

//    private lateinit var galleryViewModel: MedicamentosViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicamentos, container, false)

//        galleryViewModel =
//            ViewModelProviders.of(this).get(MedicamentosViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_medicamentos, container, false)
////        val textView: TextView = root.findViewById(R.id.text_gallery)
////        galleryViewModel.text.observe(viewLifecycleOwner, Observer {
////            textView.text = it
////        })
//        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState).apply {
            LoadData()

            rvMedicamentos.layoutManager = LinearLayoutManager(context)
            rvMedicamentos.adapter = MedicamentosAdapter(onLongItemClickListener)
        }
    }

    override fun onResume() {
        super.onResume()
        rvMedicamentos.adapter?.notifyDataSetChanged()
    }

    private fun LoadData() {
        for (x in 0..20) {
            Singleton.dataSet.add(
                Medicamento(
                    "Medicamento ${x.toString().padStart(3, '0')}",
                    "Dosis ${x}",
                    "${if (x % 2 == 0) "12:00 AM" else "12:00 PM"}"
                )
            )
        }
    }
}
