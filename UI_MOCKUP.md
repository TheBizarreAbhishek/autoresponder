# AutoResponder - UI Mockup (Updated)

## 📱 Main App Structure

```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue Toolbar with Hamburger Menu
├─────────────────────────────────────┤
│                                     │
│        CONTENT AREA                 │  ← Changes based on navigation
│        (Fragments with Icons)       │
│                                     │
│                                     │
│                                     │
│                                     │
│                                     │
│                                     │
├─────────────────────────────────────┤
│ [🏠] [🌐] [🤖] [⚙️]                │  ← Bottom Navigation (4 items)
│ Dash  Plat  AI   Set               │
└─────────────────────────────────────┘
```

---

## 🏠 Screen 1: Dashboard (Main Screen)

### Layout with Icons:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  ┌───────────────────────────────┐ │
│  │ ℹ️  Service Status            │ │  ← Status Card with Icon
│  │    Service running            │ │
│  │    Permission: Granted        │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📧  Autoreply      [Toggle ○] │ │  ← Autoreply Card with Icon
│  │    Auto-reply is enabled      │ │
│  │                               │ │
│  │    ┌─────────────────────┐   │ │
│  │    │ ✨ AI Reply         │   │ │  ← AI Reply Option (shown when ON)
│  │    └─────────────────────┘   │ │
│  │                               │ │
│  │    ┌─────────────────────┐   │ │
│  │    │ 📝 Preset Replies   │   │ │  ← Preset Replies Option
│  │    └─────────────────────┘   │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📊  Quick Stats               │ │  ← Stats Card with Icon
│  │                               │ │
│  │    150         25             │ │
│  │    Total      Today's        │ │
│  │    Replies    Replies        │ │
│  └───────────────────────────────┘ │
│                                     │
│                                     │
│    Made with ❤️ by Abhishek Babu   │  ← Footer Message
│                                     │
├─────────────────────────────────────┤
│ [🏠] [🌐] [🤖] [⚙️]                │  ← Bottom Nav
└─────────────────────────────────────┘
```

### Features:
- **Service Status Card**: Shows service status and permission with info icon
- **Autoreply Toggle**: Main switch with email icon
- **AI Reply Option**: Appears when Autoreply is ON, with sparkle icon
- **Preset Replies Option**: Button to manage presets, with document icon
- **Quick Stats**: Statistics with chart icon
- **Footer**: "Made with ❤️ by Abhishek Babu" at bottom
- **All cards have icons** for better visual hierarchy

---

## 🌐 Screen 2: Platforms

### Layout with Platform Icons:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  🌐  Supported Platforms            │  ← Title with Icon
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 💬  WhatsApp      [Toggle ○] │ │  ← WhatsApp Card with Icon
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📷  Instagram     [Toggle ○] │ │  ← Instagram Card with Icon
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 👥  Facebook      [Toggle ○] │ │  ← Facebook Card with Icon
│  │    Messenger                 │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ ✈️  Telegram      [Toggle ○] │ │  ← Telegram Card with Icon
│  └───────────────────────────────┘ │
│                                     │
├─────────────────────────────────────┤
│ [🏠] [🌐] [🤖] [⚙️]                │  ← Bottom Nav
└─────────────────────────────────────┘
```

### Features:
- **Platform Icons**: Each platform has a distinctive icon
- **Toggle Switches**: Enable/disable each platform individually
- **Color-coded Icons**: WhatsApp (green), Instagram (pink), etc.

---

## 🤖 Screen 3: AI Configuration

