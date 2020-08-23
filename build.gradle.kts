plugins {
    java
    application
    id("io.gitlab.arturbosch.detekt") version "1.7.4"
    id("eu.codeloop.kotlin") version "2.2.6.0"
}

defaultTasks("run")

application {
    mainClassName = "pl.michalkowol.BootKt"
}

repositories {
    jcenter()
    maven {
        url = uri("https://jitpack.io")
    }
}

tasks.register<Jar>("fatJar") {
    archiveBaseName.set("${project.name}-assembly")
    manifest {
        attributes["Main-Class"] = application.mainClassName
    }
    from(configurations.runtimeClasspath.get().map({ if (it.isDirectory) it else zipTree(it) }))
    with(tasks.jar.get() as CopySpec)
}

tasks.register("stage") {
    dependsOn(tasks.check, "fatJar")
}

project.afterEvaluate {
    detekt {
        config = files("detekt.yml")
    }
}

dependencies {
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("com.sparkjava:spark-core:2.9.1")
    implementation("com.google.inject:guice:4.2.2")
    implementation("com.typesafe:config:1.4.0")
    implementation("com.github.kittinunf.result:result:3.0.0")

    implementation("com.github.softwareberg.rinca:httpclient:0.12.0")
    implementation("com.github.softwareberg.rinca:json:0.12.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-M2")

    testImplementation("junit:junit:4.13")
    testImplementation("com.natpryce:hamkrest:1.7.0.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")

    testImplementation("com.despegar:spark-test:1.1.8")
}
