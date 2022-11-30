import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.6.20"
    application
}

group = "me.scolastico"
version = "dev-snapshot"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "12"
}

application {
    mainClass.set("me.scolastico.status.Application")
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    }
}

apply(plugin="org.jetbrains.dokka")

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "12"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "12"
    }
    jar{
        manifest {
            attributes["Main-Class"] = "me.scolastico.status.Application"
        }
        archiveFileName.set("status.jar")
    }
    shadowJar{
        archiveBaseName.set("status-shadow")
        archiveClassifier.set("")
        archiveVersion.set("")
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    reports {
        html.required.set(true)
        txt.required.set(true)
        xml.required.set(false)
    }
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "12"
}
tasks.withType<io.gitlab.arturbosch.detekt.DetektCreateBaselineTask>().configureEach {
    jvmTarget = "12"
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("me.scolastico:tools:2.2.3")
    implementation("io.leego:banana:2.1.0")
    implementation("io.bkbn:kompendium-core:2.3.1")
    implementation("io.bkbn:kompendium-swagger-ui:2.3.1")
    implementation("ch.qos.logback:logback-core:1.2.11")
    implementation("rome:rome:1.0")
    implementation("io.github.reactivecircus.cache4k:cache4k:0.5.0")
}
