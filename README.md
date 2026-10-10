# MONDI AI

Primera versión Android de MONDI AI como proyecto independiente de CifraChat. Genera una APK de depuración automáticamente en GitHub Actions; no requiere Google Colab ni Android Studio en tu ordenador.

## Descargar e instalar

1. Abre [Compilar APK MONDI AI en GitHub Actions](https://github.com/juanantoniomo/Mondi-ia-/actions/workflows/android-apk.yml) y selecciona la ejecución más reciente que aparezca en verde.
2. En Artifacts, descarga MONDI-AI-2.7-debug-APK.zip. Descomprímelo y copia app-debug.apk a tu teléfono.
3. Abre la APK. Si Android lo solicita, permite instalar aplicaciones desde el navegador o gestor de archivos que estés usando.

La compilación se ejecuta en cada cambio en main. Requisitos del teléfono: Android 8.0 o posterior. Es una APK de depuración para pruebas, no una versión firmada para publicar en Google Play.

## Qué incluye esta primera versión

- Aplicación Android con nombre y paquete propios: MONDI AI / com.mondi.ai.
- Interfaz de chat y conversación en memoria.
- Conexión HTTPS a servicios compatibles con la API de chat de OpenAI.
- Configuración de URL y modelo guardada localmente. La clave API se solicita al usar la app y no se guarda.

Para conversar necesitas conexión a Internet, una URL de API compatible que termine en /chat/completions, un modelo disponible para tu proveedor y una clave válida. El proveedor puede cobrar por su uso.

Esta es una base funcional de chat. Todavía no incorpora las funciones ampliadas del paquete MONDI 2.7 (voz, terminal Linux, actualizador u otras herramientas); se pueden añadir después sobre este proyecto separado.

## Compilar

GitHub Actions compila la APK con Android SDK 35, Java 17 y Gradle 8.9. Para compilar localmente, instala esos requisitos y ejecuta:

    gradle --no-daemon clean assembleDebug

La APK queda en app/build/outputs/apk/debug/app-debug.apk.
