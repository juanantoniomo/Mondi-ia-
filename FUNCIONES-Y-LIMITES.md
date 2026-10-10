# MONDI AI 2.9 — funciones y límites

## Incluido en el código Android

- Chat por API HTTPS compatible con OpenAI, con contexto de sesión.
- Dictado por voz que envía el mensaje reconocido, repetición de voz y lectura automática de las respuestas mediante TextToSpeech del teléfono.
- Búsqueda web, marcador y selector de contactos.
- Terminal del shell de Android, sin root.
- Editor local privado con ejecución confirmada y limitada a scripts `.sh`.
- Catálogo de ejemplos de programación y comandos.
- Guía Linux/Ubuntu con Termux y proot-distro externos.
- Acceso a Termux si está instalado y comprobación de Releases.
- Interfaz desplazable con márgenes para barras de sistema de Android 15.

## Lo que requiere configuración externa

El chat no incorpora motor ni clave API. Hay que indicar endpoint HTTPS, modelo y clave de un proveedor compatible. Sin un proveedor, no habrá respuestas que MONDI pueda leer en voz alta. La clave no se persiste en esta versión.

Ubuntu/Linux completo requiere Termux y proot-distro; no está embebido en la APK. La terminal local no tiene acceso root ni privilegios especiales. La app no controla otras apps ni instala actualizaciones silenciosamente.
