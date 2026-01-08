# Deployment Guide

This guide covers deploying the Task Manager application with:
- **Backend**: Fly.io (already configured)
- **Frontend**: Vercel

## Backend Deployment (Fly.io) ✅

Already configured and deployed at: https://task-manager-long-brook-749.fly.dev

## Frontend Deployment (Vercel)

### Prerequisites

1. A Vercel account (sign up at https://vercel.com)
2. Vercel CLI installed (optional, for local testing)

### Step 1: Create Vercel Project

**Option A: Via Vercel Dashboard (Recommended)**

1. Go to https://vercel.com/new
2. Import your GitHub repository: `jandelgado/task-manager`
3. Configure project:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
4. Add Environment Variable:
   - **Name**: `VITE_API_URL`
   - **Value**: `https://task-manager-long-brook-749.fly.dev/api`
5. Click "Deploy"

**Option B: Via Vercel CLI**

```bash
# Install Vercel CLI
npm i -g vercel

# Login to Vercel
vercel login

# Deploy from frontend directory
cd frontend
vercel

# Follow prompts:
# - Set up and deploy? Yes
# - Which scope? [Select your account]
# - Link to existing project? No
# - Project name? task-manager-frontend (or your choice)
# - Directory? ./
# - Override build settings? No

# Add environment variable
vercel env add VITE_API_URL production
# When prompted, enter: https://task-manager-long-brook-749.fly.dev/api

# Deploy to production
vercel --prod
```

### Step 2: Get Vercel Project Information

You need three values for GitHub Actions:

1. **VERCEL_TOKEN** (Personal Access Token)
   - Go to https://vercel.com/account/tokens
   - Click "Create Token"
   - Name it: "GitHub Actions"
   - Copy the token (starts with `vercel_...`)

2. **VERCEL_ORG_ID** (Organization/Team ID)
   ```bash
   # View in project settings or run:
   cat frontend/.vercel/project.json | grep orgId
   ```

3. **VERCEL_PROJECT_ID** (Project ID)
   ```bash
   # View in project settings or run:
   cat frontend/.vercel/project.json | grep projectId
   ```

### Step 3: Add Secrets to GitHub

Go to: https://github.com/jandelgado/task-manager/settings/secrets/actions

Add these secrets:

| Secret Name | Value | Where to Get It |
|-------------|-------|-----------------|
| `VERCEL_TOKEN` | vercel_xxxxx... | https://vercel.com/account/tokens |
| `VERCEL_ORG_ID` | team_xxxxx or user_xxxxx | `.vercel/project.json` or Vercel project settings |
| `VERCEL_PROJECT_ID` | prj_xxxxx... | `.vercel/project.json` or Vercel project settings |
| `VITE_API_URL` | https://task-manager-long-brook-749.fly.dev/api | Your Fly.io backend URL + /api |

### Step 4: Update Backend CORS

After your Vercel deployment, you'll get a URL like: `https://task-manager-xyz.vercel.app`

Update the backend to allow requests from your Vercel domain:

```bash
# Set CORS allowed origins on Fly.io
devbox run flyctl secrets set CORS_ALLOWED_ORIGINS="http://localhost:5173,https://task-manager-xyz.vercel.app" -a task-manager-long-brook-749
```

**Important:** Replace `task-manager-xyz.vercel.app` with your actual Vercel URL!

### Step 5: Trigger Deployment

```bash
# Make a small change to trigger the workflow
echo "# Deployed" >> frontend/README.md
git add frontend/README.md
git commit -m "Trigger frontend deployment"
git push origin main
```

Watch the deployment at: https://github.com/jandelgado/task-manager/actions

## How It Works

### Frontend Deployment Flow

1. Push to `main` branch with changes in `frontend/**`
2. GitHub Actions:
   - Installs Node.js dependencies
   - Builds the Vite project with `VITE_API_URL` environment variable
   - Deploys to Vercel using the Vercel action
3. Vercel:
   - Receives the built files
   - Deploys to global CDN
   - Assigns a URL (e.g., https://task-manager-xyz.vercel.app)

### API Communication

- **Development** (local):
  - Frontend: http://localhost:5173
  - API calls: `/api/tasks` → proxied by Vite to `http://localhost:8080/api/tasks`

- **Production**:
  - Frontend: https://task-manager-xyz.vercel.app
  - API calls: `https://task-manager-long-brook-749.fly.dev/api/tasks` (direct)
  - CORS: Backend allows requests from Vercel domain

## Troubleshooting

### CORS Errors

**Symptom:** Console shows "CORS policy" errors

**Solution:**
```bash
# Check current CORS setting on Fly.io
devbox run flyctl ssh console -a task-manager-long-brook-749
# Then in the SSH session:
env | grep CORS

# Update CORS to include your Vercel URL
devbox run flyctl secrets set CORS_ALLOWED_ORIGINS="http://localhost:5173,https://YOUR-VERCEL-URL.vercel.app" -a task-manager-long-brook-749
```

### Vercel Build Fails

**Symptom:** Vercel build fails with "command not found" or similar

**Solution:**
- Ensure `package.json` has correct build script
- Check Node.js version matches (should be 24)
- Verify all dependencies are in `package.json`

### API Calls Return 404

**Symptom:** Frontend loads but API calls fail

**Solution:**
1. Check `VITE_API_URL` is set correctly in Vercel
2. Verify backend is running: https://task-manager-long-brook-749.fly.dev/api/tasks
3. Check browser console for the actual URL being called

### Environment Variable Not Working

**Symptom:** App uses `/api` instead of Fly.io URL

**Solution:**
- Vercel environment variables must start with `VITE_` to be accessible in the browser
- Rebuild the frontend after adding/changing environment variables
- Check the variable is set in Vercel project settings

## Monitoring

### Frontend (Vercel)
- Dashboard: https://vercel.com/dashboard
- Analytics: Check page views, performance metrics
- Logs: View deployment and function logs

### Backend (Fly.io)
```bash
# Check status
devbox run flyctl status -a task-manager-long-brook-749

# View logs
devbox run flyctl logs -a task-manager-long-brook-749

# Monitor metrics
devbox run flyctl dashboard -a task-manager-long-brook-749
```

## Cost Estimates

### Vercel Free Tier
- 100GB bandwidth/month
- 100 build minutes/month
- Unlimited static deployments
- **Cost:** Free for hobby projects

### Fly.io Free Tier
- 3 shared-cpu VMs
- 160GB outbound bandwidth
- Auto-stop/start configured
- **Cost:** Free for development

**Total:** $0/month for this setup on free tiers!

## Custom Domain (Optional)

### Add Custom Domain to Vercel

1. Go to Vercel project settings → Domains
2. Add your domain (e.g., `taskmanager.example.com`)
3. Configure DNS:
   ```
   Type: CNAME
   Name: taskmanager (or @)
   Value: cname.vercel-dns.com
   ```
4. Update backend CORS:
   ```bash
   devbox run flyctl secrets set CORS_ALLOWED_ORIGINS="http://localhost:5173,https://taskmanager.example.com" -a task-manager-long-brook-749
   ```

## Rollback

### Rollback Frontend
1. Go to Vercel dashboard → Deployments
2. Find previous working deployment
3. Click "..." → "Promote to Production"

### Rollback Backend
```bash
# View releases
devbox run flyctl releases -a task-manager-long-brook-749

# Rollback to previous version
devbox run flyctl releases rollback <VERSION> -a task-manager-long-brook-749
```

## Security Checklist

- [ ] CORS configured to only allow your Vercel domain
- [ ] Environment variables use secrets (not committed to git)
- [ ] HTTPS enforced on both frontend (Vercel) and backend (Fly.io)
- [ ] No API keys or sensitive data in frontend code
- [ ] H2 database switched to PostgreSQL for production (TODO)

## Next Steps

1. [ ] Add custom domain
2. [ ] Set up monitoring/alerting
3. [ ] Switch from H2 to PostgreSQL on Fly.io
4. [ ] Add authentication/authorization
5. [ ] Set up staging environment
6. [ ] Configure CDN caching headers
