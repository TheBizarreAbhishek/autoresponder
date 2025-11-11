# AutoResponder - UI Design Guide

This document provides a visual representation of how the app will look and function.

## 📱 Main App Structure

```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Top Toolbar (Blue)
├─────────────────────────────────────┤
│                                     │
│        CONTENT AREA                 │  ← Changes based on navigation
│        (Fragments)                  │
│                                     │
│                                     │
│                                     │
│                                     │
│                                     │
│                                     │
├─────────────────────────────────────┤
│  [Dashboard] [Platforms] [Settings]│  ← Bottom Navigation (3 items)
└─────────────────────────────────────┘
```

---

## 🏠 Screen 1: Dashboard (Main Screen)

### Layout Structure:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Service Status                │ │  ← Status Card (White card)
│  │ Service running               │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Autoreply          [Toggle ○] │ │  ← Autoreply Card
│  │ Auto-reply is enabled         │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ AI Reply           [Toggle ○] │ │  ← AI Reply Card
│  │ AI-powered replies enabled    │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Quick Stats                   │ │  ← Stats Card
│  │                               │ │
│  │  150         25               │ │
│  │ Total      Today's            │ │
│  │ Replies    Replies            │ │
│  └───────────────────────────────┘ │
│                                     │
├─────────────────────────────────────┤
│  [Dashboard] [Platforms] [Settings]│  ← Bottom Nav
└─────────────────────────────────────┘
```

### Features:
- **Service Status Card**: Shows if the service is running, permission status
- **Autoreply Toggle**: Main switch to enable/disable auto-replies
- **AI Reply Toggle**: Switch to enable AI-powered replies (requires API key)
- **Quick Stats**: Shows total replies sent and today's replies count
- **Cards**: All cards have rounded corners (16dp), elevation/shadow, white background
- **Spacing**: 24dp padding around content, 16dp between cards

---

## 🌐 Screen 2: Platforms

### Layout Structure:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  Supported Platforms                │  ← Title (20sp, bold)
│                                     │
│  ┌───────────────────────────────┐ │
│  │ WhatsApp          [Toggle ○]  │ │  ← Platform Card
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Instagram         [Toggle ○]  │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Facebook Messenger [Toggle ○] │ │
│  └───────────────────────────────┘ │
│                                     │
│  ┌───────────────────────────────┐ │
│  │ Telegram          [Toggle ○]  │ │
│  └───────────────────────────────┘ │
│                                     │
├─────────────────────────────────────┤
│  [Dashboard] [Platforms] [Settings]│  ← Bottom Nav
└─────────────────────────────────────┘
```

### Features:
- **Platform Cards**: Each platform has its own card with toggle switch
- **Toggle States**: On/Off switches for each platform
- **Scrollable**: Can scroll if more platforms are added
- **Same Card Style**: Rounded corners, elevation, white background

---

## 📜 Screen 3: History (Placeholder)

### Layout Structure:
```
┌─────────────────────────────────────┐
│  [☰]  AutoResponder                 │  ← Blue toolbar
├─────────────────────────────────────┤
│                                     │
│  Chat History                       │  ← Title (20sp, bold)
│                                     │
│  [RecyclerView - To be implemented] │  ← Will show chat history
│                                     │
│                                     │
│                                     │
│                                     │
│                                     │
├─────────────────────────────────────┤
│  [Dashboard] [Platforms] [Settings]│  ← Bottom Nav
└─────────────────────────────────────┘
```

### Features:
- **Chat History List**: Will display past conversations
- **Filtering**: Can filter by platform, sender, date (future feature)
- **Scrollable List**: RecyclerView for efficient scrolling

---

## ⚙️ Screen 4: Settings

