package com.example.salud.ui.recordatorios

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.salud.R
import kotlinx.android.synthetic.main.fragment_recordatorios.*
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class RecordatoriosFragment : Fragment(), PopupMenu.OnMenuItemClickListener {

    private lateinit var homeViewModel: RecordatoriosViewModel

    private val TAKE_PICTURE = 1
    private val SELECT_PICTURE = 2
    private var currentPhotoPath: String = ""

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(RecordatoriosViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_recordatorios, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState).apply {

            val sharedPref = context?.getSharedPreferences("imagen_bienvenida", Context.MODE_PRIVATE)
            val imagen: String? = sharedPref?.getString("imagen","")
            val esBitmap: Boolean? = sharedPref?.getBoolean("esBitmap",false)
            val imagenBitmap: String? = sharedPref?.getString("imagenBitmap","")

            if(imagen != "" || imagenBitmap != "") {
                if(esBitmap == true) {
                    ivBienvenida.setImageBitmap(imagenBitmap?.let { convertirStringABitmap(it) })
                    redondearImagen()
                } else {
                    ivBienvenida.setImageBitmap(BitmapFactory.decodeFile(imagen))
                    redondearImagen()
                }
            }

            //Menú contextual flotante--------------------------------------------------------------
            ivBienvenida.setOnClickListener {
                showPopUp(it)
            }

        }
    }
    //Métodos del menú contextual flotante----------------------------------------------------------
    private fun showPopUp(view: View) {
        val popUp: PopupMenu = PopupMenu(context, view)
        popUp.setOnMenuItemClickListener(this)
        popUp.inflate(R.menu.menu_popup_imagen)
        popUp.show()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        //Métodos que implican una intención implícita y un proveedor de contenidos
        return when (item.itemId) {
            R.id.camara -> {
                dispatchTakePictureIntent()
                true
            }
            R.id.galeria -> {
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), SELECT_PICTURE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
//--------------------------------------------------------------------------------------------------
    //Método que recibe la imagen-------------------------------------------------------------------
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var esBitmap: Boolean = false
        var imagenBitmap: String = ""

        if (requestCode == TAKE_PICTURE) {
            esBitmap = false
            ivBienvenida.setImageBitmap(BitmapFactory.decodeFile(currentPhotoPath))
            redondearImagen()
        } else if (requestCode == SELECT_PICTURE){
            esBitmap = true
            val selectedImage = data!!.data
            val `is`: InputStream
            try {
                `is` = selectedImage?.let { context?.contentResolver?.openInputStream(it) }!!
                val bis = BufferedInputStream(`is`)
                val bitmap: Bitmap = BitmapFactory.decodeStream(bis)
                ivBienvenida.setImageBitmap(bitmap)
                redondearImagen()
                imagenBitmap = convertirBitmapAString(bitmap)
            } catch (e: FileNotFoundException) {
            }
        }

        //Preferencias con SharedPreferences--------------------------------------------------------
        val sharedPref = context?.getSharedPreferences("imagen_bienvenida", Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("imagen", currentPhotoPath)
            putBoolean("esBitmap", esBitmap)
            putString("imagenBitmap", imagenBitmap)
            commit()
        }
    //----------------------------------------------------------------------------------------------
    }

    fun convertirBitmapAString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun convertirStringABitmap(string: String): Bitmap {
        val imageBytes = Base64.decode(string, 0)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
//--------------------------------------------------------------------------------------------------

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {

                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri? = context?.let { it1 ->
                            FileProvider.getUriForFile(
                                it1,
                                "com.example.salud.fileprovider",
                                it
                            )
                        }
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, TAKE_PICTURE)
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    @Suppress("DEPRECATION")
    fun redondearImagen() {

        val originalDrawable: Drawable = ivBienvenida.drawable
        val originalBitmap: Bitmap = (originalDrawable as BitmapDrawable).getBitmap()

        val roundedDrawable: RoundedBitmapDrawable =
            RoundedBitmapDrawableFactory.create(resources, originalBitmap)

        roundedDrawable.cornerRadius = originalBitmap.height.toFloat()

        ivBienvenida.setImageDrawable(roundedDrawable)
    }

}
