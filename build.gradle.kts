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
    implementation("com.google.code.gson:gson:2.8.5")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    implementation("org.apache.httpcomponents:httpclient:4.5.5")

}

application {
    mainClassName = "es.uji.ei1048.meteorologia.App"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "8"
    targetCompatibility = "8"
}
