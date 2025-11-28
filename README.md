# 🎮 TeamMate – Intelligent Team Formation System for University Gaming Club

**TeamMate** is a Java-based application designed to help a university gaming club automatically form balanced and diverse teams for tournaments, friendly matches, or inter-university events. By collecting survey data from club members, TeamMate creates teams optimized for skill, role, personality, and game interest.

---

## 🌟 Overview

TeamMate gathers data from members through a quick personality and interest survey and uses an intelligent algorithm to form well-rounded teams based on:

- Game/Sport type
- Skill level
- Preferred playing role
- Personality traits

The system ensures teams are diverse, balanced, and ready for competitive or casual play.

---

## 🛠️ Key Features

### 1️⃣ Input & Survey
- Members complete a **short survey** including:
    - **5 personality questions** (from starter pack)
    - **Interest selection:** e.g., Valorant, Dota, FIFA, Basketball, Badminton
    - **Preferred playing role** (starter pack roles)

---

### 2️⃣ Personality Types
TeamMate categorizes members based on survey scores:

| Personality Type | Score Range |
|-----------------|------------|
| **Leader**      | 90–100     |
| **Balanced**    | 70–89      |
| **Thinker**     | 50–69      |

---

### 3️⃣ Matching Algorithm
The system forms teams of size **N**, ensuring:

- **Diverse interests:** Members with different games/sports
- **Role variety:** e.g., at least 1 defender, 1 strategist, etc.
- **Mixed personality types:** Balanced team dynamics

---

### 4️⃣ File Handling
- **Load data** from a sample CSV file (provided)
- **Save formed teams** into a CSV file (e.g., `formed_teams.csv`)
- Ensures persistent storage for easy review and editing

---

### 5️⃣ Exception Handling
- Handles **missing or invalid inputs**
- Validates **role and personality scores**
- Catches **file read/write errors** for safe operations

---

### 6️⃣ Concurrency
- **Process survey data** using threads for efficiency
- **Form teams in parallel**, especially useful for large datasets

---

## 📁 Project Structure

```plaintext
TeamMateSystem
├── .idea
├── src/
│   ├── TeamMateSystem/
│       ├── Account.java
│       ├── CSVFileHandler.java
│       ├── FillSurveyWorker.java
│       ├── Message.java
│       ├── Organizer.java
│       ├── Participant.java
│       ├── PersonalityClassifier.java
│       ├── SkillBasedTeamBuilder.java
│       ├── Survey.java
│       ├── Team.java
│       ├── TeamBuilder.java
│       ├── TeamMateController.java
│       ├── UI.java
│       └── User.java
│
└── README.md
```

---

### 🚀 How to Use

- Run the TeamMate.java main program.
- Members complete the survey or provide a CSV file of responses.
- The system categorizes members by personality type and validates inputs.
- Form teams based on game interest, role, and personality.
- View the output CSV (formed_teams.csv) for team assignments

---

### 🧩 Technologies Used

- Java 8+ – Core programming language
- CSV file handling – Data input/output
- Threads – Concurrency for processing and team formation
- OOP Principles – Modular and maintainable design
