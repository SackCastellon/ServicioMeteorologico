plugins {
    java
    application
    id("com.github.ben-manes.versions") version "0.20.0"
}

group = "es.uji.ei1048"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(kotlin("stdlib-jdk8", "1.3.11"))
    implementation("no.tornado:tornadofx:1.7.18-SNAPSHOT")
    implementation("org.controlsfx:controlsfx:8.40.14")

    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("org.apache.commons:commons-text:1.6")
    implementation("org.apache.logging.log4j:log4j-core:2.11.1")

    implementation("com.github.ben-manes.caffeine:caffeine:2.6.2")

    implementation("io.github.soc:directories:11")

    implementation("org.apache.httpcomponents:httpclient:4.5.6")
    implementation("org.jetbrains:annotations:16.0.3")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("org.mockito:mockito-core:2.23.4")
}

application {
    mainClassName = "es.uji.ei1048.meteorologia.App"
}

tasks.withType<JavaCompile> {
    sourceCompatibility = "8"
    targetCompatibility = "8"
}
