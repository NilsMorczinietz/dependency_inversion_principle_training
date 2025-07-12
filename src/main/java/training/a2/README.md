# Aufgabe A2: Bibliotheks-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `book` und `author` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Tests

### Domain-Tests (Unit Tests)
- `training.a2.author.domain.*Test` - Author Domain Tests
- `training.a2.book.domain.*Test` - Book Domain Tests

### Integration-Tests (Service-Layer)
- `training.a2.AuthorBookTest` - Cross-Aggregate Integration Tests
  - Tests für Buchanzahl pro Autor
  - Tests für produktive Autoren
  - Tests für Seitenzahl-Berechnungen
  - Tests für Autor-Buch-Beziehungen

### Architektur-Tests
- `training.a2.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a2.CycleTest"` wird grün
- `mvn test -Dtest="training.a2.*.domain.*Test"` bleibt grün
- `mvn test -Dtest="training.a2.AuthorBookTest"` wird grün

---
[← Zurück zur Übersicht](../../../README.md)
