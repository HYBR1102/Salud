# Salud
En el Button "REGISTRARSE" entramos a la activity SignUpActivity.kt (activity_sign_up.xml) en donde se implementa el almacenamiento de datos utilizando la biblioteca de ROOM, el cual a su vez implementa el almacenamiento en la memoria interna.

Al seleccionar el ImageView estamos utilizando una intención implicita y a su vez hacemos el consumo de un proveedor de contenido "FileProvider" para guardar la imagen de forma privada para la app en la memoria externa.

No es completamente funcional. :(
