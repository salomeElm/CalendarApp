# 📅 Interactive Calendar – JavaFX

This is a simple interactive calendar application built with **Java 8** and **JavaFX**.  
The user can select any month and year, view the calendar, and manage daily appointments by clicking on the days.

---

## 🛠 Features

- Select year (1990–2100) and month from ComboBoxes
- Dynamically updates the calendar view
- Clickable buttons for each day
- Popup dialog to add, edit, or remove appointments
- All appointments are stored in memory using `HashMap<String, String>`

---

## 🎥 Demo

![calendar screenshot](screenshot.png)  
*(Optional: add a screenshot of your calendar UI)*

---

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- JavaFX SDK configured
- An IDE like Eclipse or IntelliJ **with JavaFX support**

### Run Instructions
1. Clone or download this repository.
2. Open the project in your IDE.
3. Make sure JavaFX is properly linked in your project settings:
   - For Eclipse: Configure `Build Path` → Add JavaFX SDK to Libraries
4. Run `Main.java` or the appropriate launcher class.
5. The calendar window will open and allow you to interact with the interface.

---

## 📁 File Structure

```plaintext
project/
├── src/
│   └── CalendarController.java
├── resources/
│   └── main.fxml
├── README.md
└── (optional) run.bat or run.sh