### Layout Structure:
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
│  AI Configuration                   │  ← Category header
│  ├─ Enable AI Reply          [○]   │
│  ├─ API Key                         │
│  ├─ LLM Model                       │
│  ├─ AI Reply Language               │
│  ├─ Bot Name                        │
│  └─ Custom AI Prompt                │
│                                     │
└─────────────────────────────────────┘
```

### Features:
- **Preference Screen**: Uses Android PreferenceFragment
- **Categorized**: General and AI Configuration sections
- **Toggle Switches**: For boolean settings
- **Edit Text**: For text/number inputs
- **List Preferences**: For dropdown selections (models, languages)
- **Back Navigation**: Back arrow to return to main screen

---

## 🍔 Hamburger Menu (Drawer Navigation)

### Layout Structure:
```
┌─────────────────────────────────────┐
│  AutoResponder                      │  ← Header (Blue background)
│  Auto-Reply Assistant               │
├─────────────────────────────────────┤
│  📍 Dashboard                       │  ← Menu items
│  🌐 Platforms                       │
│  📜 Chat History                    │
│  ⚙️ Settings                        │
│                                     │
│  ───────────────────────────────    │  ← Divider
│                                     │
│  ℹ️ About                           │  ← Secondary menu
└─────────────────────────────────────┘
```

### Features:
- **Slide-out Drawer**: Opens from left when hamburger icon is tapped
- **Header Section**: App name and subtitle (blue background)
- **Menu Items**: Navigation options with icons
- **About Section**: Separated at bottom
- **Overlay**: Darkens main content when drawer is open

---

## 🎨 Design Elements

### Colors:
- **Primary**: Blue (#2196F3) - Toolbar, buttons, accents
- **Primary Dark**: Dark Blue (#1976D2) - Darker shades
- **Accent**: Teal (#03DAC5) - Highlights, stats
- **Background**: Light Gray (#F5F5F5) - Main background
- **Surface**: White (#FFFFFF) - Cards, surfaces
- **Text Primary**: Dark Gray (87% opacity black)
- **Text Secondary**: Medium Gray (54% opacity black)

### Typography:
- **App Title**: 20sp, bold, white (on toolbar)
- **Card Titles**: 18sp, bold, dark gray
- **Card Subtitles**: 14sp, medium gray
- **Stats Numbers**: 24sp, bold, colored (blue/teal)
- **Stats Labels**: 12sp, medium gray

### Cards:
- **Corner Radius**: 16dp (rounded corners)
- **Elevation**: 4dp (shadow effect)
- **Padding**: 20dp (internal padding)
- **Margin**: 16dp-24dp (spacing between cards)

### Spacing:
- **Screen Padding**: 24dp (edges of screen)
- **Card Spacing**: 16dp (between cards)
- **Internal Padding**: 20dp (inside cards)

---

## 🔄 Navigation Flow

```
Main Activity (DrawerLayout)
    │
    ├─ Dashboard (Default)
    │   └─ Toggle Autoreply
    │   └─ Toggle AI Reply
    │   └─ View Stats
    │
    ├─ Platforms
    │   └─ Enable/Disable platforms
    │
    ├─ History
    │   └─ View chat history (future)
    │
    └─ Settings
        └─ Configure bot behavior
        └─ Configure AI settings
```

### Bottom Navigation:
- **Dashboard**: Main screen (default)
- **Platforms**: Platform management
- **Settings**: App settings

### Drawer Menu:
- Same options as bottom navigation
- Plus "About" option
- Can be accessed from any screen

---

## 📐 Responsive Design

- **Card Width**: Full width (match_parent) with 24dp margins
- **Scrollable**: Content scrolls if it exceeds screen height
- **Adaptive**: Works on different screen sizes
- **Material Design 3**: Follows Material Design guidelines

---

## 🎯 Key Interactions

1. **Toggle Autoreply**: 
   - Tap switch on Dashboard
   - Service starts/stops automatically
   - Status updates in real-time

2. **Toggle AI Reply**:
   - Requires API key to be set
   - Enables AI-powered responses
   - Falls back to default message if AI fails

3. **Platform Management**:
   - Enable/disable platforms individually
   - Only enabled platforms will receive auto-replies
   - Changes apply immediately

4. **Settings**:
   - Tap Settings in bottom nav or drawer
   - Configure all app preferences
   - Changes saved automatically

5. **Hamburger Menu**:
   - Tap hamburger icon (☰) in toolbar
   - Drawer slides in from left
   - Tap outside or back to close

---

## 🚀 Future Enhancements (Not Yet Implemented)

- **History Screen**: Chat history with filters
- **Platform Icons**: Custom icons for each platform
- **Dark Mode**: Full dark theme support
- **Statistics Charts**: Visual graphs for stats
- **Reply Preview**: Preview AI responses before sending
- **Message Filtering**: Filter by platform, sender, date
- **Export History**: Export chat history to file

---

## 📝 Notes for Customization

If you want to make changes:

1. **Colors**: Edit `app/src/main/res/values/colors.xml`
2. **Layouts**: Edit XML files in `app/src/main/res/layout/`
3. **Strings**: Edit `app/src/main/res/values/strings.xml`
4. **Theme**: Edit `app/src/main/res/values/themes.xml`
5. **Icons**: Replace drawable resources in `app/src/main/res/drawable/`

The app follows Material Design 3 principles and uses Material Components for a modern, polished look.

