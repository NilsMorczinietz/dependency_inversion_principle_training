package training.a6.kindergartengroup.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class KindergartenGroupIdConverter extends GenericIdConverter<KindergartenGroupId> {
    public KindergartenGroupIdConverter() {
        super(KindergartenGroupId::new);
    }
}