### Layout with Icons:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  🤖  AI Configuration               │  ← Title with Icon
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🔑  API Key                   │ │  ← API Key Section with Icon
│  │    [Enter your API key...]    │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🧠  LLM Model                 │ │  ← Model Section with Icon
│  │    [GPT-4 ▼]                 │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🌍  AI Reply Language         │ │  ← Language Section with Icon
│  │    [English ▼]                │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 🤖  Bot Name                  │ │  ← Bot Name Section with Icon
│  │    [Enter bot name...]        │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📝  Custom AI Prompt          │ │  ← Prompt Section with Icon
│  │    [Enter custom prompt...]    │ │
│  │    (Multi-line text area)     │ │
│  └───────────────────────────────┘ │
│                                     │
├─────────────────────────────────────┤
│ [🏠] [🌐] [🤖] [⚙️]                │  ← Bottom Nav
└─────────────────────────────────────┘
```

### Features:
- **Dedicated AI Config Screen**: Separated from Settings
- **Icon for Each Field**: Visual indicators for each setting
- **Input Fields**: Text inputs and dropdowns for configuration
- **Organized Layout**: All AI-related settings in one place

---

## 📝 Screen 4: Preset Replies

### Layout with Icons:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  📝  Preset Replies                 │  ← Title with Icon
│     Create preset replies...        │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ [➕ Add Preset]               │ │  ← Add Button with Icon
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📝  "how are you"             │ │  ← Preset Item with Icon
│  │    → "I am fine"              │ │
│  │    [✏️] [🗑️]                  │ │  ← Edit/Delete Icons
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📝  "hello"                   │ │
│  │    → "Hi there!"              │ │
│  │    [✏️] [🗑️]                  │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ 📝  "thanks"                  │ │
│  │    → "You're welcome!"         │ │
│  │    [✏️] [🗑️]                  │ │
│  └───────────────────────────────┘ │
│                                     │
│  [➕]                               │  ← Floating Action Button
│                                     │
├─────────────────────────────────────┤
│ [🏠] [🌐] [🤖] [⚙️]                │  ← Bottom Nav
└─────────────────────────────────────┘
```

### Add/Edit Preset Dialog:
```
┌─────────────────────────────────────┐
│  📝  Add Preset Reply               │  ← Dialog Title with Icon
├─────────────────────────────────────┤
│                                     │
│  ✏️  Keyword or phrase              │  ← Input with Icon
│  [________________________]         │
│     Message must contain...        │
│                                     │
│  📧  Reply Message                  │  ← Input with Icon
│  [________________________]         │
│  [________________________]         │
│     Message to send when...         │
│                                     │
│  🔑  Case Sensitive    [Toggle ○]  │  ← Switch with Icon
│                                     │
│  🔑  Exact Match       [Toggle ○]  │  ← Switch with Icon
│                                     │
│  [Cancel]  [Save]                   │  ← Action Buttons
└─────────────────────────────────────┘
```

### Features:
- **Preset List**: Shows all preset replies with icons
- **Add Button**: Both top button and floating action button
- **Edit/Delete**: Action buttons on each item
- **Dialog with Icons**: Add/Edit dialog has icons for all fields
- **Empty State**: Shows icon when no presets exist

---

## ⚙️ Screen 5: Settings

