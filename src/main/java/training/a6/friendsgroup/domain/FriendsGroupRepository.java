package training.a6.friendsgroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendsGroupRepository extends JpaRepository<FriendsGroup, FriendsGroupId> {
}
