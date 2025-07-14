# Dependency Inversion Principle (DIP) Training

**Ziel:** Zyklische Abhängigkeiten durch Anwendung des Dependency Inversion Principle eliminieren.

## Aufgaben

| Task | Domäne | Pakete | Details |
|------|--------|--------|---------|
| **A1** | Universität | `course` ↔ `student` | [→ A1](src/main/java/training/a1/README.md) |
| **A2** | Bibliothek | `book` ↔ `author` | [→ A2](src/main/java/training/a2/README.md) |
| **A3** | E-Commerce | `product` ↔ `customer` | [→ A3](src/main/java/training/a3/README.md) |
| **A5** | Projekt-Management | `project` ↔ `developer` | [→ A5](src/main/java/training/a5/README.md) |
| **A6** | Kindergarten | `child` ↔ `friendsgroup` ↔ `kindergartengroup` | [→ A6](src/main/java/training/a6/README.md) |
| **A7** | Krankenhaus | `doctor` ↔ `patient` | [→ A7](src/main/java/training/a7/README.md) |

> **💡 Hinweis:** Alle Aufgaben wurden getestet und sind lösbar. Jede Aufgabe hat funktionierende Business-Logic-Tests und eine klare DIP-Lösung.

## Anleitung

1. **Problem prüfen:** `mvn test -Dtest="training.*.CycleTest"` → alle Tests rot
2. **Aufgabe wählen:** A1, A2, A3, A5, A6 oder A7 README lesen
3. **DIP anwenden:** Zyklische Abhängigkeiten auflösen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben
5. **Erfolg prüfen:** Tests werden grün

## Tests

```bash
# Alle Cycle-Tests
mvn test -Dtest="training.*.CycleTest"

# Business-Logic-Tests (müssen grün bleiben)
mvn test -Dtest="training.*.*.domain.*Test"

# Integration-Tests (Service-Layer Tests)
mvn test -Dtest="training.a1.StudentCourseTest"
mvn test -Dtest="training.a2.AuthorBookTest"
mvn test -Dtest="training.a3.CustomerProductTest"
mvn test -Dtest="training.a5.ProjectDeveloperTest"
mvn test -Dtest="training.a6.KindergartenIntegrationTest"
mvn test -Dtest="training.a7.DoctorPatientTest"

# Einzelne Aufgabe - Cycle Tests
mvn test -Dtest="training.a1.CycleTest"
mvn test -Dtest="training.a2.CycleTest" 
mvn test -Dtest="training.a3.CycleTest"
mvn test -Dtest="training.a5.CycleTest"
mvn test -Dtest="training.a6.CycleTest"
mvn test -Dtest="training.a7.CycleTest"

# Alle Tests einer Aufgabe
mvn test -Dtest="training.a1.*Test"
mvn test -Dtest="training.a2.*Test"
mvn test -Dtest="training.a3.*Test"
mvn test -Dtest="training.a5.*Test"
mvn test -Dtest="training.a6.*Test"
mvn test -Dtest="training.a7.*Test"
```

**Erfolgskriterium:** Alle CycleTests grün + Business-Logic-Tests bleiben grün + Integration-Tests grün
