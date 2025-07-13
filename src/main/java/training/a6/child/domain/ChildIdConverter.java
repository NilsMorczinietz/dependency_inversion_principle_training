package training.a6.child.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class ChildIdConverter extends GenericIdConverter<ChildId> {
    public ChildIdConverter() {
        super(ChildId::new);
    }
}
