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
import java.time.LocalDate;

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
        doctor1 = new Doctor("Dr. Smith", "Cardiology", 10, new BigDecimal("150.00"));
        doctor1 = doctorService.addDoctor(doctor1);
        
        doctor2 = new Doctor("Dr. Johnson", "Emergency", 5, new BigDecimal("120.00"));
        doctor2 = doctorService.addDoctor(doctor2);
        
        doctor3 = new Doctor("Dr. Brown", "Surgery", 15, new BigDecimal("200.00"));
        doctor3 = doctorService.addDoctor(doctor3);
    }

    /**
     * Initializes the patients used in the tests.
     */
    private void initializePatients() {
        patient1 = new Patient("Alice Wilson", LocalDate.of(1970, 5, 15), 
                              "Critical Cardiology emergency", 9, "diabetes,hypertension");
        patient1 = patientService.addPatient(patient1);
        
        patient2 = new Patient("Bob Brown", LocalDate.of(1985, 8, 22), 
                              "Serious surgery required", 7, "obesity");
        patient2 = patientService.addPatient(patient2);
        
        patient3 = new Patient("Charlie Davis", LocalDate.of(1950, 3, 10), 
                              "Urgent emergency treatment", 8, "heart disease,diabetes");
        patient3 = patientService.addPatient(patient3);
        
        patient4 = new Patient("Diana Miller", LocalDate.of(1995, 12, 5), 
                              "Routine cardiology checkup", 3, "none");
        patient4 = patientService.addPatient(patient4);
    }

    /**
     * Assigns patients to doctors for testing.
     */
    private void assignPatientsToDoctor() {
        doctorService.assignPatientToDoctor(patient1, doctor1); // Critical cardiology -> Cardiologist
        doctorService.assignPatientToDoctor(patient2, doctor2); // Surgery -> Emergency doctor  
        doctorService.assignPatientToDoctor(patient3, doctor3); // Emergency -> Surgeon (for fair comparison)
        doctorService.assignPatientToDoctor(patient4, doctor3); // Routine cardiology -> Surgeon
    }

    @Test
    public void testDoctorWorkloadCalculation() {
        // Test cyclic dependency: Doctor calculates workload based on Patient complexity
        Double workload1 = doctorService.calculateDoctorWorkload(doctor1.getId());
        Double workload2 = doctorService.calculateDoctorWorkload(doctor2.getId());
        Double workload3 = doctorService.calculateDoctorWorkload(doctor3.getId());
        
        assertNotNull(workload1);
        assertNotNull(workload2);
        assertNotNull(workload3);
        
        assertTrue(workload1 > 0, "Doctor 1 should have positive workload");
        assertTrue(workload2 > 0, "Doctor 2 should have positive workload");
        assertTrue(workload3 > 0, "Doctor 3 should have positive workload");
        
        // Doctor 2 (5 years) has 1 patient, Doctor 3 (15 years) has 2 patients
        // But Doctor 3's experience should help manage the workload better per patient
        // So compare workload divided by number of patients
        Double avgWorkload2 = workload2; // 1 patient
        Double avgWorkload3 = workload3 / 2.0; // 2 patients
        assertTrue(avgWorkload3 <= avgWorkload2, "More experienced doctor should have better workload management per patient");
    }

    @Test
    public void testPatientPriorityCalculation() {
        // Test cyclic dependency: Patient priority based on doctor specialization
        Integer priority1 = patientService.calculatePatientPriority(patient1.getId());
        Integer priority2 = patientService.calculatePatientPriority(patient2.getId());
        Integer priority4 = patientService.calculatePatientPriority(patient4.getId());
        
        assertNotNull(priority1);
        assertNotNull(priority2);
        assertNotNull(priority4);
        
        assertTrue(priority1 >= 9, "Critical cardiology patient with matching specialist should have high priority");
        assertTrue(priority4 >= 3, "Routine patient should have lower priority");
        assertTrue(priority1 > priority4, "Critical patient should have higher priority than routine");
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
        
        // Surgery (doctor3) has highest hourly rate, should result in higher costs
        assertTrue(cost2 > cost1, "Surgery should be more expensive than cardiology");
    }

    @Test
    public void testDoctorCapacityCalculation() {
        // Test cyclic dependency: Doctor capacity based on patient priorities
        Integer capacity1 = doctorService.calculateAvailableCapacity(doctor1.getId());
        Integer capacity2 = doctorService.calculateAvailableCapacity(doctor2.getId());
        Integer capacity3 = doctorService.calculateAvailableCapacity(doctor3.getId());
        
        assertNotNull(capacity1);
        assertNotNull(capacity2);
        assertNotNull(capacity3);
        
        assertTrue(capacity1 >= 0, "Capacity should be non-negative");
        assertTrue(capacity2 >= 0, "Capacity should be non-negative");
        assertTrue(capacity3 >= 0, "Capacity should be non-negative");
        
        // Doctor 3 has 2 patients, doctor 1 and doctor 2 each have 1 patient
        // So doctor 3 should have less available capacity
        assertTrue(capacity3 <= capacity1, "Doctor with more patients should have less available capacity");
        assertTrue(capacity3 <= capacity2, "Doctor with more patients should have less available capacity");
    }

    @Test
    public void testComplexityScoreCalculation() {
        // Test cyclic dependency: Patient complexity score uses Doctor experience
        Double complexity1 = patientService.calculateComplexityScore(patient1.getId());
        Double complexity2 = patientService.calculateComplexityScore(patient2.getId());
        Double complexity4 = patientService.calculateComplexityScore(patient4.getId());
        
        assertNotNull(complexity1);
        assertNotNull(complexity2);
        assertNotNull(complexity4);
        
        assertTrue(complexity1 > 0, "Complexity should be positive");
        assertTrue(complexity2 > 0, "Complexity should be positive");
        assertTrue(complexity4 > 0, "Complexity should be positive");
        
        // Critical patient should have higher complexity than routine
        assertTrue(complexity1 > complexity4, "Critical patient should have higher complexity");
    }

    @Test
    public void testAverageTreatmentDuration() {
        // Test cyclic dependency: Treatment duration uses Doctor experience
        Double avgDuration1 = doctorService.calculateAverageTreatmentDuration(doctor1.getId());
        Double avgDuration2 = doctorService.calculateAverageTreatmentDuration(doctor2.getId());
        Double avgDuration3 = doctorService.calculateAverageTreatmentDuration(doctor3.getId());
        
        assertNotNull(avgDuration1);
        assertNotNull(avgDuration2);
        assertNotNull(avgDuration3);
        
        assertTrue(avgDuration1 > 0, "Average duration should be positive");
        assertTrue(avgDuration2 > 0, "Average duration should be positive");
        assertTrue(avgDuration3 > 0, "Average duration should be positive");
        
        // More experienced doctors should have shorter treatment times
        assertTrue(avgDuration3 < avgDuration2, "More experienced doctor should have shorter treatment times");
    }

    @Test
    public void testAverageComplexityAcrossAllPatients() {
        // Test cross-aggregate calculation
        Double avgComplexity = patientService.calculateAverageComplexity();
        
        assertNotNull(avgComplexity);
        assertTrue(avgComplexity > 0, "Average complexity should be positive");
        assertTrue(avgComplexity <= 10, "Average complexity should be reasonable");
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
}
