plugins {
	kotlin("jvm") version "2.2.0"
	kotlin("plugin.spring") version "2.2.0"
	id("org.springframework.boot") version "4.0.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.fResult"
version = "0.0.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

val log4jVersion = "3.0.0-beta3"
val testcontainersVersion = "1.21.3"

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:$testcontainersVersion")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.apache.logging.log4j:log4j-bom:$log4jVersion")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

  runtimeOnly("org.postgresql:postgresql")
  runtimeOnly("org.postgresql:r2dbc-postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:mongodb")
	testImplementation("org.testcontainers:r2dbc")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
