plugins {
    id("java-library")
    id 'org.jetbrains.kotlin.jvm'
}

java {
}
dependencies {
    implementation("com.google.code.gson:gson:2.11.0")
    implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(8)
}