# 🎯 Dependency Inversion Principle (DIP) Training

**Ziel:** Zyklische Abhängigkeiten durch Anwendung des Dependency Inversion Principle eliminieren.

## 📋 Aufgaben

| Task | Domäne | Pakete | Details |
|------|--------|--------|---------|
| **A1** | 🎓 Universität | `course` ↔ `student` | [→ A1](src/main/java/training/a1/README.md) |
| **A2** | 📚 Bibliothek | `book` ↔ `author` | [→ A2](src/main/java/training/a2/README.md) |
| **A3** | 🛒 E-Commerce | `product` ↔ `customer` | [→ A3](src/main/java/training/a3/README.md) |

## 🚀 Anleitung

1. **Problem prüfen:** `mvn test -Dtest="training.*.CycleTest"` → alle Tests ❌ rot
2. **Aufgabe wählen:** A1, A2 oder A3 README lesen
3. **DIP anwenden:** Zyklische Abhängigkeiten auflösen
4. **Erfolg prüfen:** Tests werden ✅ grün

## 🧪 Tests

```bash
# Alle Cycle-Tests
mvn test -Dtest="training.*.CycleTest"

# Einzelne Aufgabe
mvn test -Dtest="training.a1.CycleTest"
mvn test -Dtest="training.a2.CycleTest" 
mvn test -Dtest="training.a3.CycleTest"
```

**Erfolgskriterium:** Alle CycleTests grün ✅