### Layout:
```
┌─────────────────────────────────────┐
│  [←]  Settings                      │  ← Back arrow + title
├─────────────────────────────────────┤
│                                     │
│  General                            │  ← Category header
│  ├─ Enable Auto-Reply        [○]   │
│  ├─ Default Reply Message          │
│  ├─ Group Reply              [○]   │
│  ├─ Reply Prefix                   │
│  ├─ Max Replies Per Person/Day     │
│  ├─ Natural Reply Delay      [○]   │
│  └─ Custom Delay (seconds)         │
│                                     │
│  (AI Configuration moved to         │
│   separate AI Config screen)        │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- **General Settings Only**: AI settings moved to AI Config screen
- **Preference Screen**: Uses Android PreferenceFragment
- **Back Navigation**: Returns to previous screen

---

## 🍔 Hamburger Menu (Drawer Navigation)

### Layout with Icons:
```
┌─────────────────────────────────────┐
│  AutoResponder                      │  ← Header (Blue background)
│  Auto-Reply Assistant               │
├─────────────────────────────────────┤
│  🏠  Dashboard                      │  ← Menu items with icons
│  🌐  Platforms                      │
│  🤖  AI Configuration               │
│  📜  Chat History                   │
│  ⚙️  Settings                       │
│                                     │
│  ───────────────────────────────    │  ← Divider
│                                     │
│  ℹ️  About                          │  ← Secondary menu
└─────────────────────────────────────┘
```

### Features:
- **Icons for All Items**: Visual navigation
- **AI Configuration**: Separate menu item
- **Slide-out Drawer**: Opens from left
- **Header Section**: App name and subtitle

---

## 🎨 Design Elements

### Icons Used:
- **Dashboard**: 🏠 (Home icon)
- **Platforms**: 🌐 (Globe icon)
- **AI Config**: 🤖 (Robot icon)
- **Settings**: ⚙️ (Gear icon)
- **History**: 📜 (Scroll icon)
- **Service Status**: ℹ️ (Info icon)
- **Autoreply**: 📧 (Email icon)
- **AI Reply**: ✨ (Sparkle icon)
- **Preset Replies**: 📝 (Document icon)
- **Stats**: 📊 (Chart icon)
- **WhatsApp**: 💬 (Chat icon)
- **Instagram**: 📷 (Camera icon)
- **Facebook**: 👥 (People icon)
- **Telegram**: ✈️ (Paper plane icon)
- **API Key**: 🔑 (Key icon)
- **Model**: 🧠 (Brain icon)
- **Language**: 🌍 (Globe icon)
- **Bot Name**: 🤖 (Robot icon)
- **Prompt**: 📝 (Document icon)
- **Add**: ➕ (Plus icon)
- **Edit**: ✏️ (Pencil icon)
- **Delete**: 🗑️ (Trash icon)

### Colors:
- **Primary**: Blue (#2196F3) - Toolbar, buttons, accents
- **Primary Dark**: Dark Blue (#1976D2)
- **Accent**: Teal (#03DAC5)
- **Background**: Light Gray (#F5F5F5)
- **Surface**: White (#FFFFFF) - Cards
- **Text Primary**: Dark Gray (87% opacity)
- **Text Secondary**: Medium Gray (54% opacity)

### Typography:
- **App Title**: 20sp, bold, white (on toolbar)
- **Card Titles**: 18sp, bold, dark gray
- **Card Subtitles**: 14sp, medium gray
- **Stats Numbers**: 24sp, bold, colored
- **Stats Labels**: 12sp, medium gray

### Cards:
- **Corner Radius**: 16dp
- **Elevation**: 4dp (shadow)
- **Padding**: 20dp (internal)
- **Margin**: 16dp-24dp (spacing)

---

## 🔄 Navigation Flow

```
Main Activity (DrawerLayout)
    │
    ├─ Dashboard (Default)
    │   └─ Toggle Autoreply
    │   └─ AI Reply Option
    │   └─ Preset Replies Option
    │   └─ View Stats
    │
    ├─ Platforms
    │   └─ Enable/Disable platforms
    │
    ├─ AI Configuration (NEW)
    │   └─ Configure AI settings
    │   └─ API Key, Model, Language, etc.
    │
    ├─ Preset Replies (NEW)
    │   └─ Create/Edit/Delete presets
    │
    ├─ History
    │   └─ View chat history
    │
    └─ Settings
        └─ Configure general behavior
```

### Bottom Navigation (4 items):
- **🏠 Dashboard**: Main screen (default)
- **🌐 Platforms**: Platform management
- **🤖 AI Config**: AI configuration (NEW)
- **⚙️ Settings**: General settings

### Drawer Menu:
- Same options as bottom navigation
- Plus "About" option
- All items have icons

---

## 🎯 Key Interactions

1. **Toggle Autoreply**: 
   - Tap switch on Dashboard
   - Shows AI Reply and Preset Replies options
   - Service starts/stops automatically

2. **AI Reply**:
   - Appears when Autoreply is ON
   - Requires API key (configured in AI Config)
   - Enables AI-powered responses

3. **Preset Replies**:
   - Tap to open Preset Replies screen
   - Create keyword-based replies
   - Priority over AI/default replies

4. **AI Configuration**:
   - Dedicated bottom nav item
   - Configure all AI settings in one place
   - Separate from general settings

5. **Platform Management**:
   - Enable/disable platforms individually
   - Each platform has distinctive icon
   - Changes apply immediately

---

## ✨ Recent Updates

### Added Features:
- ✅ **Icons throughout the app** for better visual hierarchy
- ✅ **AI Configuration** as separate bottom nav item
- ✅ **Preset Replies** feature with full CRUD
- ✅ **"Made with ❤️ by Abhishek Babu"** footer on dashboard
- ✅ **Updated navigation** with 4 bottom nav items
- ✅ **Icon-based UI** for all screens and components

### UI Improvements:
- All cards have icons
- All navigation items have icons
- All input fields have icons
- All action buttons have icons
- Consistent icon sizing and colors
- Better visual hierarchy

---

## 📝 Notes

This mockup reflects the current implementation with:
- Material Design 3 components
- Icon-enhanced UI throughout
- Separated AI Configuration
- Preset Replies feature
- Updated navigation structure
- Footer message on dashboard

All icons are vector drawables that adapt to light/dark themes.
