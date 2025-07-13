package training.a7.patient.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class PatientIdConverter extends GenericIdConverter<PatientId> {
    public PatientIdConverter() {
        super(PatientId::new);
    }
}
