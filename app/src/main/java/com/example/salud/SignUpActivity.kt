package com.example.salud

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStoragePublicDirectory
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.salud.data.Usuario
import com.example.salud.data.UsuarioDatabase
import com.example.salud.data.UsuarioRepository
import com.example.salud.view_models.UsuarioViewModel
import com.example.salud.view_models.UsuarioViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class SignUpActivity : AppCompatActivity() {

    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_TAKE_PHOTO = 1
    var currentPhotoPath: String = ""

    private val usuarioViewModel: UsuarioViewModel by viewModels {
        UsuarioViewModelFactory(
            UsuarioRepository(UsuarioDatabase.getDatabase(applicationContext).usuarioDao())
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        ivUsuario.setOnClickListener {
            dispatchTakePictureIntent()
        }

        btnRegistrarse.setOnClickListener {
            val strUsuario: String = etUsuario.text.toString().trim()
            val strNombre: String = etNombre.text.toString().trim()
            val strCorreo: String = etCorreo.text.toString().trim()
            val strContrasena: String = etContrasena.text.toString().trim()
            val strConfirmarContrasena: String = etConfirmarContrasena.text.toString().trim()

            val strFotoDePerfil: String = currentPhotoPath

            when {
                TextUtils.isEmpty(strUsuario) -> {
                    etUsuario.error = "Por favor, introduzca su usuario."
                }
                TextUtils.isEmpty(strCorreo) -> {
                    etCorreoElectronico.error = "Por favor, introduzca su correo electrónico."
                }
                TextUtils.isEmpty(strContrasena) -> {
                    etContrasena.error = "Por favor, introduzca su contraseña."
                }
                strContrasena != strConfirmarContrasena -> {
                    etConfirmarContrasena.error = "La contraseña no coincide."
                }
                else -> {

                    val usuarioN =
                        Usuario(1, strUsuario, strNombre, strCorreo, strContrasena, strFotoDePerfil)
                    usuarioViewModel.guardarUsuario(usuarioN)
                    Snackbar.make(it, "Usuario agregado", Snackbar.LENGTH_LONG)
                        .setAction("Iniciar Sesión") { onBackPressed() }.show()
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {

                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.salud.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }
}