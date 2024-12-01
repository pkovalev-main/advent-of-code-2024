rootProject.name = "advent-of-code-2024"
include("app")

dependencyResolutionManagement {
    versionCatalogs {
        create("userLibs") {
            library("logback", "ch.qos.logback:logback-classic:1.4.14")
            library("lombok", "org.projectlombok:lombok:1.18.30")
        }
    }
}

