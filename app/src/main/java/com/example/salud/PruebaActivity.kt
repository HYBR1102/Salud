package com.example.salud

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.salud.medicamentos.Medicamento
import com.example.salud.medicamentos.MySingleton
import kotlinx.android.synthetic.main.activity_prueba.*

class PruebaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba)

        iniciarCargaDeDatos()

    }

    fun iniciarCargaDeDatos(){
        mostrarLoading();
        ejecutarRequest();
    }

    fun mostrarLoading(){
        spinner.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
//        errorView.setVisibility(View.GONE);
    }

    fun mostrarContenido(){
        spinner.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
//        errorView.setVisibility(View.GONE);
        val elementos = Medicamento.dataSet
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adaptador
    }

    fun mostrarError(){
        spinner.setVisibility(View.GONE);
//        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    fun ejecutarRequest() {

        val url = "http://tutti-frutti-notes.company/salud/obtenerMedicamentos.php"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                //textView.text = "Response: %s".format(response.toString())
                for(i in 0 until response.length()) {
                    var jsonObject = response.getJSONObject(i)
                    var medicamento: String = jsonObject.getString("medicamento")
                    Medicamento.dataSet.add(medicamento)
                }
                //Despues de terminar de recuperar los datos cargamos los datos en la pantalla y la mostramos
                mostrarContenido()
            },
            Response.ErrorListener { error ->
                //Despues de parsear el error te recomiendo mostrar una pantalla de error
                mostrarError()
            }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonArrayRequest)
    }
}
