#!/bin/bash

echo "🚀 Desplegando API Control Financiero en Render..."
echo ""

# Verificar que estamos en la rama main
BRANCH=$(git branch --show-current)
if [ "$BRANCH" != "main" ]; then
    echo "⚠️  Advertencia: No estás en la rama 'main'"
    echo "📍 Rama actual: $BRANCH"
    read -p "¿Continuar de todos modos? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

# Agregar archivos
echo "📦 Agregando archivos..."
git add render.yaml
git add src/main/resources/application-prod.properties
git add DEPLOY-RENDER.md
git add Dockerfile
git add pom.xml

# Commit
echo "💾 Haciendo commit..."
git commit -m "feat: Add Render deployment configuration

- Add render.yaml for automatic deployment
- Add application-prod.properties profile
- Add Spring Boot Actuator for health checks
- Update Dockerfile to support PORT environment variable
- Add comprehensive deployment guide"

# Push
echo "🚀 Pusheando a GitHub..."
git push origin main

echo ""
echo "✅ ¡Código pusheado exitosamente!"
echo ""
echo "📋 Próximos pasos:"
echo "1. Ve a https://dashboard.render.com"
echo "2. Click en 'New +' → 'Blueprint'"
echo "3. Conecta tu repositorio"
echo "4. Selecciona 'Api-control-financiero'"
echo "5. Click en 'Apply'"
echo ""
echo "⏱️  El despliegue tomará aproximadamente 5-7 minutos"
echo "🌐 Tu API estará disponible en: https://control-financiero-api.onrender.com"
echo ""

