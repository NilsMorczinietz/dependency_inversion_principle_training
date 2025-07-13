package training.a7.doctor.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class DoctorIdConverter extends GenericIdConverter<DoctorId> {
    public DoctorIdConverter() {
        super(DoctorId::new);
    }
}
