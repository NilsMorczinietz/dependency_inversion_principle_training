# Aufgabe A6: Kindergarten-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `child`, `friendsgroup` und `kindergartengroup` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Hinweise zur Lösung

**Entfernung von Code:** Es ist ausdrücklich erlaubt, unnötige Code-Teile zu entfernen, die nicht benötigt werden und auch nicht von Tests abgedeckt sind. Dies ist sogar erwünscht, um eine saubere Architektur zu erreichen.

**Fokus auf getestete Funktionalität:** Konzentrieren Sie sich auf die Funktionalität, die durch die Tests validiert wird. Alles andere kann entfernt werden, wenn es zur Auflösung der zyklischen Abhängigkeiten beiträgt.

## Domain Beschreibung

Das System verwaltet einen Kindergarten mit folgenden Entitäten:
- **Child**: Kinder mit Namen, Alter und Gruppenzugehörigkeiten
- **FriendsGroup**: Freundesgruppen mit Aktivitäten (informelle Gruppen)
- **KindergartenGroup**: Offizielle Kindergartengruppen mit Betreuern und Kapazitätsgrenzen

## Korrekte Abhängigkeitsrichtung

Die **richtigen** Abhängigkeiten sollten von spezifisch zu allgemein verlaufen:
- `FriendsGroup` → `Child` (Freundesgruppen kennen ihre Mitglieder)
- `KindergartenGroup` → `Child` (Kindergartengruppen kennen ihre Mitglieder)

## Zyklische Abhängigkeiten

Das System enthält **mehrere** zyklische Abhängigkeiten:

### Domain-Level Zyklen
- `Child` ↔ `FriendsGroup` (durch `joinFriendsGroup()` und `addChild()`)
- `Child` ↔ `KindergartenGroup` (durch `joinKindergartenGroup()` und `addChild()`)

### Service-Level Zyklen  
- `ChildService` ↔ `FriendsGroupService` 
- `ChildService` ↔ `KindergartenGroupService`

## Herausforderung

Diese Aufgabe ist **schwieriger** als die vorherigen, da:
1. **Dreiecks-Abhängigkeiten** zwischen 3 Entitäten bestehen
2. **Mehrere Zyklen** gleichzeitig aufgelöst werden müssen (4 Zyklen total)
3. **Sowohl Domain- als auch Service-Zyklen** vorhanden sind
4. Die **Abhängigkeitsrichtung** klar definiert ist und eingehalten werden muss

**Schwierigkeitsgrad:** ★★★★☆ (Fortgeschritten)

**Warum ist A6 schwieriger?**
- **Komplexere Zyklen:** Nicht nur 1:1 Beziehungen wie in A1-A5
- **Mehrfache Service-Abhängigkeiten:** `ChildService` hängt von beiden anderen Services ab
- **Domain-Logic-Zyklen:** Entitäten rufen sich gegenseitig auf (`joinFriendsGroup` ↔ `addChild`)
- **Korrekte Richtung wichtig:** Von spezifisch zu allgemein, nicht umgekehrt

## Tests

### Integration-Tests
- `training.a6.KindergartenIntegrationTest` - Cross-Aggregate Integration Tests
  - Tests für Gruppenzuweisungen
  - Tests für soziales Verhalten
  - Tests für Kapazitätsgrenzen
  - Tests für Freundschaftsbeziehungen

### Architektur-Tests
- `training.a6.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a6.CycleTest"` wird grün
- `mvn test -Dtest="training.a6.KindergartenIntegrationTest"` wird grün

---
[← Zurück zur Übersicht](../../../../../README.md)
