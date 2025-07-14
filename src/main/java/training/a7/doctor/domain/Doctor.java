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
    private BigDecimal hourlyRate;

    @ElementCollection(fetch = FetchType.EAGER)
    private final List<PatientId> assignedPatients = new ArrayList<>();

    public Doctor(String name, String specialization, BigDecimal hourlyRate) {
        this.name = name;
        this.specialization = specialization;
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
}
