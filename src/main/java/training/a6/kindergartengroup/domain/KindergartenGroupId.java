package training.a6.kindergartengroup.domain;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import training.GenericId;

import java.util.UUID;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "kindergarten_group_id"))
public class KindergartenGroupId extends GenericId {
    public KindergartenGroupId(UUID id) {
        super(id);
    }
}
