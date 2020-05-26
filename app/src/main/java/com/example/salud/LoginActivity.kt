package com.example.salud

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.salud.data.UsuarioDatabase
import com.example.salud.data.UsuarioRepository
import com.example.salud.view_models.UsuarioViewModel
import com.example.salud.view_models.UsuarioViewModelFactory
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

//    private val usuarioViewModel: UsuarioViewModel by viewModels {
//        UsuarioViewModelFactory(
//            UsuarioRepository(UsuarioDatabase.getDatabase(applicationContext).usuarioDao())
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnRegistrar.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
//        loginViewModel = ViewModelProviders.of(this@LoginActivity).get(
//            LoginViewModel::class.java
//        )
//
//        loginViewModel!!.getGetAllData()
//            ?.observe(this, object : Observer<List<Login?>?> {
//                fun onChanged(@Nullable data: List<Login>) {
//                    try {
//                        etCorreoElectronico.setText(requireNonNull(data[0].email))
//                        etContraseña.setText(requireNonNull(data[0].password))
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            })
    }

//    fun onClick(view: View?) {
//        val strEmail: String = etCorreoElectronico.text.toString().trim()
//        val strPassword: String = etContraseña.text.toString().trim()
//
//        val login = Login()
//
//        if (TextUtils.isEmpty(strEmail)) {
//            etCorreoElectronico.error = "Please Enter Your E-mail Address"
//        } else if (TextUtils.isEmpty(strPassword)) {
//            etContraseña.error = "Please Enter Your Password"
//        } else {
//            data.email = strEmail
//            data.password = strPassword
//            loginViewModel?.insert(data)
//        }
//    }
}
