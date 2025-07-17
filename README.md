# 🔧 API Automation Framework - RestAssured + Java + Maven

This project is a **lightweight and scalable API Test Automation Framework** built with Java and RestAssured. It is designed to automate RESTful API testing and integrate easily into CI/CD pipelines using tools like Jenkins or GitHub Actions.

---

## 📂 Project Structure

Restassured_FW/
├── src/
│ ├── main/
│ │ └── java/
│ └── test/
│ ├── java/
│ │ ├── base/ # Base classes and reusable setup
│ │ ├── tests/ # Test classes (organized by API modules)
│ │ ├── utils/ # Utility classes: config reader, logger, etc.
│ │ └── data/ # Test data providers
│ └── resources/ # Properties/config files
├── testng.xml # TestNG configuration file
├── pom.xml # Maven project file
└── README.md

---

## ⚙️ Tech Stack

- **Language**: Java 17+
- **Build Tool**: Maven
- **Test Framework**: TestNG
- **HTTP Client**: RestAssured
- **Reporting**: ExtentReports 
- **Logging**: Log4j2 
- **CI/CD**: GitHub Actions / Jenkins ready

## 🚀 Getting Started

### ✅ Prerequisites

- Java 17+
- Maven 3.8+
- IDE like IntelliJ IDEA or Eclipse
- Git (for version control)

### 📥 Clone the repository

```bash
git clone https://github.com/khanntm/Restassured_FW.git
cd Restassured_FW
