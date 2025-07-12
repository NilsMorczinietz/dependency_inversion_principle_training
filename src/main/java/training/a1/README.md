# Aufgabe A1: Universitäts-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `course` und `student` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Erfolgskriterium

- `mvn test -Dtest="training.a1.CycleTest"` wird grün
- `mvn test -Dtest="training.a1.*.domain.*Test"` bleibt grün

---
[← Zurück zur Übersicht](../../../README.md)
