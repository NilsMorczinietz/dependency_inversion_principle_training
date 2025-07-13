package training.a7.patient.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a7.patient.domain.Patient;
import training.a7.patient.domain.PatientId;
import training.a7.patient.domain.PatientRepository;
import training.a7.doctor.application.DoctorService;
import training.a7.doctor.domain.Doctor;
import training.a7.doctor.domain.DoctorId;

import java.util.List;

@Service
@Transactional
public class PatientService {
    private PatientRepository patientRepository;
    private DoctorService doctorService;

    @Autowired
    public PatientService(PatientRepository patientRepository, DoctorService doctorService) {
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
    }

    public Patient addPatient(Patient patient) {
        if (patient == null) throw new IllegalArgumentException("Patient is null");
        return patientRepository.save(patient);
    }

    public Patient getPatientById(PatientId patientId) {
        if (patientId == null) throw new IllegalArgumentException("Patient ID is null");
        return patientRepository.findById(patientId).orElseThrow(
                () -> new IllegalArgumentException("Patient not found"));
    }

    // Cyclic dependency: Uses DoctorService to calculate treatment costs
    public Double calculateTreatmentCost(PatientId patientId) {
        if (patientId == null) throw new IllegalArgumentException("Patient ID is null");
        Patient patient = getPatientById(patientId);
        
        if (patient.getAssignedDoctor() == null) {
            return 100.0; // default cost if no doctor assigned
        }
        
        Doctor doctor = doctorService.getDoctorById(patient.getAssignedDoctor());
        return patient.calculateTreatmentCost(doctor);
    }

    // Cyclic dependency: Uses DoctorService to calculate priority based on doctor specialization
    public Integer calculatePatientPriority(PatientId patientId) {
        if (patientId == null) throw new IllegalArgumentException("Patient ID is null");
        Patient patient = getPatientById(patientId);
        
        if (patient.getAssignedDoctor() == null) {
            return patient.calculatePriority(null);
        }
        
        Doctor doctor = doctorService.getDoctorById(patient.getAssignedDoctor());
        return patient.calculatePriority(doctor.getSpecialization());
    }

    // Cyclic dependency: Uses DoctorService to calculate complexity score
    public Double calculateComplexityScore(PatientId patientId) {
        if (patientId == null) throw new IllegalArgumentException("Patient ID is null");
        Patient patient = getPatientById(patientId);
        
        if (patient.getAssignedDoctor() == null) {
            return patient.calculateComplexityScore(null);
        }
        
        Doctor doctor = doctorService.getDoctorById(patient.getAssignedDoctor());
        return patient.calculateComplexityScore(doctor);
    }

    public List<Patient> getPatientsByDiagnosis(String diagnosis) {
        if (diagnosis == null || diagnosis.isEmpty()) 
            throw new IllegalArgumentException("Diagnosis is null or empty");
        return patientRepository.findByDiagnosis(diagnosis);
    }

    public List<Patient> getHighPriorityPatients(Integer minPriority) {
        if (minPriority == null || minPriority < 1) minPriority = 7;
        return patientRepository.findByPriorityGreaterThanEqualOrderByPriorityDesc(minPriority);
    }

    // Calculate average complexity for all patients assigned to doctors
    public Double calculateAverageComplexity() {
        List<Patient> allPatients = patientRepository.findAll();
        Double totalComplexity = 0.0;
        Integer patientsWithDoctors = 0;
        
        for (Patient patient : allPatients) {
            if (patient.getAssignedDoctor() != null) {
                totalComplexity += calculateComplexityScore(patient.getId());
                patientsWithDoctors++;
            }
        }
        
        return patientsWithDoctors > 0 ? totalComplexity / patientsWithDoctors : 0.0;
    }
}
