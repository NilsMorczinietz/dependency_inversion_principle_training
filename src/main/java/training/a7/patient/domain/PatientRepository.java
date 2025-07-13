package training.a7.patient.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import training.a7.doctor.domain.DoctorId;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, PatientId> {
    @Query("SELECT p FROM Patient p WHERE p.assignedDoctor = :doctorId")
    List<Patient> findByAssignedDoctor(@Param("doctorId") DoctorId doctorId);
    
    List<Patient> findByDiagnosis(String diagnosis);
    
    @Query("SELECT p FROM Patient p WHERE p.priority >= :minPriority ORDER BY p.priority DESC")
    List<Patient> findByPriorityGreaterThanEqualOrderByPriorityDesc(@Param("minPriority") Integer minPriority);
}
