package training.a7.patient.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import training.a7.doctor.domain.Doctor;
import training.a7.doctor.domain.DoctorId;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class Patient {

    @EmbeddedId
    @Setter(AccessLevel.PRIVATE)    // only for JPA
    private PatientId id;

    private String name;
    private String diagnosis;
    private Integer priority; // 1-10, higher is more urgent
    private DoctorId assignedDoctor;

    public Patient(String name, String diagnosis, Integer priority) {
        this.name = name;
        this.diagnosis = diagnosis;
        this.priority = priority;
        this.id = new PatientId();
    }

    public void assignToDoctor(Doctor doctor) {
        if (doctor == null) throw new IllegalArgumentException("Doctor is null");
        this.assignedDoctor = doctor.getId();
    }
}
