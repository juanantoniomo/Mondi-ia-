# Actualizaciones de MONDI AI

La APK 3.4 usa el paquete `com.mondi.ai`, `versionCode` 35 y la clave de firma persistente de MONDI. El comprobador busca publicaciones estables, ignora versiones preliminares y ofrece la APK release cuyo `versionCode` sea superior al instalado.

Antes de instalar, MONDI verifica el paquete, la versión, la firma y el SHA-256 cuando GitHub lo proporciona. Android presenta la confirmación final de instalación.

La APK 3.3 entregada anteriormente tiene un certificado distinto de la clave persistente. Android no permite actualizar entre certificados distintos: para pasar a 3.4 hay que desinstalar la 3.3 e instalar 3.4 una vez. La desinstalación puede borrar los datos locales de la app; conserva antes lo que necesites. Después de esa migración, las actualizaciones con la misma firma se instalan encima.

La clave privada no se guarda en el repositorio. GitHub Actions publica una APK release únicamente cuando están configurados los secretos `MONDI_SIGNING_KEYSTORE_BASE64`, `MONDI_KEYSTORE_PASSWORD`, `MONDI_KEY_ALIAS` y `MONDI_KEY_PASSWORD`. Las APK debug son solo para pruebas y no sirven para actualizar la versión release.
