# Salud
La aplicación empieza en una Navigation Drawer Activity de la cual se usan dos **Fragment**.

El Fragment que se muestra al comienzo contiene dos Text View y un Image View. Al presionar el Image View aparece un **Menú emergente** que nos da la opción de Cámara o Galería. Las dos opciones implican una **Intención implícita** y el proveedor de contenidos. La opción Cámara toma la foto y la guarda en el **Almacenamiento externo** pero privado de la aplicación devolviendo la dirección de la imagen mientras que la de opción de Galería devuelve un mapa de bits. Ambas se guardan en un un archivo de preferencias tipo **SharedPreferences** (archivo que se guarda en el **Almacenamiento interno**, también se guarda si es o no un mapa de bits como un tipo booleano, mientrás que las otras dos preferencias se guardan como tipo cadena. Así cuando el usuario escoja una imagen esta quedará guardada y se mostrará en el Image View cada vez que inicie la aplicación.

En la pestaña izquierda que proporciona la Navigation Drawer Activity tenemos acceso a los dos Fragment utilizados de esta.

En el otro Fragment tenemos un **Recycler View** con tres Text View cada elemento, este Recycler View contiene las "alarmas" de los medicamentos. Al hacer una presión prolongada en uno de los elementos aparece un **Modo de acción contextual** para eliminar dicha alarma. Al presionar para eliminar en el Modo de acción contextual aparecerá un **Snackbar** que nos permitirá deshacer la acción.

También tenemos un **Menú de opciones** que desplegará la opción de Agregar alarma y Salir.

Si presionamos la opción Agregar alarma se utiliza una **Intención explícita** para abrir una Activity.

En esta Activity tenemos un Spinner que es alimentado por un **Servicio Web** (para consumir el servicio web se utilizo Volley), tenemos un dos Edit Text y dos botones. Uno de los botones sólo es para mostrar uno de los Edit Text en caso de que no se quiera usar lo que contiene el Spinner, mientras que el otro botón guarda la alarma... apareciendo un **Cuadro de diálogo** para confirmar.

Aquí empiezan mis desgracias...

No pude implementar satisfactoriamente **Room** pero se puede ver el intento en la creación de las diferentes clases utilizadas para su funcionamiento.
Tampoco pude implementar la **Tarea en segundo plano** que planeaba para mi proyecto que es lo de **Alarm Manager**. 
