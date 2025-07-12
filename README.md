# Dependency Inversion Principle (DIP) Training

Dieses Projekt dient als Training für die Anwendung des **Dependency Inversion Principle** (DIP), einem der SOLID-Prinzipien.

## Aufgabenstellung

### Ziel
Das Ziel ist es, zyklische Abhängigkeiten zwischen Paketen durch die Anwendung des Dependency Inversion Principle zu eliminieren.

### Aktueller Zustand
Das Projekt enthält momentan eine zyklische Abhängigkeit zwischen den Paketen:
- `training.a1.course` 
- `training.a1.student`

Diese zyklische Abhängigkeit führt dazu, dass der ArchUnit-Test `CycleTest` fehlschlägt.

## Aufgaben

### Aufgabe A1: Zyklische Abhängigkeiten auflösen
**Paket:** `training.a1`

**Problem:** 
- `CourseService` ist abhängig von `StudentRepository` und `Student`
- `StudentService` ist abhängig von `CourseService` und `Course`
- Dies führt zu einer zyklischen Abhängigkeit zwischen den Paketen

**Erfolgskriterium:**
Wenn das DIP korrekt angewendet wurde, läuft der Test `CycleTest.testNoCyclicDependenciesBetweenPackages()` erfolgreich durch (grün).

Der Test `training.a1.CycleTest` prüft, ob zyklische Abhängigkeiten zwischen den Paketen eliminiert wurden.
