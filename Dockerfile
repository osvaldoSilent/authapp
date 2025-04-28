# ============================================
# 🧱 ETAPA 1: Construir la app con Gradle + Java 21
# ============================================

# Usamos una imagen oficial de Gradle con JDK 21 preinstalado
FROM gradle:8.5-jdk21 AS build

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos solo los archivos de configuración primero
# (build.gradle.kts, settings.gradle.kts y gradle wrapper) para aprovechar el caché
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# Ejecutamos un primer build para descargar dependencias
# Si no está el código fuente aún, el build puede fallar, por eso ponemos "|| return 0"
RUN gradle build || return 0

# Ahora copiamos el resto del proyecto (código fuente, resources, etc.)
COPY . .

# Construimos el proyecto completo
# Si quieres correr los tests, simplemente quita el "-x test"
RUN gradle build -x test


# ============================================
# 🏗️ ETAPA 2: Crear la imagen final de producción con solo Java 21
# ============================================

# Usamos Amazon Corretto 21 como entorno de ejecución (JRE)
FROM amazoncorretto:21

# Establecemos el directorio de trabajo para correr la app
WORKDIR /app

# Copiamos el JAR generado desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Exponemos el puerto 8080 (puedes cambiarlo si tu microservicio usa otro)
EXPOSE 8080

# Definimos el comando que correrá la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]
