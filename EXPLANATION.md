# Reto Android: solución implementada

Esta es la explicación de la solución que implementé para resolver el reto.

## Arquitectura

Decidí usar la arquitectura MVVM, propuesta como oficial por parte de Google. Esta arquitectura establece una separación entre la vista (fragments, activities) y el modelo (Servicios web, bases de datos, fuentes de datos). Estos 2 elementos se comunican a través de un intermediario (ViewModel) y se crea también un enlace entre ellos.

Para seguir esta arquitectura, usé varios componentes de Android Jetpack.

### Vista

La app consta de 2 fragmentos:

-   `LocationFragment.kt`, el cual contiene todos los `EditText` necesarios para mostrar los detalles del código postal consultado.
-   `SupportMapFragment`, el cual contiene el mapa que muestra el polígono del código postal consultado.

Adicionalmente, todo lo relacionado a la lógica del mapa se encuentra en la `MainActivity.kt`. Originalmente el mapa estaba en `location_fragment.xml` y la lógica estaba en el `LocationFragment.kt`. Decidí extraer el mapa y ponerlo en el `activity_main.xml` para descargar de responsabilidad al Fragment.

### Modelo

La app utiliza un repositorio (`AppRepository.kt`) que sirve de intermediario entre las API y las demás capas de la aplicación. Aunque este repositorio podría parecer innecesario debido al tamaño de la aplicación, considero que implementarlo desde el principio otorga escalabilidad en caso de que se necesite implementar algún tipo de caché.

### ViewModel

Utilicé un `ViewModel` procedente de Android Jetpack, que mantiene el estado entre el `LocationFragment.kt` y la `MainActivity.kt`, así como sus respectivos _layouts_. El `ViewModel` pone los datos a disposición de la vista mediante `LiveData` y la vista se suscribe a estos datos. Asimismo, el ViewModel recibe los eventos que se produzcan en la vista.

El `ViewModel` también recibe los datos de la API transformados en datos de dominio. Esto último nos permite obtener los datos relevantes para la aplicación fácilmente, descartando datos irrelevantes de la API.

## Librerías utilizadas

-   ConstraintLayout: diseño de las pantallas.
-   LiveData: permitirle a las vistas suscribirse a los cambios en los datos de la app.
-   ViewModel: implementar la arquitectura MVVM.
-   Play Services maps: Mostrar los mapas de Google Maps.
-   Retrofit: obtener los datos de la API
-   Moshi: convertir la repuesta JSON en objetos de Kotlin.
-   Firebase: obtener un API key de Google Maps (no incluida como dependencia en la app).

## Código fuente

Todos los métodos están documentados, con excepción de aquellos utilizados por Retrofit para obtener los datos. Todas las clases están documentadas con excepción de las clases que sirven para representar los datos en JSON del servicio. Toda la app fue hecha con Kotlin. Todos los recursos utilizados en la app están debidamente establecidos en el folder `res`.

## Pruebas

Toda la app está probada manualmente, considerando los siguientes casos:

-   Insertar un código postal de la Ciudad de México.
-   Insertar un código postal del Estado de México.
-   Insertar un código portal inválido.
-   Desconectar el Wi-Fi.
-   Rotar la pantalla.
-   Cambiar el tema del dispositivo.

## Problemas conocidos

-   En horizontal la pantalla no hace scroll. Intenté con `ScrollView` y con `NestedScrollView` y no resultó.
-   La API de Polígonos responde 500 cuando se le envia cualquier código postal que no sea de la Ciudad de México. Atrapo ese error y dejé un error adicional que saldrá en caso de que la API responda con cualquier otro código.
-   La API de Sepomex responde 404 cuando se le envía cualquier cosa que no sea un código postal. Atrapo ese error y dejé un error adicional que saldrá en caso de que la API responda con cualquier otro código.
