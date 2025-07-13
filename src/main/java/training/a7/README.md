# Aufgabe A7: Krankenhaus-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `doctor` und `patient` Paketen mit komplexen medizinischen Berechnungsabhängigkeiten eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten zwischen den beiden Domänen
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Domänen-Beschreibung

### Doctor (Arzt)
- Verwaltet zugewiesene Patienten
- Berechnet Arbeitsbelastung basierend auf Patient-Komplexität
- Berechnet durchschnittliche Behandlungsdauer pro Spezialisierung
- Bestimmt verfügbare Kapazität für neue Patienten
- Validiert Behandlungseignung basierend auf Patient-Diagnosen

### Patient
- Hat Diagnosen, Behandlungshistorie und Risikofaktoren
- Berechnet Behandlungskosten basierend auf Doctor-Spezialisierung
- Berechnet Prioritätsscore basierend auf Doctor-Verfügbarkeit
- Validiert Behandlungsplan anhand Doctor-Qualifikationen
- Berechnet optimale Behandlungszeit basierend auf Doctor-Erfahrung

## Tests

### Domain-Tests (Unit Tests)
- `training.a7.doctor.domain.*Test` - Doctor Domain Tests
- `training.a7.patient.domain.*Test` - Patient Domain Tests

### Integration-Tests (Service-Layer)
- `training.a7.DoctorPatientTest` - Cross-Aggregate Integration Tests
  - Tests für Arbeitsbelastungs-Berechnungen
  - Tests für Behandlungskosten-Berechnungen
  - Tests für Prioritäts-Scoring
  - Tests für Kapazitäts-Planungen

### Architektur-Tests
- `training.a7.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a7.CycleTest"` wird grün
- `mvn test -Dtest="training.a7.*.domain.*Test"` bleibt grün
- `mvn test -Dtest="training.a7.DoctorPatientTest"` wird grün

---
[← Zurück zur Übersicht](../../../../../README.md)
