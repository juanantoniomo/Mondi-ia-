# MONDI AI 2.9

Aplicación Android independiente de CifraChat. Mantiene el paquete `com.mondi.ai` y sube `versionCode` a 30 para que Android reconozca la versión como actualización.

## Funciones

- Chat con proveedores que ofrecen una API compatible con OpenAI.
- Dictado por voz que envía el mensaje reconocido y lectura automática o repetición de respuestas.
- Búsqueda web, marcador telefónico y selección de contactos.
- Terminal Android local, editor privado y ejecución confirmada de scripts `.sh`.
- Catálogo de ejemplos de programación y guía para instalar Ubuntu mediante Termux/proot-distro.
- Consulta de versiones publicadas en GitHub Releases.

El chat necesita una URL HTTPS compatible, el nombre de un modelo y una clave API del proveedor. No incluye un modelo ni una clave; el servicio elegido puede cobrar. La clave solo se conserva durante la sesión. Ubuntu requiere Termux y proot-distro aparte.

## Actualizaciones sin desinstalar

Android actualiza una app sobre la existente cuando coinciden el paquete (`com.mondi.ai`), la firma digital y el `versionCode` nuevo es mayor. Esta versión conserva el paquete y sube el código de versión a 30. Las compilaciones de actualización deben usar siempre el mismo almacén de firma privado.

El flujo de GitHub Actions guarda la clave como Secrets del repositorio; nunca se debe subir el archivo `.jks` ni la contraseña al repositorio público. Hay que configurar una sola vez `MONDI_SIGNING_KEYSTORE_BASE64`, `MONDI_KEYSTORE_PASSWORD`, `MONDI_KEY_ALIAS` y `MONDI_KEY_PASSWORD`. Después, cada publicación de la APK `app-release.apk` se podrá instalar encima y mantener los datos. Android seguirá pidiendo confirmar la instalación.

La APK debug de pruebas no sirve para publicar actualizaciones: su firma no es persistente. Si la app instalada se firmó con una clave debug anterior, no se puede garantizar la actualización sobre ella sin recuperar esa clave original; en ese caso habrá que hacer una única transición a la APK firmada de publicación. Las actualizaciones siguientes ya usarán la misma firma persistente.

## Compilación

GitHub Actions compila con Java 17, Android SDK 35 y Gradle 8.9. Sin secretos de firma solo guarda una APK debug de pruebas y no publica una APK como actualización.

```sh
gradle --no-daemon clean assembleDebug
```

APK debug: `app/build/outputs/apk/debug/app-debug.apk`  
APK de publicación firmada: `app/build/outputs/apk/release/app-release.apk`
