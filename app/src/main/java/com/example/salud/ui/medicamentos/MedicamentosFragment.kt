package com.example.salud.ui.medicamentos

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.salud.R
import com.example.salud.recycler_view.MedicamentosAdapter
import com.example.salud.recycler_view.Singleton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_prueba.*
import kotlinx.android.synthetic.main.fragment_medicamentos.*


class MedicamentosFragment : Fragment() {

//    private lateinit var galleryViewModel: MedicamentosViewModel

    var actionMode : ActionMode? = null
    var position: Int = 0

    private val onLongItemClickListener: (Int) -> Unit = {
        position = it
        // Called when the user long-clicks on someView
        when (actionMode) {
            null -> {
                // Start the CAB using the ActionMode.Callback defined above
                actionMode = (requireView().context as AppCompatActivity).startSupportActionMode(
                    actionModeCallback
                )
                requireView().isSelected = true
                true
            }
            else -> false
        }

    }
    //        view?.let { it1 -> Snackbar.make(it1,"P R U E B A", Snackbar.LENGTH_LONG).show() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
//        registerForContextMenu(rvMedicamentos)

//        rvMedicamentos.setOnCreateContextMenuListener(View.OnCreateContextMenuListener() {
//            fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
//                super.onCreateContextMenu(menu, v, menuInfo)
//                val inflater: MenuInflater = menuInflater
//                inflater.inflate(R.menu.menu_recyclerview_medicamentos, menu)
//            }
//        })
    }

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

            rvMedicamentos.layoutManager = LinearLayoutManager(context)
            rvMedicamentos.adapter = MedicamentosAdapter(onLongItemClickListener)
        }
    }

    override fun onResume() {
        super.onResume()
        rvMedicamentos.adapter?.notifyDataSetChanged()
    }

    private val actionModeCallback = object : ActionMode.Callback {
        // Called when the action mode is created; startActionMode() was called
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            // Inflate a menu resource providing context menu items
            val inflater: MenuInflater = mode.menuInflater
            inflater.inflate(R.menu.contextual_menu, menu)
            return true
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
            return false // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.borrar -> {
                    eliminarAlarmaMedicamento(position)
                    mode.finish() // Action picked, so close the CAB
                    true
                }
                else -> false
            }
        }

        // Called when the user exits the action mode
        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }
    }

    fun eliminarAlarmaMedicamento(position: Int) {
        val medicamento = Singleton.dataSet[position]
        Singleton.dataSet.removeAt(position)
        rvMedicamentos.adapter?.notifyDataSetChanged()

        Snackbar.make(rvMedicamentos, "Alarma del medicamento ${medicamento.medicamento} eliminada.", Snackbar.LENGTH_LONG)
            .setAction("Deshacer") {
                Singleton.dataSet.add(position, medicamento)
                rvMedicamentos.adapter?.notifyDataSetChanged()
            }.show()
    }
}
