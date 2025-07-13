package training.a6.friendsgroup.domain;

import org.springframework.stereotype.Component;
import training.GenericIdConverter;

@Component
public class FriendsGroupIdConverter extends GenericIdConverter<FriendsGroupId> {
    public FriendsGroupIdConverter() {
        super(FriendsGroupId::new);
    }
}
