plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "todo"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2024.0.1"

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // Spring Cloud Config Server
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap")
    implementation("org.springframework.cloud:spring-cloud-starter-config")

    // Monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework:spring-aspects")
    // Micrometer & Tracing
    implementation("io.micrometer:micrometer-tracing-bridge-otel")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
    // DataSource observability
    implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.3")
    implementation("io.opentelemetry:opentelemetry-exporter-zipkin:1.35.0")
    // Runtime dependencies
    runtimeOnly("com.github.loki4j:loki-logback-appender:1.3.2")

    // Caffeine
    implementation("com.github.ben-manes.caffeine:caffeine")
    implementation("org.springframework.boot:spring-boot-starter-cache:3.4.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
