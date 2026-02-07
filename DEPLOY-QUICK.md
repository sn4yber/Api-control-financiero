# 🚀 Deploy to Render - Quick Guide

## ⚡ Ultra Quick Deploy (3 Steps)

### 1️⃣ Push to GitHub
```bash
git add .
git commit -m "feat: Add Render deployment"
git push origin main
```

### 2️⃣ Create Web Service on Render

1. Go to [dashboard.render.com](https://dashboard.render.com)
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository: `Api-control-financiero`
4. Configure:
   - **Name**: `control-financiero-api`
   - **Runtime**: Docker
   - **Branch**: main
   - **Region**: Oregon
   - **Plan**: Free

5. **Environment Variables** (Add these):
   
   **⚠️ IMPORTANT: Copy ONLY the URL, NOT the 'psql' command!**
   
   ```
   Key: DATABASE_URL
   Value: postgresql://neondb_owner:npg_5OimKyqF9sIX@ep-dawn-unit-adn7096y-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require
   ```
   *(Remove `&channel_binding=require` if present)*
   
   ```
   Key: SPRING_PROFILES_ACTIVE
   Value: prod
   ```
   
   ```
   Key: JAVA_OPTS
   Value: -Xms256m -Xmx512m
   ```
   
   **Common mistake:** Don't copy `psql 'postgresql://...'` - only copy the URL starting with `postgresql://`

6. **Advanced Settings**:
   - Health Check Path: `/actuator/health`

7. Click **"Create Web Service"**

### 3️⃣ Wait ~5-7 minutes
Your API will be live at: `https://control-financiero-api.onrender.com`

---

## ✅ Test Your API

```bash
# Health check
curl https://control-financiero-api.onrender.com/actuator/health

# Create user
curl -X POST https://control-financiero-api.onrender.com/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@test.com","password":"123456","fullName":"Test User"}'
```

---

## 📚 Full Documentation

See [DEPLOY-RENDER.md](./DEPLOY-RENDER.md) for complete guide with:
- Manual deployment steps
- Environment variables
- Troubleshooting
- Monitoring
- Cost information
- Advanced configuration

---

## 💰 Cost

**$0/month** (Free tier includes):
- PostgreSQL database
- Web service hosting
- SSL certificate
- Automatic deployments

---

## ⚠️ Free Tier Limitations

- Service sleeps after 15 minutes of inactivity
- First request after sleep takes ~30 seconds
- PostgreSQL: 256MB RAM, 1GB storage, expires after 90 days (renewable)

---

## 🔄 Auto-Deploy

Every push to `main` branch automatically deploys to Render.

---

## 📊 Monitor Your App

- **Logs**: [dashboard.render.com](https://dashboard.render.com) → Your Service → Logs
- **Metrics**: Dashboard → Your Service → Metrics
- **Health**: Dashboard → Your Service → Events

---

## 🆘 Need Help?

1. Check logs in Render Dashboard
2. Read [DEPLOY-RENDER.md](./DEPLOY-RENDER.md)
3. Verify environment variables
4. Test health endpoint: `/actuator/health`

---

That's it! Your API is production-ready! 🎉

