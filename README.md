# AyudaDesk: Financial Aid Application and Processing System

A Java Swing desktop application for a barangay office that handles emergency aid applications. Staff log in, register applicants (Regular or PWD), submit aid applications for them, and review pending applications. PWD applicants are given priority.

Group 1 final project for Object-Oriented Programming.

## Features

1. **Staff login** with a 3-attempt limit.
2. **Applicant registration** for Regular and PWD applicants.
3. **Emergency aid submission** with an automatic completeness check. An incomplete application is returned to staff for correction.
4. **Staff dashboard** that lists pending applications with PWD applicants first, with Approve and Reject buttons.

Also included: update applicant details, and a table that shows every applicant and their current status.

## Test accounts

| Staff ID | Password | Name |
|---|---|---|
| S001 | admin123 | Maria Reyes |
| S002 | staff456 | Jose Santos |

Contact numbers must be 11 digits starting with `09` (example: `09171234567`).

## OOP concepts used

- **Encapsulation:** private fields with validating getters and setters.
- **Inheritance:** `RegularApplicant` and `PWDApplicant` extend the abstract `Applicant` class.
- **Polymorphism:** `getPriorityLevel()` returns a different value for each applicant type, and the dashboard sorts by it.
- **Abstraction:** `Applicant` is abstract, and `AidApplication` hides its validation and status tracking behind `submitApplication()`, `approveApplication()` and `rejectApplication()`.

## Project structure

```
AyudaDesk/
  src/ayudadesk/
    Main.java                      starts the program
    model/                         the data and the rules
      Applicant.java               abstract parent class
      RegularApplicant.java
      PWDApplicant.java
      AidApplication.java
      Staff.java
      AppData.java                 holds all data while the program runs
    ui/                            the Swing windows
      LoginFrame.java
      MainFrame.java
      RegisterApplicantDialog.java
      UpdateApplicantDialog.java
      SubmitApplicationDialog.java
      ReviewApplicationsDialog.java
      ViewApplicantsDialog.java
      UiUtil.java
```

## How to run it on your own laptop

### What you need

- **JDK 17 or newer** (a JDK, not just a JRE). Check with `java -version` and `javac -version` in a terminal.
- **NetBeans** (recommended), or just a terminal.
- **Git**, to download the code.

### Step 1: Download the code

Open a terminal (Command Prompt on Windows), go to the folder where you keep your projects, and run:

```
git clone https://github.com/villanuevarav/OOP-AyudaDesk-.git
```

This creates a folder named `OOP-AyudaDesk-`. This is a private repository, so you must accept the collaborator invitation (check your email) and sign in to GitHub when Git asks.

If you don't use Git, open the repository page, click **Code**, then **Download ZIP**, and unzip it.

### Step 2A: Run it in NetBeans (recommended)

1. Open NetBeans and choose **File** > **Open Project**.
2. Select the downloaded folder (the one that contains `src` and `build.xml`) and click **Open Project**.
3. Wait for the project to appear in the **Projects** tab. If you see a warning about a missing Java platform, go to **Tools** > **Java Platforms** and make sure a JDK is added.
4. Right-click the project and choose **Clean and Build** (or press Shift+F11).
5. Expand **Source Packages** > **ayudadesk**, right-click **Main.java** and choose **Run File** (or press Shift+F6).
6. The login window opens. Log in with `S001` and `admin123`.

If NetBeans says the project has no main class, right-click the project, choose **Properties** > **Run**, click **Browse** next to Main Class, and pick `ayudadesk.Main`.

### Step 2B: Run it from the terminal (no NetBeans)

On Windows, open Command Prompt inside the downloaded folder and run:

```
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
java -cp out ayudadesk.Main
```

On Mac or Linux:

```
find src -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out ayudadesk.Main
```

You can delete `sources.txt` and the `out` folder afterwards. They are only needed to run the program.

## Try it out (a quick test)

1. Log in with `S001` and `admin123`.
2. Register one Regular and one PWD applicant with complete details.
3. Open **Submit Emergency Aid Application** and submit both, with an amount like `500`.
4. Open **Review Pending Applications**. The PWD applicant should be listed first. Approve one and reject the other.
5. Open **View Applicants and Status** to see Approved and Rejected.

## Troubleshooting

- **Red errors in NetBeans:** run **Clean and Build**. If errors remain, check that you opened the folder that contains `src` and `build.xml`, and that your JDK is version 17 or newer.
- **"package does not exist" or "class not found":** the folder structure is wrong. `Main.java` must sit directly in `src/ayudadesk`, with the `model` and `ui` folders next to it.
- **`javac` is not recognized:** you have a JRE but not a JDK, or Java is not on your PATH. Install a JDK and open a new terminal.
- **The window does not appear:** check the Output tab in NetBeans (or the terminal) for an error message.

## Known limits

- Data is kept in memory only. Closing the program clears all applicants and applications.
- Staff passwords are plain text and the two staff accounts are hard-coded. This is acceptable for a school prototype.
- Each list holds at most 100 items.

## Working together

1. Run `git pull` before you start, so you have the latest version.
2. Change one thing at a time, and tell the group which file you are editing.
3. Then run:

```
git add .
git commit -m "describe what you changed"
git push
```

If Git reports a conflict, do not force the push. Ask the group for help first.
