import org.gradle.api.tasks.testing.Test
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.testLogger)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.dataframe)
    alias(libs.plugins.ksp)
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.log.back)

            // Api Requests
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)

            // Fancy UI
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)

            // Data Science?
            implementation(libs.dataframe)
            implementation(libs.multik.core)
            implementation(libs.multik.default)
            implementation(libs.dagger)


        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

    }
}

compose.desktop {
    application {
        mainClass = "com.lithiumlogos.chess.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.lithiumlogos.chess"
            packageVersion = "1.0.0"

            windows {
                iconFile.set(project.file("composeApp/src/commonMain/composeResources/drawable/icon.ico"))
            }
        }
    }
}

// Ensure that tests run during the build process
tasks.named("build") {
    dependsOn("check")
}

tasks.withType<Test> {
    useJUnitPlatform()
}