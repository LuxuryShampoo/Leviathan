import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi

plugins {
    alias(libs.plugins.compose.kotlin)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.compose)
}

group = "xyz.malefic.compose"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(libs.bundles.malefic.compose)
    implementation(compose.desktop.currentOs)
    implementation(libs.bundles.malefic.ext)
    implementation(libs.precompose)
}

compose.desktop {
    application {
        mainClass = "xyz.malefic.compose.MainKt"

        nativeDistributions {
            targetFormats(Dmg, Msi, Deb)
            packageName = "ComposeDesktopTemplate"
            packageVersion = "1.0.0"
        }
    }
}

tasks {
    register("formatAndLintKotlin") {
        group = "formatting"
        description = "Fix Kotlin code style deviations with kotlinter"
        dependsOn(formatKotlin)
        dependsOn(lintKotlin)
    }
    build {
        dependsOn(named("formatAndLintKotlin"))
    }
    check {
        dependsOn("installKotlinterPrePushHook")
    }
}
