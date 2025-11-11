# Quick Start Guide

## 🚀 Push to GitHub

### Option 1: Automated Setup (Recommended)

Run the setup script:

```bash
./setup-github.sh
```

This will:
- ✅ Initialize git repository
- ✅ Create GitHub repository (if GitHub CLI is installed)
- ✅ Push all code to GitHub
- ✅ Set up GitHub Actions workflow

### Option 2: Manual Setup

1. **Initialize Git** (if not already done):
   ```bash
   git init
   git branch -M main
   ```

2. **Add all files**:
   ```bash
   git add .
   git commit -m "Initial commit: AutoResponder Android app"
   ```

3. **Create GitHub Repository**:
   - Go to https://github.com/new
   - Repository name: `autoresponder`
   - Click "Create repository"

4. **Push to GitHub**:
   ```bash
   git remote add origin https://github.com/thebizzareabhishek/autoresponder.git
   git push -u origin main
   ```

## 📱 GitHub Actions

After pushing to GitHub, the workflow will automatically:

1. ✅ Build release APK on every push to `main` branch
2. ✅ Upload APK as artifact (available for 90 days)
3. ✅ Build on pull requests (for testing)

### Accessing Built APK

1. Go to: https://github.com/thebizzareabhishek/autoresponder/actions
2. Click on the latest workflow run
3. Scroll to "Artifacts" section
4. Download `app-release` artifact
5. Extract and install the APK

## 🔧 Workflow Details

- **Trigger**: Push to main/master, pull requests, manual dispatch
- **Build Type**: Release APK (optimized, minified)
- **Artifact Retention**: 90 days
- **Build Time**: ~5-10 minutes (first build may take longer)

## 📝 Next Steps

1. ✅ Code is ready to push
2. ✅ GitHub Actions workflow is configured
3. 🔄 Run `./setup-github.sh` or follow manual setup
4. 📱 Download APKs from GitHub Actions artifacts

## 🆘 Troubleshooting

### GitHub CLI Not Installed

Install from: https://cli.github.com/

Or use manual setup (Option 2 above)

### Build Failures

- Check Actions tab for error messages
- Ensure all dependencies are correct
- Verify Android SDK setup in workflow

### Permission Issues

- Ensure you have push access to repository
- Check GitHub credentials
- Run `gh auth login` if using GitHub CLI

## 📚 More Information

See [GITHUB_SETUP.md](./GITHUB_SETUP.md) for detailed setup instructions.

## 🎉 You're All Set!

Once you push to GitHub, the workflow will automatically build your APK on every push. No manual builds needed!

