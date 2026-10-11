# MONDI AI 3.5

Aplicación Android independiente de CifraChat. Mantiene el paquete `com.mondi.ai`, sube `versionCode` a 36 y usa Gemini como asistente conversacional.

## Pantalla y accesibilidad

- Contraste reforzado, tarjetas delimitadas, títulos claros y botones grandes.
- Acciones rápidas y herramientas en cuadrícula, sin desplazamiento horizontal para encontrarlas.
- Interruptores persistentes para activar texto grande y lectura automática de respuestas.
- Campo de mensaje multilínea.

## Funciones

- Chat con historial cifrado en el móvil; mantén pulsado un mensaje para copiarlo.
- Dictado, lectura de respuestas, búsqueda, teléfono y contactos.
- Gemini configurable con una clave propia de Google AI Studio. La clave se cifra en el dispositivo y no se incluye en la APK.
- Herramientas: nivel, calculadora, WhatsApp, terminal Android, editor y guía para Ubuntu con Termux/proot-distro.
- Comprobador de actualizaciones de publicaciones estables.

Ubuntu necesita Termux y proot-distro externos. El uso de Gemini está sujeto a límites y condiciones de la cuenta de Google.

## Instalación y actualizaciones

Descarga la APK release firmada desde la publicación estable. Para actualizar sobre la misma instalación, cada APK debe tener el mismo certificado persistente y un `versionCode` mayor. Android siempre pide confirmar la instalación. La APK 3.3 anterior usaba otro certificado, por lo que el paso a la firma persistente requiere una migración única; la 3.5 sí actualiza sobre la 3.4.

La clave privada no se almacena en el repositorio. GitHub Actions solo genera una APK release si están configurados los secretos de firma; sin ellos genera una APK debug para pruebas.

## Compilación

GitHub Actions usa Java 17, Android SDK 35 y Gradle 8.9.

```sh
gradle --no-daemon clean assembleDebug
```

APK release: `app/build/outputs/apk/release/app-release.apk`.
