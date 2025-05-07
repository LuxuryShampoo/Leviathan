import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.kotlin)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.hot.reload)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.compose)
}

group = "shampoo.luxury"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(libs.bundles.malefic.compose)
    implementation(compose.desktop.currentOs)
    implementation(compose.components.resources)
    implementation(libs.bundles.malefic.ext)
    implementation(libs.malefic.signal)
    implementation(libs.font.awesome)
    implementation(libs.bundles.ktor)
    implementation(libs.precompose)
    implementation(libs.kermit)
    implementation(libs.slf4j)
}

compose.desktop {
    application {
        mainClass = "shampoo.luxury.MainKt"

        nativeDistributions {
            targetFormats(Dmg, Msi, Deb)
            packageName = "Leviathan"
            packageVersion = "1.0.0"
        }
    }
}

kotlin {
    jvmToolchain(21)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
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
        dependsOn("formatAndLintKotlin")
    }
    check {
        dependsOn(installKotlinterPrePushHook)
    }
    withType<LintTask> {
        this.source = this.source.minus(fileTree("build/generated")).asFileTree
    }
    withType<FormatTask> {
        this.source = this.source.minus(fileTree("build/generated")).asFileTree
    }
}
