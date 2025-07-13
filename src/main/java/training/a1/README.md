# Aufgabe A1: Universitäts-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `course` und `student` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Tests

### Domain-Tests (Unit Tests)
- `training.a1.course.domain.*Test` - Course Domain Tests
- `training.a1.student.domain.*Test` - Student Domain Tests

### Integration-Tests (Service-Layer)
- `training.a1.StudentCourseTest` - Cross-Aggregate Integration Tests
  - Tests für ECTS-Berechnungen
  - Tests für Kurs-Einschreibungen  
  - Tests für Student-Kurs-Beziehungen

### Architektur-Tests
- `training.a1.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a1.CycleTest"` wird grün
- `mvn test -Dtest="training.a1.*.domain.*Test"` bleibt grün
- `mvn test -Dtest="training.a1.StudentCourseTest"` wird grün

---
[← Zurück zur Übersicht](../../../../../README.md)
