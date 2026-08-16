# Modern Mobile Text Editor with Incremental Version Control

**IS2205 – Mobile Application Design and Development Mini-Project**

## Overview

This project is a lightweight Android text editor designed for developers and technical writers. It provides essential text editing and file management features along with Kotlin and Markdown syntax highlighting, crash recovery, and an incremental version control system for efficiently managing file versions.

## Features

* Create, open, save, and save-as files
* Recent files management
* UTF-8 encoding support
* Word wrapping
* Undo and redo
* Search and search-and-replace
* Kotlin syntax highlighting
* Markdown syntax highlighting
* Automatic temporary backup for crash recovery
* Read-only file support
* Named or numbered file versions
* Delta-based version storage
* Version comparison using diff
* Restore previous versions
* Local persistence using SQLite/Room

## Version Control

The application stores the initial file as the base version. For subsequent versions, instead of storing a complete copy of the file, the changes are calculated and stored as deltas/patches.

This reduces unnecessary storage duplication while allowing previous versions to be compared and restored.

## Technologies

* Android
* Kotlin
* SQLite / Room Persistence Library
* java-diff-utils
* Git and GitHub
* Jetpack Compose

## Team Members

| Member         | Contribution                     |
| -------------- | -------------------------------- |
| P.C.Perera     | Editor and File Management       |
| G.H.D.Savindya | Syntax Highlighting and Markdown |
| V.P.O.Maharage | Version Control and Database     |

## Getting Started

### Prerequisites

* Android Studio
* Android SDK
* Android device or emulator

### Installation

1. Clone the repository:

```bash
git clone <repository-url>
```

2. Open the project in Android Studio.
3. Allow Gradle to sync and download the required dependencies.
4. Connect an Android device or start an emulator.
5. Build and run the application.

## Course

**IS2205 – Mobile Application Design and Development**
**Mini-Project: Modern Mobile Text Editor with Incremental Version Control**
