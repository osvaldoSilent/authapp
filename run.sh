#!/bin/bash

echo "📦 Cargando variables desde .env..."

if [ ! -f .env ]; then
  echo "❌ No se encontró el archivo .env. Crea uno desde .env.template"
  exit 1
fi

# Leer el archivo .env línea por línea
while IFS='=' read -r key value || [ -n "$key" ]; do
  # Ignorar comentarios y líneas vacías
  if [[ "$key" =~ ^#.* ]] || [[ -z "$key" ]]; then
    continue
  fi

  # Eliminar comillas y exportar
  clean_key=$(echo "$key" | xargs)
  clean_value=$(echo "$value" | sed -e 's/^"//' -e 's/"$//' | xargs)
  export "$clean_key=$clean_value"
done < .env

echo "🚀 Ejecutando la aplicación con Gradle..."
./gradlew bootRun
