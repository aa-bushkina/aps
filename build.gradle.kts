plugins {
    id("java")
    id("io.freefair.lombok") version "6.5.1"
}

group = "org.course"
version = "1.0-SNAPSHOT"

apply(plugin = "java")
apply(plugin = "io.freefair.lombok")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("io.freefair.gradle:lombok-plugin:6.5.1")
    implementation("org.jfree:jfreechart:1.5.3")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}