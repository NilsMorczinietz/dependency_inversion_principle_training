package training.a7.doctor.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import training.a7.doctor.domain.Doctor;
import training.a7.doctor.domain.DoctorId;
import training.a7.doctor.domain.DoctorRepository;
import training.a7.patient.domain.Patient;
import training.a7.patient.domain.PatientRepository;

import java.util.List;

@Service
@Transactional
public class DoctorService {
    private DoctorRepository doctorRepository;
    private PatientRepository patientRepository; // Creates cyclic dependency!

    @Autowired
    public DoctorService(
            DoctorRepository doctorRepository,
            PatientRepository patientRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    public Doctor addDoctor(Doctor doctor) {
        if (doctor == null) throw new IllegalArgumentException("Doctor is null");
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(DoctorId doctorId) {
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID is null");
        return doctorRepository.findById(doctorId).orElseThrow(
                () -> new IllegalArgumentException("Doctor not found"));
    }

    public void assignPatientToDoctor(Patient patient, Doctor doctor) {
        if (patient == null || doctor == null)
            throw new IllegalArgumentException("Patient or doctor is null");
        doctor.assignPatient(patient);
        patient.assignToDoctor(doctor);
        doctorRepository.save(doctor);
        patientRepository.save(patient);
    }

    public List<Patient> getPatientsForDoctor(DoctorId doctorId) {
        if (doctorId == null) throw new IllegalArgumentException("Doctor ID is null");
        return patientRepository.findByAssignedDoctor(doctorId);
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        if (specialization == null || specialization.isEmpty()) 
            throw new IllegalArgumentException("Specialization is null or empty");
        return doctorRepository.findBySpecialization(specialization);
    }
}
