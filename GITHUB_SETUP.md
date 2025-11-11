# GitHub Repository Setup Guide

This guide will help you set up the GitHub repository and push your code.

## Prerequisites

- Git installed on your system
- GitHub account (thebizzareabhishek)
- GitHub CLI (optional, but recommended) - Install from https://cli.github.com/

## Option 1: Using the Setup Script (Recommended)

1. Run the setup script:
   ```bash
   ./setup-github.sh
   ```

2. If you have GitHub CLI installed, the script will:
   - Initialize git repository
   - Create the GitHub repository
   - Push all code to GitHub
   - Set up the remote

3. If you don't have GitHub CLI, the script will:
   - Initialize git repository
   - Create the initial commit
   - Provide manual instructions to create the repository on GitHub

## Option 2: Manual Setup

### Step 1: Initialize Git Repository

```bash
git init
git branch -M main
git add .
git commit -m "Initial commit: AutoResponder Android app"
```

### Step 2: Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `autoresponder`
3. Description: "AI-powered auto-reply assistant for Android"
4. Visibility: Public (or Private if you prefer)
5. **Do NOT** initialize with README, .gitignore, or license (we already have these)
6. Click "Create repository"

### Step 3: Push to GitHub

```bash
git remote add origin https://github.com/thebizzareabhishek/autoresponder.git
git push -u origin main
```

## GitHub Actions Workflow

The repository includes a GitHub Actions workflow (`.github/workflows/build-release.yml`) that:

- Automatically builds a release APK on every push to main/master
- Builds on pull requests (for testing)
- Can be manually triggered from the Actions tab
- Uploads the APK as a downloadable artifact (retained for 90 days)

### Accessing the Built APK

1. Go to the Actions tab in your GitHub repository
2. Click on the latest workflow run
3. Scroll down to the "Artifacts" section
4. Download the `app-release` artifact
5. Extract the APK file

## Workflow Features

- **Automated Builds**: Every push triggers a new build
- **Release APK**: Builds the release version (optimized and minified)
- **Artifact Storage**: APK is stored as an artifact for 90 days
- **Caching**: Gradle dependencies are cached for faster builds
- **Multi-trigger**: Works on push, pull request, and manual dispatch

## Troubleshooting

### Gradle Wrapper Not Found

If the workflow fails because `gradlew` is not found:
- The workflow will automatically generate it on the first run
- Alternatively, generate it locally: `gradle wrapper --gradle-version 8.2`

### Build Failures

If the build fails:
1. Check the Actions tab for error messages
2. Ensure all dependencies are correctly specified in `build.gradle.kts`
3. Verify that the Android SDK is properly configured in the workflow

### Permission Issues

If you get permission errors:
1. Ensure you have push access to the repository
2. Check that your GitHub credentials are configured correctly
3. If using GitHub CLI, run: `gh auth login`

## Next Steps

After setting up the repository:

1. ✅ Code is pushed to GitHub
2. ✅ GitHub Actions workflow is active
3. ✅ APK builds automatically on every push
4. 🔄 Monitor builds in the Actions tab
5. 📱 Download APKs from the Artifacts section

## Support

For issues or questions:
- Open an issue on GitHub
- Contact: [GitHub Profile](https://github.com/thebizzareabhishek)
- Donate: [PayPal](https://www.paypal.me/TheGreatBabaAbhishek)

