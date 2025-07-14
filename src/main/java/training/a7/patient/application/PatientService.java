package training.a7.patient.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a7.patient.domain.Patient;
import training.a7.patient.domain.PatientId;
import training.a7.patient.domain.PatientRepository;
import training.a7.doctor.application.DoctorService;
import training.a7.doctor.domain.Doctor;

import java.util.List;

@Service
@Transactional
public class PatientService {
    private PatientRepository patientRepository;
    private DoctorService doctorService; // Creates cyclic dependency!

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
        // Simple cost calculation: doctor's hourly rate * priority factor
        Double baseCost = doctor.getHourlyRate().doubleValue();
        Double priorityMultiplier = patient.getPriority() / 5.0; // priority 1-10, so 0.2-2.0 multiplier
        
        return baseCost * priorityMultiplier;
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
}
