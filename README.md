# 🗡️ AI-Powered OOP Text RPG

An interactive Object-Oriented Java Text RPG featuring **real-time AI narration via local LLM**, **native Windows Text-to-Speech audio**, and a **dark-themed Java Swing desktop GUI**.

Built as a  Computer Science Engineering Minor Project.

---

## ✨ Features

- 🤖 **Local AI Narration:** Communicates asynchronously with a local Ollama instance (`phi3` model) to generate dynamic, simple room descriptions in real time.
- 🗣️ **Native Text-to-Speech:** Uses Windows `System.Speech` via PowerShell to read AI descriptions out loud without external Java dependencies.
- 🖥️ **Java Swing Desktop GUI:** Styled dark interface featuring health/inventory status monitors, scrollable story log, interactive directional buttons (`North`, `South`, `East`, `West`, `Attack`, `Take`), and command input.
- 🏗️ **Solid OOP Design:** Built cleanly using core Object-Oriented Programming concepts (Encapsulation, Modular Class Structures, Event-Driven Architecture, Multi-Threading).

---

## 🛠️ Project Structure

```text
├── Main.java         # Entry point (Launches GUI safely on Event Dispatch Thread)
├── GameWindow.java   # Java Swing GUI layout, event handling, and combat logic
├── AIManager.java    # Native HttpClient handler for Ollama LLM & Windows TTS
├── Player.java       # Player stats, health management, and inventory tracking
├── Room.java         # World map nodes with directional pointers, items, and monsters
└── Monster.java      # Monster properties, damage calculation, and health tracking





🎮 How to Play

​Movement: Click North, South, East, or West buttons (or type directional commands into the input box).
​Combat: Click Attack to battle monsters blocking your path.
​Items: Click Take to grab potions and items found in rooms. Type drink to restore health.
​Quit: Type quit to exit the game.









---

## 👤 Author

- **Khushkaranpreet Kaur**
- **Institution:** Punjabi University, Patiala
- **Course:** Bachelor of Technology in Computer Science Engineering 
