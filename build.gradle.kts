buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        // Android Gradle Plugin
        classpath("com.android.tools.build:gradle:8.2.0")
        // Frissített Kotlin verzió a kompatibilitáshoz
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.10")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

// Modern megoldás a clean taskra, a deprecated buildDir elkerülésével
tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}
