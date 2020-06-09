package com.example.salud.recycler_view

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.salud.NuevoMedicamentoActivity
import com.example.salud.R
import kotlinx.android.synthetic.main.medicamentos_recyclerview_item.view.*

class MedicamentosAdapter(private val longItemClickistener: (Int) -> Unit) : RecyclerView.Adapter<MedicamentosAdapter.ViewHolder>() {
        class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val tvHora = v.tvHora
            val tvMedicamento = v.tvMedicamento
            val tvDosis = v.tvDosis
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.medicamentos_recyclerview_item, parent, false)

            return ViewHolder(v)
        }

        override fun getItemCount() = Singleton.dataSet.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            holder.itemView.setOnClickListener {
                Toast.makeText(holder.itemView.context, Singleton.dataSet.get(position).medicamento, Toast.LENGTH_LONG).show()
                val intent = Intent(holder.itemView.context, NuevoMedicamentoActivity:: class.java)
                intent.putExtra("hora", Singleton.dataSet.get(position).hora)
                intent.putExtra("medicamento", Singleton.dataSet.get(position).medicamento)
                intent.putExtra("dosis", Singleton.dataSet.get(position).dosis)
                holder.itemView.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener {
                longItemClickistener.invoke(position)
                true
            }

            holder.tvHora.text = Singleton.dataSet.get(position).hora
            holder.tvMedicamento.text = Singleton.dataSet.get(position).medicamento
            holder.tvDosis.text = Singleton.dataSet.get(position).dosis
        }
    }