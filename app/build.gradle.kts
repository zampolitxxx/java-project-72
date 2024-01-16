plugins {
    application
    id("java")
    id("jacoco")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.4"
}

application {
    mainClass.set("hexlet.code.App")
}

group = "hexlet.code"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
    implementation("com.zaxxer:HikariCP:5.1.0")
    // https://mvnrepository.com/artifact/com.h2database/h2
    implementation("com.h2database:h2:2.2.224")
    implementation("io.javalin:javalin:5.6.3")
    implementation("io.javalin:javalin-rendering:5.6.3")
    implementation("io.javalin:javalin-bundle:5.6.3")
    implementation("org.slf4j:slf4j-simple:2.0.10")
    implementation("gg.jte:jte:3.1.6")
    // Unirest
    implementation("com.konghq:unirest-java-bom:4.2.6")
    implementation("com.konghq:unirest-java:4.0.0-RC2")
    testImplementation("com.konghq:unirest-object-mappers-gson:4.2.6")
    implementation("com.konghq:unirest-objectmapper-jackson:4.2.6")

    //tests
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/org.projectlombok/lombok
//    compileOnly("org.projectlombok:lombok:1.18.30")
}


tasks.test {
    useJUnitPlatform()
}
tasks.jacocoTestReport {
    reports {
        xml.required = true
    }
}
tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "hexlet.code.App"
    }
}