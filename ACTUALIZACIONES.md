# Actualizaciones de MONDI AI

El paquete se mantiene como `com.mondi.ai` y cada versión nueva debe tener un `versionCode` mayor. Android actualiza encima y conserva los datos si la APK está firmada con el mismo certificado.

Las APK debug son solo para pruebas y pueden llevar otra firma en cada compilación. Para publicar APK instalables como actualización, configura una única clave privada persistente en GitHub Actions Secrets con estos cuatro valores:

- `MONDI_SIGNING_KEYSTORE_BASE64`: contenido del almacén `.jks` codificado en Base64.
- `MONDI_KEYSTORE_PASSWORD`: contraseña del almacén.
- `MONDI_KEY_ALIAS`: alias del certificado.
- `MONDI_KEY_PASSWORD`: contraseña de la clave.

No subir el `.jks` ni las contraseñas al repositorio público. Con esos secretos, el flujo publica `app-release.apk`; sin ellos, solo guarda un APK debug de pruebas y no crea una publicación instalable de actualización.

El actualizador de la app muestra la descarga de GitHub Releases. Android requiere que el usuario confirme la instalación. Si la app anterior tiene un certificado diferente, la instalación sobre ella se rechaza; una vez migrada a la firma persistente, las versiones posteriores se actualizan sin desinstalar.
