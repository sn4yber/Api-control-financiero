#!/bin/bash

echo "🐘 Iniciando script de inicialización de PostgreSQL..."

# Esperar a que PostgreSQL esté listo
until pg_isready -h localhost -U admin; do
  echo "⏳ Esperando a PostgreSQL..."
  sleep 2
done

echo "✅ PostgreSQL está listo"
echo "🔧 Base de datos 'control_financiero' ya está creada por POSTGRES_DB"
echo "✅ Inicialización completada"

