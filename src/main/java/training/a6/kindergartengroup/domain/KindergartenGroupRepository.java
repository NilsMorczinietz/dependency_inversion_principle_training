package training.a6.kindergartengroup.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KindergartenGroupRepository extends JpaRepository<KindergartenGroup, KindergartenGroupId> {
}
