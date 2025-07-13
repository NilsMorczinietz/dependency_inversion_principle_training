package training.a6.kindergartengroup.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import training.a6.child.application.ChildService;
import training.a6.child.domain.ChildId;
import training.a6.kindergartengroup.domain.KindergartenGroup;
import training.a6.kindergartengroup.domain.KindergartenGroupId;
import training.a6.kindergartengroup.domain.KindergartenGroupRepository;

import java.util.List;

@Service
public class KindergartenGroupService {

    private final KindergartenGroupRepository kindergartenGroupRepository;
    private final ChildService childService; // <- Zyklus!

    public KindergartenGroupService(KindergartenGroupRepository kindergartenGroupRepository,
                                   @Lazy ChildService childService) {
        this.kindergartenGroupRepository = kindergartenGroupRepository;
        this.childService = childService;
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

    // PROBLEM: Diese Methode erstellt zyklische Service-Abhängigkeit
    public int countGroupsForChild(ChildId childId) {
        List<KindergartenGroup> allGroups = kindergartenGroupRepository.findAll();
        return (int) allGroups.stream()
                .filter(group -> group.hasChild(childId))
                .count();
    }

    public boolean canChildJoinGroup(KindergartenGroupId groupId, ChildId childId) {
        // Diese Methode hängt vom ChildService ab (zyklisch)
        if (childService.findById(childId) == null) {
            return false;
        }
        
        KindergartenGroup group = findById(groupId);
        return group != null && !group.isFull() && !group.hasChild(childId);
    }
}
