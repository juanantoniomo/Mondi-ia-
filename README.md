# MONDI AI 3.4

Aplicación Android independiente de CifraChat. Mantiene el paquete `com.mondi.ai`, sube `versionCode` a 35 y usa Gemini como asistente conversacional.

## Funciones

- Chat con mensajes separados, historial cifrado en el móvil y copia de mensajes manteniéndolos pulsados.
- Indicador de carga y opción para detener una respuesta; lectura por voz y dictado.
- Configuración de Gemini plegable, con explicación de la clave, la privacidad y los posibles límites o costes del proveedor.
- Herramientas agrupadas para voz, búsqueda, teléfono, calculadora, nivel, terminal, editor y programación.
- Guía para instalar Ubuntu mediante Termux y proot-distro; no requiere root.
- Clave API y conversación cifradas mediante Android Keystore. La clave no se incluye en la APK.

Gemini requiere una clave propia de Google AI Studio. El uso está sujeto a los límites y condiciones de la cuenta. Ubuntu necesita Termux y proot-distro externos.

## Instalación y actualizaciones

Descarga MONDI AI 3.4 desde la publicación estable de GitHub. Esta APK usa la firma persistente. Si tienes instalada una versión 3.3 firmada con el certificado anterior, Android exige desinstalarla una sola vez antes de instalar la 3.4; guarda antes cualquier dato local que quieras conservar. Después, las versiones con la misma firma se actualizan desde la aplicación.

El botón Buscar actualización localiza publicaciones estables nuevas, verifica paquete, versión y certificado antes de pedir a Android que instale la APK. Android siempre pide confirmar la instalación.

La clave privada no se incluye en el repositorio. El flujo solo publica una APK release si los secretos de firma están configurados.

## Compilación

GitHub Actions compila con Java 17, Android SDK 35 y Gradle 8.9. No se usa Google Colab.

```sh
gradle --no-daemon clean assembleDebug
```

Salidas: `app/build/outputs/apk/debug/app-debug.apk` y, cuando la firma está configurada, `app/build/outputs/apk/release/app-release.apk`.
