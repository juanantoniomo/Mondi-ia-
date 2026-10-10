# Estado de MONDI AI 2.9

- Proyecto Android independiente de CifraChat; paquete conservado `com.mondi.ai`.
- `versionCode` 30 y `versionName` 2.9.
- Código fuente incluye chat API, dictado con envío automático y lectura de respuestas, búsqueda, teléfono/contactos, terminal Android, editor, funciones de programación, guía de Linux/Ubuntu con Termux y comprobación de Releases.
- La actualización sobre la app instalada requiere que coincidan paquete y certificado de firma y que el código de versión aumente. El flujo ahora prepara APK `release` con firma persistente configurada en GitHub Secrets. Sin esos secretos solo genera la APK debug de pruebas.
- Si la APK antigua se firmó con otra clave debug, puede ser necesaria una transición única desinstalando esa instalación. No se puede recuperar la clave privada desde el APK instalado/publicado.
- Aún falta publicar los cambios y compilar esta revisión en GitHub Actions. La APK corregida no está generada todavía.
- MONDI no incorpora modelo ni clave API: se configura un proveedor HTTPS compatible. La API puede tener coste. La clave no se guarda entre sesiones.
- Termux y proot-distro son externos; MONDI no integra Ubuntu ni tiene root.
