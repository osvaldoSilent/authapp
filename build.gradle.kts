plugins {
	java
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.sonarqube") version "4.2.1.3168"

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
		property("sonar.projectKey", "osvaldoSilent_authapp")
		property("sonar.organization", "osvaldosilent") // según tu cuenta
		property("sonar.host.url", "https://sonarcloud.io")
		property("sonar.token", System.getenv("SONAR_TOKEN") ?: "")
		property("sonar.junit.reportPaths", file("build/test-results/test").absolutePath.replace("\\", "/"))
		property("sonar.gradle.skipCompile", "true")
		property("sonar.java.binaries", "build/classes/java/main")
		property("sonar.coverage.exclusions", "**/config/**,**/dto/**,**/exception/**,**/model/**,**/repository/**")

		//property("sonar.java.test.binaries", "build/classes/java/test")
		//property("sonar.java.binaries", "build/classes")
		//property("sonar.sources", "src/main/java")
		//properties["sonar.sources"] = listOf("src/main/java")

	}
}


tasks.matching { it.name in listOf("sonar", "sonarqube") }.configureEach {
	dependsOn("test")
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

	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	implementation("io.github.cdimascio:dotenv-java:3.0.0")

	implementation ("org.springframework.boot:spring-boot-starter-validation")

}


// Gestión de dependencias de Spring Cloud
dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
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

