import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.kotlin)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.hot.reload)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.compose)
    idea
}

group = "shampoo.luxury"
version = "1.3.4"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    mavenLocal()
    google()
}

dependencies {
    implementation(compose.components.resources)
    implementation(libs.bundles.malefic.compose)
    implementation(compose.desktop.currentOs)
    implementation(libs.bundles.malefic.ext)
    implementation(libs.bundles.exposed)
    implementation(libs.malefic.signal)
    implementation(libs.bundles.ktor)
    implementation(libs.font.awesome)
    implementation(libs.precompose)
    implementation(libs.whisper)
    implementation(libs.bcrypt)
    implementation(libs.kermit)
    implementation(libs.slf4j)
    implementation(libs.mysql)
}

compose.desktop {
    application {
        mainClass = "shampoo.luxury.leviathan.MainKt"

        nativeDistributions {
            includeAllModules = true
            targetFormats(Dmg, Exe, Deb)
            packageName = "Leviathan"
            packageVersion = "1.3.4"
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

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
