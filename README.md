# Shopping App – CS Senior Project

## About This Project

This Android shopping calculator app was developed as my senior project for the Computer Science program at BYU–Idaho. It's designed to help users track and manage grocery expenses with ease, supporting price entry, persistent sales tax, and a reusable item list. The project is being developed in two phases:

- **Regular Joe**: A simple, user-friendly version built for anyone who wants to manage their shopping totals with customizable features like multi-trip support and checkable items.
- **Regular Joe+**: A future expansion designed for users who receive EBT or WIC assistance, with additional budgeting logic, item categorization, and program-specific behavior.

The current version (Regular Joe) is actively in development and will serve as the clean foundation for the upcoming Regular Joe+ version.

---

## Features

### ✅ Current Features (Regular Joe)
- Add and edit shopping list items with name, quantity/weight, and price
- Automatically calculates and updates a running total including sales tax
- Persistent storage of sales tax percentage—set once, used for every trip
- Save and reuse frequently purchased items through a **"My Items"** list
- Add checked items from "My Items" to an active shopping trip (avoids duplicates)
- Continue unfinished shopping trips where you left off

---

### 🚧 Planned Features (Regular Joe)
- Support for managing **multiple shopping trips** using `shoppingTripID`
- Ability to rename, delete, and switch between saved shopping trips
- Allow users to **check off purchased items**, automatically:
  - Move checked items to the **bottom** of the list
  - Apply a **different color or visual style** to distinguish them

---

### 🌟 Future Expansion: Regular Joe+

This version will be built on top of the Regular Joe foundation and will focus on enhanced budgeting functionality for users who receive government assistance:

- Mark items as **EBT** (exempt from tax) or **WIC** (free or produce-limited)
- Budget rules applied per item type (e.g., no tax for EBT, limits for WIC produce)
- Display **total costs for EBT, WIC, and out-of-pocket items** at the top of the screen
- Modified data models and logic to reflect real-world budgeting scenarios

This version will be developed as a **separate app**, cloned from the finalized Regular Joe codebase to ensure a clean and focused experience.

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
