package training.a7.patient.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;
import training.a7.doctor.domain.Doctor;
import training.a7.doctor.domain.DoctorId;

import java.time.LocalDate;

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
    private LocalDate birthDate;
    private String diagnosis;
    private Integer priority; // 1-10, higher is more urgent
    private String riskFactors;
    private DoctorId assignedDoctor;

    public Patient(String name, LocalDate birthDate, String diagnosis, Integer priority, String riskFactors) {
        this.name = name;
        this.birthDate = birthDate;
        this.diagnosis = diagnosis;
        this.priority = priority;
        this.riskFactors = riskFactors;
        this.id = new PatientId();
    }

    public void assignToDoctor(Doctor doctor) {
        if (doctor == null) throw new IllegalArgumentException("Doctor is null");
        this.assignedDoctor = doctor.getId();
    }

    // Cyclic dependency: Patient complexity calculation uses Doctor experience
    public Double calculateComplexityScore(Doctor doctor) {
        if (doctor == null) return 5.0; // default complexity
        
        Double baseComplexity = 5.0;
        
        // Increase complexity based on diagnosis severity
        if (diagnosis != null) {
            if (diagnosis.toLowerCase().contains("critical") || diagnosis.toLowerCase().contains("emergency")) {
                baseComplexity += 3.0;
            } else if (diagnosis.toLowerCase().contains("serious") || diagnosis.toLowerCase().contains("urgent")) {
                baseComplexity += 2.0;
            }
        }
        
        // Risk factors increase complexity
        if (riskFactors != null && !riskFactors.isEmpty()) {
            baseComplexity += riskFactors.split(",").length * 0.5;
        }
        
        // Doctor's experience reduces perceived complexity
        if (doctor.getExperienceYears() != null) {
            Double experienceReduction = doctor.getExperienceYears() * 0.1;
            baseComplexity = Math.max(1.0, baseComplexity - experienceReduction);
        }
        
        return baseComplexity;
    }

    // Cyclic dependency: Priority calculation uses Doctor specialization
    public Integer calculatePriority(String doctorSpecialization) {
        Integer calculatedPriority = this.priority != null ? this.priority : 5;
        
        // Adjust priority based on doctor specialization match
        if (doctorSpecialization != null && diagnosis != null) {
            if (diagnosis.toLowerCase().contains(doctorSpecialization.toLowerCase())) {
                calculatedPriority += 2; // Higher priority if specialization matches
            }
        }
        
        // Age factor - older patients get higher priority
        if (birthDate != null) {
            int age = LocalDate.now().getYear() - birthDate.getYear();
            if (age > 70) calculatedPriority += 2;
            else if (age > 60) calculatedPriority += 1;
        }
        
        return Math.min(10, Math.max(1, calculatedPriority));
    }

    // Cyclic dependency: Treatment duration calculation uses Doctor experience
    public Double calculateExpectedTreatmentDuration(Integer doctorExperience) {
        Double baseDuration = 60.0; // minutes
        
        // Increase duration based on diagnosis complexity
        if (diagnosis != null) {
            if (diagnosis.toLowerCase().contains("complex") || diagnosis.toLowerCase().contains("rare")) {
                baseDuration += 30.0;
            }
            if (diagnosis.toLowerCase().contains("surgery")) {
                baseDuration += 90.0;
            }
        }
        
        // Priority affects duration
        if (priority != null && priority > 7) {
            baseDuration += 20.0; // High priority cases take longer
        }
        
        // Doctor experience reduces treatment time significantly
        if (doctorExperience != null) {
            Double experienceReduction = doctorExperience * 5.0; // 5 minutes per year of experience
            baseDuration = Math.max(15.0, baseDuration - experienceReduction);
        }
        
        return baseDuration;
    }

    // Cyclic dependency: Cost calculation uses Doctor hourly rate
    public Double calculateTreatmentCost(Doctor doctor) {
        if (doctor == null || doctor.getHourlyRate() == null) return 100.0;
        
        Double estimatedHours = calculateExpectedTreatmentDuration(doctor.getExperienceYears()) / 60.0;
        Double baseCost = doctor.getHourlyRate().doubleValue() * estimatedHours;
        
        // Add complexity multiplier
        Double complexityMultiplier = calculateComplexityScore(doctor) / 5.0;
        
        return baseCost * complexityMultiplier;
    }
}
