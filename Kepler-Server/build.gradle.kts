plugins {
    java
    application
    kotlin("jvm") version "1.6.10"
}

group = "org.alexdev.kepler"
version = "1.0-SNAPSHOT"

repositories {
    flatDir {
        dirs("lib")
    }
    maven(url = "https://jitpack.io")
    mavenCentral()
}

application {
    mainClass.set("org.alexdev.kepler.Kepler")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.maxmind.geoip2:geoip2:2.12.0")
    implementation(group = "com.zaxxer", name = "HikariCP", version = "3.4.1")
    implementation(group = "org.mariadb.jdbc", name = "mariadb-java-client", version = "2.3.0")
    implementation(group = "io.netty", name = "netty-all", version = "4.1.33.Final")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.25")
    implementation(group = "org.slf4j", name = "slf4j-log4j12", version = "1.7.25")
    implementation(group = "log4j", name = "log4j", version = "1.2.17")
    implementation(group = "org.apache.commons", name = "commons-configuration2", version = "2.2")
    implementation(group = "org.apache.commons", name = "commons-lang3", version = "3.9")
    implementation(group = "commons-lang", name = "commons-lang", version = "2.6")
    implementation(group = "commons-validator", name = "commons-validator", version = "1.6")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.0")
    implementation("com.github.bhlangonijr:chesslib:1.1.1")
    implementation("com.goterl:lazysodium-java:5.1.1")
    implementation("net.java.dev.jna:jna:5.8.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks {
    val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes(mapOf("Main-Class" to application.mainClass)) } // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it) } +
                sourcesMain.output
        from(contents)
    }
    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }
}