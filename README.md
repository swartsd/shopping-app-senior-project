# Shopping App – CS Senior Project

## About This Project

This Android shopping calculator app was developed as my senior project for the Computer Science program at BYU–Idaho. It's designed to help users track and manage grocery expenses with ease, supporting price entry, persistent sales tax, and a reusable item list.

The current version is actively in development and focuses on providing a clean, simple foundation for tracking shopping expenses. A future version is planned to support advanced budgeting features for users receiving EBT or WIC assistance.

---

## Features

### ✅ Current Features
- Add and edit shopping list items with name, quantity/weight, and price
- Automatically calculates and updates a running total including sales tax
- Persistent storage of sales tax percentage—set once, used for every trip
- Save and reuse frequently purchased items through a **"My Items"** list
- Add checked items from "My Items" to an active shopping trip (avoids duplicates)
- Continue unfinished shopping trips where you left off

---

### 🚧 Planned Features
- Support for managing **multiple shopping trips** using `shoppingTripID`
- Ability to rename, delete, and switch between saved shopping trips
- Allow users to **check off purchased items**, automatically:
  - Move checked items to the **bottom** of the list
  - Apply a **different color or visual style** to distinguish them

---

### 🌟 Future Expansion: EBT/WIC Support

A future version of this app is planned to provide enhanced budgeting functionality for users who receive government assistance:

- Mark items as **EBT** (exempt from tax) or **WIC** (free or produce-limited)
- Budget rules applied per item type (e.g., no tax for EBT, limits for WIC produce)
- Display **total costs for EBT, WIC, and out-of-pocket items** at the top of the screen
- Modified data models and logic to reflect real-world budgeting scenarios

This version will be developed as a **separate app**, cloned from the finalized shopping app codebase to ensure a clean and focused experience.

---

## Technologies Used
- **Java** (core app logic)
- **Android Studio** (development environment and UI)
- **Room** (local database for persistent storage)
- **MVVM Architecture** (for clean and modular code organization)

---

## Getting Started

To run the app on your local machine:

1. Clone this repository:
   ```bash
   git clone https://github.com/swartsd/shopping-app-senior-project.git
