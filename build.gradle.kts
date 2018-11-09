import org.gradle.internal.os.OperatingSystem

plugins {
    java
    application
    id("com.google.osdetector") version "1.6.0"
    id("com.github.ben-manes.versions") version "0.20.0"
}

val jfxVersion: String by project
val platform: String = when (val os = osdetector.os) {
    "windows" -> "win"
    "osx" -> "mac"
    else -> os
}

group = "es.uji.ei1048"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.openjfx:javafx-base:$jfxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$jfxVersion:$platform")
    implementation("org.openjfx:javafx-controls:$jfxVersion:$platform")
    implementation("org.openjfx:javafx-fxml:$jfxVersion:$platform")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

application {
    mainClassName = "es.uji.ei1048.meteorologia.App"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "11"
    targetCompatibility = "11"

    doFirst {
        options.compilerArgs = listOf(
            "--module-path", classpath.asPath,
            "--add-modules", "javafx.base",
            "--add-modules", "javafx.graphics",
            "--add-modules", "javafx.controls",
            "--add-modules", "javafx.fxml"
        )
    }
}

tasks.withType<JavaExec> {
    doFirst {
        jvmArgs = listOf(
            "--module-path", classpath.asPath,
            "--add-modules", "javafx.base",
            "--add-modules", "javafx.graphics",
            "--add-modules", "javafx.controls",
            "--add-modules", "javafx.fxml"
        )
    }
}
