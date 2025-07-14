# Aufgabe A7: Krankenhaus-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `doctor` und `patient` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten zwischen den beiden Domänen
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Herausforderung

Die Domänen haben eine **bidirektionale Beziehung**:
- **Doctor** muss Patienten zuweisen können
- **Patient** muss einem Arzt zugewiesen werden können
- **Services** brauchen Zugriff auf beide Domänen für Berechnungen

**Warum entsteht der Zyklus?**
- `DoctorService` braucht `PatientRepository` für Patientenlisten
- `PatientService` braucht `DoctorService` für Behandlungskosten-Berechnungen
- Domain-Objekte referenzieren sich teilweise direkt

## Domänen-Beschreibung

### Doctor (Arzt)
- Verwaltet Name, Spezialisierung und Stundensatz
- Kann Patienten zugewiesen bekommen
- Einfache Datenstruktur ohne komplexe Berechnungen

### Patient  
- Hat Name, Diagnose und Priorität
- Kann einem Arzt zugewiesen werden
- Einfache Datenstruktur ohne komplexe Berechnungen

## Tests

### Domain-Tests (Unit Tests)
- `training.a7.doctor.domain.DoctorTest` - Doctor Domain Tests
- `training.a7.patient.domain.PatientTest` - Patient Domain Tests

### Integration-Tests (Service-Layer)
- `training.a7.DoctorPatientTest` - Cross-Aggregate Integration Tests
  - Tests für Behandlungskosten-Berechnungen (zyklische Abhängigkeit)
  - Tests für Arzt-Patient-Zuweisungen
  - Tests für Spezialisierungs-Filterung
  - Tests für Prioritäts-Filterung

### Architektur-Tests
- `training.a7.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a7.CycleTest"` wird grün
- `mvn test -Dtest="training.a7.*.domain.*Test"` bleibt grün
- `mvn test -Dtest="training.a7.DoctorPatientTest"` wird grün

---
[← Zurück zur Übersicht](../../../../../README.md)
