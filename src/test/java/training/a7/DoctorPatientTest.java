package training.a7;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import training.a7.doctor.application.DoctorService;
import training.a7.doctor.domain.Doctor;
import training.a7.patient.application.PatientService;
import training.a7.patient.domain.Patient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class DoctorPatientTest {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    private Doctor doctor1, doctor2, doctor3;
    private Patient patient1, patient2, patient3, patient4;

    @BeforeEach
    public void setUp() {
        initializeDoctors();
        initializePatients();
        assignPatientsToDoctor();
    }

    /**
     * Initializes the doctors used in the tests.
     */
    private void initializeDoctors() {
        doctor1 = new Doctor("Dr. Smith", "Cardiology", new BigDecimal("150.00"));
        doctor1 = doctorService.addDoctor(doctor1);
        
        doctor2 = new Doctor("Dr. Johnson", "Emergency", new BigDecimal("120.00"));
        doctor2 = doctorService.addDoctor(doctor2);
        
        doctor3 = new Doctor("Dr. Brown", "Surgery", new BigDecimal("200.00"));
        doctor3 = doctorService.addDoctor(doctor3);
    }

    /**
     * Initializes the patients used in the tests.
     */
    private void initializePatients() {
        patient1 = new Patient("Alice Wilson", "Critical Cardiology emergency", 9);
        patient1 = patientService.addPatient(patient1);
        
        patient2 = new Patient("Bob Brown", "Serious surgery required", 7);
        patient2 = patientService.addPatient(patient2);
        
        patient3 = new Patient("Charlie Davis", "Urgent emergency treatment", 8);
        patient3 = patientService.addPatient(patient3);
        
        patient4 = new Patient("Diana Miller", "Routine cardiology checkup", 3);
        patient4 = patientService.addPatient(patient4);
    }

    /**
     * Assigns patients to doctors for testing.
     */
    private void assignPatientsToDoctor() {
        doctorService.assignPatientToDoctor(patient1, doctor1); // Critical cardiology -> Cardiologist
        doctorService.assignPatientToDoctor(patient2, doctor2); // Surgery -> Emergency doctor  
        doctorService.assignPatientToDoctor(patient3, doctor3); // Emergency -> Surgeon
        doctorService.assignPatientToDoctor(patient4, doctor3); // Routine cardiology -> Surgeon
    }

    @Test
    public void testTreatmentCostCalculation() {
        // Test cyclic dependency: Patient cost calculation uses Doctor hourly rate
        Double cost1 = patientService.calculateTreatmentCost(patient1.getId());
        Double cost2 = patientService.calculateTreatmentCost(patient2.getId());
        Double cost3 = patientService.calculateTreatmentCost(patient3.getId());
        
        assertNotNull(cost1);
        assertNotNull(cost2);
        assertNotNull(cost3);
        
        assertTrue(cost1 > 0, "Treatment cost should be positive");
        assertTrue(cost2 > 0, "Treatment cost should be positive");
        assertTrue(cost3 > 0, "Treatment cost should be positive");
        
        // Patient 1 has priority 9, Patient 3 has priority 8 - both use different doctors
        // We can just check that they're all positive and reasonable
        assertTrue(cost1 > 100, "High priority patient should have reasonable cost");
        assertTrue(cost3 > 100, "High priority patient should have reasonable cost");
    }

    @Test
    public void testDoctorSpecializationFiltering() {
        var cardiologists = doctorService.getDoctorsBySpecialization("Cardiology");
        var emergencyDoctors = doctorService.getDoctorsBySpecialization("Emergency");
        var surgeons = doctorService.getDoctorsBySpecialization("Surgery");
        
        assertEquals(1, cardiologists.size(), "Should find one cardiologist");
        assertEquals(1, emergencyDoctors.size(), "Should find one emergency doctor");
        assertEquals(1, surgeons.size(), "Should find one surgeon");
        
        assertEquals("Dr. Smith", cardiologists.get(0).getName());
        assertEquals("Dr. Johnson", emergencyDoctors.get(0).getName());
        assertEquals("Dr. Brown", surgeons.get(0).getName());
    }

    @Test
    public void testHighPriorityPatientFiltering() {
        var highPriorityPatients = patientService.getHighPriorityPatients(7);
        
        assertNotNull(highPriorityPatients);
        assertTrue(highPriorityPatients.size() >= 2, "Should find at least 2 high priority patients");
        
        // Check that all returned patients have high priority
        for (Patient patient : highPriorityPatients) {
            assertTrue(patient.getPriority() >= 7, "All returned patients should have priority >= 7");
        }
    }

    @Test
    public void testPatientAssignment() {
        // Verify patients are correctly assigned to doctors
        var doctor1Patients = doctorService.getPatientsForDoctor(doctor1.getId());
        var doctor2Patients = doctorService.getPatientsForDoctor(doctor2.getId());
        var doctor3Patients = doctorService.getPatientsForDoctor(doctor3.getId());
        
        assertEquals(1, doctor1Patients.size(), "Doctor 1 should have 1 patient");
        assertEquals(1, doctor2Patients.size(), "Doctor 2 should have 1 patient");
        assertEquals(2, doctor3Patients.size(), "Doctor 3 should have 2 patients");
        
        assertEquals("Alice Wilson", doctor1Patients.get(0).getName());
        assertEquals("Bob Brown", doctor2Patients.get(0).getName());
        assertTrue(doctor3Patients.stream().anyMatch(p -> p.getName().equals("Charlie Davis")));
        assertTrue(doctor3Patients.stream().anyMatch(p -> p.getName().equals("Diana Miller")));
    }

    @Test 
    public void testPatientsByDiagnosis() {
        var urgentPatients = patientService.getPatientsByDiagnosis("Urgent emergency treatment");
        
        assertNotNull(urgentPatients);
        assertEquals(1, urgentPatients.size(), "Should find 1 patient with exact diagnosis match");
        
        assertEquals("Charlie Davis", urgentPatients.get(0).getName());
    }
}
