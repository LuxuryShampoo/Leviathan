import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.file.FileCopyDetails
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.kotlin)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.hot.reload)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.compose)
    idea
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "shampoo.luxury"
version = "1.3.6"

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
            packageVersion = "1.3.6"
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
    // Task to package compiled classes into a JAR for hot reload compatibility on Mac
    val hotJar by registering(Jar::class) {
        archiveBaseName.set("hot-classes")
        archiveVersion.set("")
        from(sourceSets.main.get().output)
        destinationDirectory.set(file("${buildDir}/run/main/classpath"))
    }
    named<JavaExec>("runHot") {
        dependsOn(hotJar)
        classpath = files("$buildDir/run/main/classpath/hot-classes.jar")
    }
    // Ensure hotJar is built before any runHot* tasks that might use the JAR
    matching { it.name.startsWith("runHot") }.configureEach {
        dependsOn(hotJar)
    }
    register<Jar>("hotFatJar") {
        archiveBaseName.set("hot-classes-all")
        archiveVersion.set("")
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
        destinationDirectory.set(file("${buildDir}/run/main/classpath"))
        manifest {
            attributes["Main-Class"] = "shampoo.luxury.leviathan.MainKt"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        // Exclude all signature and manifest files from dependencies
        exclude(
            "META-INF/*.SF",
            "META-INF/*.DSA",
            "META-INF/*.RSA",
            "META-INF/*.EC",
            "META-INF/*.MF",
            "META-INF/signature/*",
            "META-INF/INDEX.LIST"
        )
    }
    named<ShadowJar>("shadowJar") {
        manifest {
            attributes["Main-Class"] = "shampoo.luxury.leviathan.MainKt"
        }
    }
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
