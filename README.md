# Dependency Inversion Principle (DIP) Training

**Ziel:** Zyklische Abhängigkeiten durch Anwendung des Dependency Inversion Principle eliminieren.

## Aufgaben

| Task | Domäne | Pakete | Details |
|------|--------|--------|---------|
| **A1** | Universität | `course` ↔ `student` | [→ A1](src/main/java/training/a1/README.md) |
| **A2** | Bibliothek | `book` ↔ `author` | [→ A2](src/main/java/training/a2/README.md) |
| **A3** | E-Commerce | `product` ↔ `customer` | [→ A3](src/main/java/training/a3/README.md) |
| **A5** | Projekt-Management | `project` ↔ `developer` | [→ A5](src/main/java/training/a5/README.md) |

## Anleitung

1. **Problem prüfen:** `mvn test -Dtest="training.*.CycleTest"` → alle Tests rot
2. **Aufgabe wählen:** A1, A2, A3 oder A5 README lesen
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

# Einzelne Aufgabe - Cycle Tests
mvn test -Dtest="training.a1.CycleTest"
mvn test -Dtest="training.a2.CycleTest" 
mvn test -Dtest="training.a3.CycleTest"
mvn test -Dtest="training.a5.CycleTest"

# Alle Tests einer Aufgabe
mvn test -Dtest="training.a1.*Test"
mvn test -Dtest="training.a2.*Test"
mvn test -Dtest="training.a3.*Test"
mvn test -Dtest="training.a5.*Test"
```

**Erfolgskriterium:** Alle CycleTests grün + Business-Logic-Tests bleiben grün + Integration-Tests grün
