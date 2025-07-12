# Aufgabe A3: E-Commerce-Domäne

**Problem:** Zyklische Abhängigkeiten zwischen `product` und `customer` Paketen eliminieren.

## Aufgabe

1. Finden Sie die zyklischen Abhängigkeiten
2. Wenden Sie das Dependency Inversion Principle an
3. Beheben Sie alle Package-Zyklen
4. **Wichtig:** Business-Logic-Tests müssen grün bleiben

## Tests

### Domain-Tests (Unit Tests)
- `training.a3.customer.domain.*Test` - Customer Domain Tests
- `training.a3.product.domain.*Test` - Product Domain Tests

### Integration-Tests (Service-Layer)
- `training.a3.CustomerProductTest` - Cross-Aggregate Integration Tests
  - Tests für Wishlist-Operationen
  - Tests für VIP-Kunden-Identifikation
  - Tests für Produkt-Käufe
  - Tests für Customer-Product-Beziehungen

### Architektur-Tests
- `training.a3.CycleTest` - Zyklus-Erkennung

## Erfolgskriterium

- `mvn test -Dtest="training.a3.CycleTest"` wird grün
- `mvn test -Dtest="training.a3.*.domain.*Test"` bleibt grün
- `mvn test -Dtest="training.a3.CustomerProductTest"` wird grün

---
[← Zurück zur Übersicht](../../../README.md)
