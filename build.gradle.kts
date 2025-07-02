plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "4.4.1.3373"
	jacoco
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

jacoco {
	toolVersion = "0.8.10"
}


tasks.withType<Test> {
	useJUnitPlatform()
}

sonar {
	properties {
		property("sonar.projectKey", "authapp")
		property("sonar.organization", "osvaldosilent") // según tu cuenta
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.java.binaries", "build/classes")
		property("sonar.token", System.getenv("SONAR_TOKEN") ?: "")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	//implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	//runtimeOnly("com.h2database:h2")

	// JWT
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.30")
	annotationProcessor("org.projectlombok:lombok:1.18.30")
	// BCrypt para cifrado de contraseñas
	implementation("org.springframework.security:spring-security-crypto")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

//Declarar el nuevo source set
sourceSets {
	create("integrationTest") {
		java.srcDirs("src/integrationTest/java")
		resources.srcDirs("src/integrationTest/resources")
		compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
		runtimeClasspath += output + compileClasspath
	}
}

val sonarToken: String? = System.getenv("SONAR_TOKEN")
val mongoUri: String? = System.getenv("MONGO_URI")

sonarqube {
	properties {
		property("sonar.login", sonarToken ?: "")
	}
}


//Crear la configuración de dependencias
configurations.named("integrationTestImplementation") {
	extendsFrom(configurations["testImplementation"])
}

//Crear la tarea integrationTest
val integrationTest = tasks.register<Test>("integrationTest") {
	description = "Runs integration tests."
	group = "verification"
	testClassesDirs = sourceSets["integrationTest"].output.classesDirs
	classpath = sourceSets["integrationTest"].runtimeClasspath
	useJUnitPlatform()
	shouldRunAfter("test")
}

/*
* Generar archivos .exec separados para coberturaD
* */
integrationTest.configure {
	extensions.configure<JacocoTaskExtension> {
		setDestinationFile(layout.buildDirectory.file("jacoco/integrationTest.exec").get().asFile)
	}
}

//
/*
* Crear reportes separados con JaCoCo
* */
registerJacocoReportTask("jacocoUnitTestReport", "/jacoco/test.exec", tasks.test)
registerJacocoReportTask("jacocoIntegrationTestReport", "/jacoco/integrationTest.exec", integrationTest)

fun registerJacocoReportTask(name: String, execFile: String, dependsOnTask: TaskProvider<*>) {
	tasks.register<JacocoReport>(name) {
		dependsOn(dependsOnTask)
		reports {
			xml.required.set(true)
			html.required.set(true)
		}
		classDirectories.setFrom(fileTree("build/classes/java/main"))
		sourceDirectories.setFrom(files("src/main/java"))
		layout.buildDirectory.get().asFileTree.matching {
			include(execFile)
		}
	}
}

