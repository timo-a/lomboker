/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    id("lomboker.java-application-conventions")
    id("org.springframework.boot") version "2.2.2.RELEASE"
}

dependencies {
    implementation(project(":lib"))

    implementation("info.picocli:picocli:4.6.1")

}

application {
    // Define the main class for the application.
    mainClass.set("de.lomboker.app.App")
}

tasks.bootJar {
    archiveBaseName.set("lomboker")
    archiveVersion.set(VERSION)

    dependsOn("generateVersionProperties")
}

val generatedVersionDir : String = "${buildDir}/generated-version"

sourceSets {
    main {
        output.dir(generatedVersionDir, Pair("builtBy","generateVersionProperties"))
    }
}

tasks.register("generateVersionProperties") {
    doLast {
        val propertiesFile : File = file("$generatedVersionDir/version.txt")
        propertiesFile.parentFile.mkdirs()
        propertiesFile.writeText(VERSION)
    }
}
