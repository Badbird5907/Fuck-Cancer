plugins {
    id("java")
}

group = "dev.badbird"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    implementation("net.dv8tion:JDA:5.0.0-alpha.13")
    implementation("com.google.code.gson:gson:2.9.0")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
tasks.jar {
    manifest.attributes["Main-Class"] = "dev.badbird.Main"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
