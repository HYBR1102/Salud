package com.example.salud

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.room.util.StringUtil.newStringBuilder
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.salud.alarm.PlanificarAlarmas
import com.example.salud.dialog.AgregarAlarmaMedicamentoDialogFragment
import com.example.salud.medicamentos.MySingleton
import com.example.salud.recycler_view.Medicamento
import com.example.salud.recycler_view.Singleton
import kotlinx.android.synthetic.main.activity_nuevo_medicamento.*
import kotlinx.android.synthetic.main.activity_prueba.*
import kotlinx.android.synthetic.main.medicamentos_recyclerview_item.*
import java.util.*

@Suppress("DEPRECATION")
class NuevoMedicamentoActivity : AppCompatActivity(), AgregarAlarmaMedicamentoDialogFragment.AgregarAlarmaMedicamentoDialogListener {

    val onLongItemClickListener: (Int) -> Unit = {

    }

    private var mNotificationManager: NotificationManager? = null

    private val NOTIFICATION_ID = 0
    private val PRIMARY_CHANNEL_ID = "primary_notification_channel"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_medicamento)

        etMedicamento.visibility = View.GONE

        iniciarCargaDeDatos()

    }

    private fun DialogAgregarAlarmaMedicamento() {
        val dialog = AgregarAlarmaMedicamentoDialogFragment()
        dialog.show(supportFragmentManager, "NoticeDialogFragment")
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDialogPositiveClick() {

        var medicamento: Medicamento? = null

        if(etMedicamento.visibility == View.VISIBLE) {
            medicamento = Medicamento("${etMedicamento.text}","${etDosis.text}", convertirHoraYMinutoEnCadena())
        } else if (spinnerMedicamento.isEnabled) {
            medicamento = Medicamento("${spinnerMedicamento.selectedItem.toString()}","${etDosis.text}", convertirHoraYMinutoEnCadena())
        }

        medicamento?.let { Singleton.dataSet.add(it) }






        val notifyIntent = Intent(this, PlanificarAlarmas::class.java)

        val alarmUp = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID, notifyIntent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
        //Si la alarma está activa
        //alarmToggle.isChecked = alarmUp

        val notifyPendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val planificarAlarma = getSystemService(Context.ALARM_SERVICE) as AlarmManager

//        try {
//            val cal: Calendar = Calendar.getInstance()
//            cal.timeInMillis = System.currentTimeMillis()
//            cal.set(Calendar.HOUR_OF_DAY, tpHora.currentHour)
//            cal.set(Calendar.MINUTE, tpHora.currentMinute)
//            cal.set(Calendar.SECOND, 0)
//            val hora: Int = tpHora.currentHour
//            val minutos: Int = tpHora.currentMinute
//            var hora_programada =
//                StringBuilder().append(hora).append(":0").append(minutos)
//                    .toString() + " hrs"
//            if (minutos < 10) {
//                Toast.makeText(applicationContext,"Tarea programada a las --> $hora_programada", Toast.LENGTH_LONG).show()
//            } else {
//                hora_programada =
//                    StringBuilder().append(hora).append(":").append(minutos)
//                        .toString() + " hrs"
//                Toast.makeText(applicationContext,"Tarea programada a las --> $hora_programada", Toast.LENGTH_LONG).show()
//            }
//            makeText(
//                applicationContext,
//                "Tarea programada a las $hora_programada",
//                Toast.LENGTH_SHORT
//            ).show()
//            val intent = Intent(applicationContext, PlanificarAlarmas::class.java)
//            val pi =
//                PendingIntent.getBroadcast(applicationContext, 0, intent, 0)
//            planificarAlarma.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
//        } catch (ex: Exception) {
//            println("Error al programar hora de tarea: " + ex.message)
//        }

//        alarmToggle.setOnCheckedChangeListener { _, isChecked ->
//            val toastMessage: String = (if (isChecked) {

                val repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES

                val triggerTime = (SystemClock.elapsedRealtime()
                        + repeatInterval)

                // If the Toggle is turned on, set the repeating alarm with
                // a 15 minute interval.

                // If the Toggle is turned on, set the repeating alarm with
                // a 15 minute interval.
                planificarAlarma?.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    triggerTime, repeatInterval,
                    notifyPendingIntent
                )

                //Set the toast message for the "on" case.
//                "Stand Up Alarm On!"


//            } else {
//                //Cancel notification if the alarm is turned off
//                mNotificationManager?.cancelAll()
//
//                alarmManager?.cancel(notifyPendingIntent)
//
//                //Set the toast message for the "off" case.
//                "Stand Up Alarm Off!"
//            }) as String
//
//            //Show a toast to say the alarm is turned on or off.
//            Toast.makeText(this@MainActivity, toastMessage, Toast.LENGTH_SHORT).show()
//        }

        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()

        Toast.makeText(this, "Alarma del medicamento ${etMedicamento.text} agregada.", Toast.LENGTH_LONG).show()
    }

    override fun onDialogNegativeClick() {
        Toast.makeText(this, "No se agrego la alarma del medicamento", Toast.LENGTH_SHORT).show()
    }

    fun createNotificationChannel() {
        // Create a notification manager object.
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID,
                "Stand up notification",
                NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Notifies every 15 minutes to stand up and walk"
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
    }

    fun iniciarCargaDeDatos(){
        mostrarLoading()
        ejecutarRequest()
    }

    fun mostrarLoading(){
        spinnerMedicamento.visibility = View.GONE
        progressBarMedicamento.visibility = View.VISIBLE
    }

    fun mostrarContenido(){
        spinnerMedicamento.visibility = View.VISIBLE
        progressBarMedicamento.visibility = View.GONE

        val elementos = com.example.salud.medicamentos.Medicamento.dataSet
        elementos.add("Seleccione un medicamento...")
        val adaptador = ArrayAdapter(this, android.R.layout.simple_spinner_item, elementos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedicamento.adapter = adaptador

        spinnerMedicamento.setSelection(spinnerMedicamento.count-1)

        btnCapturarOtroMedicamento.setOnClickListener {
            etMedicamento.visibility = View.VISIBLE
            spinnerMedicamento.isEnabled = false
        }

        btnAgregar.setOnClickListener {
            //Cuadro de diálogo---------------------------------------------------------------------
            DialogAgregarAlarmaMedicamento()
//            Snackbar.make(it, "¡Alarma de medicamento agregada! ${convertirHoraYMinutoEnCadena()}", Snackbar.LENGTH_LONG).show()
        }
    }

    fun mostrarError(){
        spinnerMedicamento.visibility = View.GONE
        progressBarMedicamento.visibility = View.GONE
    }

    fun ejecutarRequest() {

        val url = "http://tutti-frutti-notes.company/salud/obtenerMedicamentos.php"

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                //textView.text = "Response: %s".format(response.toString())
                for(i in 0 until response.length()) {
                    var jsonObject = response.getJSONObject(i)
                    var medicamento: String = jsonObject.getString("medicamento")
                    com.example.salud.medicamentos.Medicamento.dataSet.add(medicamento)
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

    @RequiresApi(Build.VERSION_CODES.M)
    fun convertirHoraYMinutoEnCadena()
    : String {
        when (tpHora.hour) {
            12 -> {
                return "12:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            13 -> {
                return "01:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            14 -> {
                return "02:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            15 -> {
                return "03:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            16 -> {
                return "04:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            17 -> {
                return "05:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            18 -> {
                return "06:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            19 -> {
                return "07:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            20 -> {
                return "08:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            21 -> {
                return "09:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            22 -> {
                return "10:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            23 -> {
                return "11:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} PM"
            }
            0 -> {
                return "12:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} AM"
            }
            else -> {
                return "${tpHora.hour}:${if(tpHora.minute.toString().count() == 1) "0${tpHora.minute}" else tpHora.minute} AM"
            }
        }
    }
}
