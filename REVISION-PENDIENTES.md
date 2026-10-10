# Revisión de MONDI AI 2.9

- La fuente incorpora chat, voz, herramientas Android, terminal local, editor, programación y guía externa de Linux; mantiene el paquete `com.mondi.ai`.
- Android 15: se aplican márgenes de barras del sistema y desplazamiento.
- `versionCode` 30 permite que Android reconozca una actualización frente a versiones anteriores con menor código.
- Actualización sin desinstalar: requiere conservar el mismo certificado privado de firma. El workflow produce una APK de publicación únicamente cuando se configuran los secretos de firma.
- Falta publicar esta revisión, configurar una vez la firma persistente y compilar/probar en CI/dispositivo. La APK corregida todavía no está generada.
- El actualizador busca `app-release.apk` firmada y abre su descarga; Android solicita confirmación.
- MONDI necesita endpoint/modelo/clave de un proveedor para responder; no incluye modelo propio. La clave no persiste entre sesiones.
- Ubuntu requiere Termux/proot-distro externo.
