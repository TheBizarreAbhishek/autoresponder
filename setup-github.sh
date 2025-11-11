#!/bin/bash

# Setup script to initialize git and create GitHub repository

set -e

REPO_NAME="autoresponder"
GITHUB_USER="thebizzareabhishek"

echo "🚀 Setting up GitHub repository for $REPO_NAME"

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "❌ Git is not installed. Please install Git first."
    exit 1
fi

# Check if GitHub CLI is installed
if command -v gh &> /dev/null; then
    echo "✅ GitHub CLI found"
    USE_GH_CLI=true
else
    echo "⚠️  GitHub CLI not found. You'll need to create the repository manually."
    USE_GH_CLI=false
fi

# Initialize git repository if not already initialized
if [ ! -d .git ]; then
    echo "📦 Initializing git repository..."
    git init
    git branch -M main
else
    echo "✅ Git repository already initialized"
fi

# Add all files
echo "📝 Adding files to git..."
git add .

# Create initial commit
echo "💾 Creating initial commit..."
git commit -m "Initial commit: AutoResponder Android app

- AI-powered auto-reply assistant for social media platforms
- Support for WhatsApp, Instagram, Facebook Messenger, Telegram
- Material Design 3 UI with modern navigation
- Chat history management with contact name lookup
- Preset replies and AI configuration
- GitHub Actions workflow for automated APK builds"

# Create GitHub repository if GitHub CLI is available
if [ "$USE_GH_CLI" = true ]; then
    echo "🔗 Creating GitHub repository..."
    gh repo create "$REPO_NAME" --public --source=. --remote=origin --push || {
        echo "⚠️  Repository might already exist. Trying to add remote..."
        git remote add origin "https://github.com/$GITHUB_USER/$REPO_NAME.git" 2>/dev/null || \
        git remote set-url origin "https://github.com/$GITHUB_USER/$REPO_NAME.git"
    }
    
    echo "🚀 Pushing to GitHub..."
    git push -u origin main
else
    echo ""
    echo "📋 Manual steps required:"
    echo "1. Go to https://github.com/new"
    echo "2. Create a new repository named: $REPO_NAME"
    echo "3. Run the following commands:"
    echo ""
    echo "   git remote add origin https://github.com/$GITHUB_USER/$REPO_NAME.git"
    echo "   git push -u origin main"
    echo ""
fi

echo ""
echo "✅ Setup complete!"
echo "🔗 Repository: https://github.com/$GITHUB_USER/$REPO_NAME"
echo "📱 GitHub Actions will automatically build the APK on push"

