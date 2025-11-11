# AutoResponder

A modern Android app for automated message replies across multiple social media platforms using AI.

## Features

- 🤖 **AI-Powered Replies**: Support for GPT-4, Gemini, DeepSeek, and custom APIs
- 📱 **Multi-Platform Support**: WhatsApp, Instagram, Facebook Messenger, Telegram, Twitter
- 🎨 **Modern UI/UX**: Material Design 3 with drawer navigation and bottom navigation
- 💬 **Chat History**: Stores conversation context for better AI responses
- ⚙️ **Customizable Settings**: Configure reply behavior, delays, and AI models
- 📊 **Statistics**: Track total replies and daily activity

## Architecture

- **Kotlin**: Modern Android development with Kotlin
- **Material Design 3**: Beautiful, modern UI components
- **Navigation Component**: Jetpack Navigation for seamless navigation
- **NotificationListenerService**: Intercepts notifications to read and reply to messages
- **SQLite Database**: Stores chat history and message data
- **SharedPreferences**: Stores user settings and configurations

## Setup

1. Clone the repository
   ```bash
   git clone https://github.com/thebizzareabhishek/autoresponder.git
   cd autoresponder
   ```

2. Open in Android Studio
3. Sync Gradle files
4. Build and run on an Android device (API 24+)

## GitHub Actions

The repository includes a GitHub Actions workflow that automatically builds a release APK on every push to the main branch. The APK is available as a downloadable artifact in the Actions tab.

### Building Locally

To build the release APK locally:
```bash
./gradlew assembleRelease
```

The APK will be available at: `app/build/outputs/apk/release/app-release.apk`

## Permissions

- **Notification Access**: Required to read and reply to messages
- Enable in Settings > Apps > Special access > Notification access

## Configuration

1. Grant notification access permission
2. Enable Auto-Reply toggle
3. Configure AI settings (API key, model, etc.)
4. Select enabled platforms
5. Customize reply messages and behavior

## Supported Platforms

- WhatsApp
- Instagram
- Facebook Messenger
- Telegram
- Twitter/X

## AI Models

- GPT-4o / GPT-4o Mini
- GPT-4 / GPT-3.5 Turbo
- Gemini 2.0 Flash
- DeepSeek Chat
- Custom API

## Developer

**Abhishek Babu**

- GitHub: [@thebizzareabhishek](https://github.com/thebizzareabhishek)
- PayPal: [Support via PayPal](https://www.paypal.me/TheGreatBabaAbhishek)

## License

GPL-3.0

