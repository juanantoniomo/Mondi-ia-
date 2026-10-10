plugins { id("com.android.application") }
android {
    namespace = "com.mondi.ai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mondi.ai"
        minSdk = 26
        targetSdk = 35
        // Debe ser mayor que el versionCode de cada APK MONDI ya instalada.
        versionCode = 31
        versionName = "3.0"
    }

    // La clave se inyecta desde GitHub Actions Secrets y nunca se guarda en el repo.
    signingConfigs {
        create("release") {
            val keystorePath = System.getenv("MONDI_KEYSTORE_PATH")
            if (!keystorePath.isNullOrBlank()) {
                storeFile = file(keystorePath)
                storePassword = System.getenv("MONDI_KEYSTORE_PASSWORD")
                keyAlias = System.getenv("MONDI_KEY_ALIAS")
                keyPassword = System.getenv("MONDI_KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("release") {
            val signingReady = listOf("MONDI_KEYSTORE_PATH", "MONDI_KEYSTORE_PASSWORD", "MONDI_KEY_ALIAS", "MONDI_KEY_PASSWORD")
                .all { !System.getenv(it).isNullOrBlank() }
            if (signingReady) signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }
}
