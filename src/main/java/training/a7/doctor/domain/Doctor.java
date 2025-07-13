package training.a7.doctor.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.*;
import training.a7.patient.domain.Patient;
import training.a7.patient.domain.PatientId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Doctor {

    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private DoctorId id;

    private String name;
    private String specialization;
    private Integer experienceYears;
    private BigDecimal hourlyRate;

    @ElementCollection(fetch = FetchType.EAGER)
    private final List<PatientId> assignedPatients = new ArrayList<>();

    public Doctor(String name, String specialization, Integer experienceYears, BigDecimal hourlyRate) {
        this.name = name;
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.hourlyRate = hourlyRate;
        this.id = new DoctorId();
    }

    public void assignPatient(Patient patient) {
        if (patient == null) throw new IllegalArgumentException("Patient is null");
        assignedPatients.add(patient.getId());
    }

    public void removePatient(PatientId patientId) {
        if (patientId == null) throw new IllegalArgumentException("Patient ID is null");
        assignedPatients.remove(patientId);
    }

    // Cyclic dependency: Doctor calculates workload based on Patient complexity
    public Double calculateWorkload(List<Patient> patients) {
        if (patients == null || patients.isEmpty()) return 0.0;
        
        Double totalComplexity = 0.0;
        for (Patient patient : patients) {
            if (assignedPatients.contains(patient.getId())) {
                // Use patient's complexity calculation which depends on doctor experience
                totalComplexity += patient.calculateComplexityScore(this);
            }
        }
        
        // Adjust based on experience - more experienced doctors handle complexity better
        Double experienceMultiplier = 1.0 + (experienceYears * 0.02); // 2% reduction per year of experience
        return totalComplexity / Math.max(1.0, experienceMultiplier);
    }

    // Cyclic dependency: Doctor's availability depends on Patient priority calculations
    public Integer calculateAvailableCapacity(List<Patient> allPatients) {
        if (allPatients == null) return 10; // default capacity
        
        Integer baseCapacity = 10;
        Integer usedCapacity = 0;
        
        for (Patient patient : allPatients) {
            if (assignedPatients.contains(patient.getId())) {
                // Patient priority calculation uses doctor's specialization
                Integer patientPriority = patient.calculatePriority(this.specialization);
                usedCapacity += Math.max(1, patientPriority / 2);
            }
        }
        
        return Math.max(0, baseCapacity - usedCapacity);
    }

    // Cyclic dependency: Average treatment duration uses Patient diagnosis complexity
    public Double calculateAverageTreatmentDuration(List<Patient> patients) {
        if (patients == null || patients.isEmpty()) return 0.0;
        
        Double totalDuration = 0.0;
        Integer relevantPatients = 0;
        
        for (Patient patient : patients) {
            if (assignedPatients.contains(patient.getId())) {
                // Patient treatment duration calculation uses doctor's experience
                totalDuration += patient.calculateExpectedTreatmentDuration(this.experienceYears);
                relevantPatients++;
            }
        }
        
        return relevantPatients > 0 ? totalDuration / relevantPatients : 0.0;
    }
}
