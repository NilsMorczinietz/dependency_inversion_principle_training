package training.a5.developer.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class DeveloperIdConverter extends GenericIdConverter<DeveloperId> {
    public DeveloperIdConverter() {
        super(DeveloperId::new);
    }
}
