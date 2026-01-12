# 🚀 Deploy to Render - Quick Guide

## ⚡ Ultra Quick Deploy (3 pasos)

### 1️⃣ Push to GitHub
```bash
git add .
git commit -m "feat: Add Render deployment"
git push origin main
```

### 2️⃣ Connect to Render
1. Go to [dashboard.render.com](https://dashboard.render.com)
2. Click **"New +"** → **"Blueprint"**
3. Select your repository: `Api-control-financiero`
4. Click **"Apply"**

### 3️⃣ Wait ~5 minutes
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

