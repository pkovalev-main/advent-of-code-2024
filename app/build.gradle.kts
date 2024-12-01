plugins {
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(userLibs.logback)
    compileOnly(userLibs.lombok)
    annotationProcessor(userLibs.lombok)

    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("local.pkovalev.adventofcode.aoc2024.App")
}
