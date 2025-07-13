# Aufgabe A5: Projekt-Management-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `project` und `developer` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Domain Beschreibung

Das System verwaltet ein Software-Entwicklungsunternehmen mit folgenden Entitäten:
- **Project**: Software-Projekte mit Technologien, Budget und zugewiesenen Entwicklern
- **Developer**: Entwickler mit Fähigkeiten, Erfahrungslevel und Projektzuweisungen

## Zyklische Abhängigkeiten

Das System enthält zyklische Abhängigkeiten zwischen den Paketen:
- Project ↔ Developer

## Tests

### Integration-Tests (Service-Layer)
- `training.a5.ProjectDeveloperTest` - Cross-Aggregate Integration Tests
  - Tests für Entwicklerzuweisungen
  - Tests für Projektstatistiken
  - Tests für Arbeitsbelastung
  - Tests für Fähigkeitsabgleich

### Architektur-Tests
- `training.a5.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a5.CycleTest"` wird grün
- `mvn test -Dtest="training.a5.ProjectDeveloperTest"` wird grün

---
[← Zurück zur Übersicht](../../../../../README.md)
