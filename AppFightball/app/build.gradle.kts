import com.android.build.api.dsl.Packaging
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.model.JavaVisibility


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dokka)
}

android {
    namespace = "com.example.fightball"
    compileSdk = 35

    packagingOptions {
        resources {
            pickFirsts.add("META-INF/NOTICE.md".toString())
            pickFirsts.add("META-INF/LICENSE.md".toString())
        }
    }
    defaultConfig {
        applicationId = "com.example.fightball"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }



    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }

    // También es común est
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.lombok)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.okhttp.v4120)

    implementation(libs.retrofit.v220)
    implementation(libs.squareup.converter.gson)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.java.android.websocket.client)
    implementation(libs.spring.security.crypto)
    implementation(libs.dokka.gradle.plugin)

    implementation (libs.java.websocket)
    // https://mvnrepository.com/artifact/dev.gustavoavila/java-android-websocket-client
    implementation("dev.gustavoavila:java-android-websocket-client:2.0.2")


}




tasks.named<DokkaTask>("dokkaHtml") {
    outputDirectory.set(layout.buildDirectory.dir("dokka").get().asFile)

    dokkaSourceSets {
        create("androidMain") {
            sourceRoots.from(file("src/main/java"))

            // Configuración para incluir todo
            includeNonPublic.set(true)
            skipDeprecated.set(false)
            skipEmptyPackages.set(false)
            reportUndocumented.set(false)
            suppress.set(false)

            // Configuración específica por paquete
            perPackageOption {
                matchingRegex.set(".*")
                includeNonPublic.set(true)
            }

            jdkVersion.set(11)
            displayName.set("FightBall App")
            platform.set(org.jetbrains.dokka.Platform.jvm)
        }
    }

    doFirst {
        println("📄 Generando documentación completa de todo el módulo app (Android)")
    }
}









