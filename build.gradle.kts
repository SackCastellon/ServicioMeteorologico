import org.gradle.internal.os.OperatingSystem

plugins {
    java
    application
    id("com.github.ben-manes.versions") version "0.20.0"
}

group = "es.uji.ei1048"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.com.gson:gson:5.3.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
}

application {
    mainClassName = "es.uji.ei1048.meteorologia.App"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "8"
    targetCompatibility = "8"
}
