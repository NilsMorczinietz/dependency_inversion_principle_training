package training.a6.kindergartengroup.application;

import org.springframework.stereotype.Service;
import training.a6.child.domain.ChildId;
import training.a6.kindergartengroup.domain.KindergartenGroup;
import training.a6.kindergartengroup.domain.KindergartenGroupId;
import training.a6.kindergartengroup.domain.KindergartenGroupRepository;

import java.util.List;

@Service
public class KindergartenGroupService {

    private final KindergartenGroupRepository kindergartenGroupRepository;

    public KindergartenGroupService(KindergartenGroupRepository kindergartenGroupRepository) {
        this.kindergartenGroupRepository = kindergartenGroupRepository;
    }

    public KindergartenGroup createGroup(String name, String teacher, int maxChildren) {
        KindergartenGroup group = new KindergartenGroup(name, teacher, maxChildren);
        return kindergartenGroupRepository.save(group);
    }

    public KindergartenGroup findById(KindergartenGroupId kindergartenGroupId) {
        return kindergartenGroupRepository.findById(kindergartenGroupId).orElse(null);
    }

    public boolean addChildToGroup(KindergartenGroupId groupId, ChildId childId) {
        KindergartenGroup group = findById(groupId);
        if (group != null && !group.isFull()) {
            group.addChildById(childId);
            kindergartenGroupRepository.save(group);
            return true;
        }
        return false;
    }

    public int countGroupsForChild(ChildId childId) {
        List<KindergartenGroup> allGroups = kindergartenGroupRepository.findAll();
        return (int) allGroups.stream()
                .filter(group -> group.hasChild(childId))
                .count();
    }
}