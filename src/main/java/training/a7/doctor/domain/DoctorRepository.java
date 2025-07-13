package training.a7.doctor.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import training.a7.patient.domain.PatientId;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, DoctorId> {
    @Query("SELECT d FROM Doctor d WHERE :patientId MEMBER OF d.assignedPatients")
    List<Doctor> findByAssignedPatientsContains(@Param("patientId") PatientId patientId);
    
    List<Doctor> findBySpecialization(String specialization);
}
