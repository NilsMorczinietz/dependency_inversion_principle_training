package training.a6.child.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import training.a6.child.domain.Child;
import training.a6.child.domain.ChildId;
import training.a6.child.domain.ChildRepository;
import training.a6.friendsgroup.application.FriendsGroupService;
import training.a6.kindergartengroup.application.KindergartenGroupService;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final FriendsGroupService friendsGroupService; // <- Zyklus!
    private final KindergartenGroupService kindergartenGroupService; // <- Zyklus!

    public ChildService(ChildRepository childRepository, 
                       @Lazy FriendsGroupService friendsGroupService,
                       @Lazy KindergartenGroupService kindergartenGroupService) {
        this.childRepository = childRepository;
        this.friendsGroupService = friendsGroupService;
        this.kindergartenGroupService = kindergartenGroupService;
    }

    public Child createChild(String firstName, String lastName, int age) {
        Child child = new Child(firstName, lastName, age);
        return childRepository.save(child);
    }

    public Child findById(ChildId childId) {
        return childRepository.findById(childId).orElse(null);
    }

    // PROBLEM: Diese Methoden erstellen zyklische Service-Abhängigkeiten
    public int getTotalGroupsForChild(ChildId childId) {
        int friendsGroups = friendsGroupService.countGroupsForChild(childId);
        int kindergartenGroups = kindergartenGroupService.countGroupsForChild(childId);
        return friendsGroups + kindergartenGroups;
    }

    public boolean isChildSocial(ChildId childId) {
        return friendsGroupService.countGroupsForChild(childId) > 1;
    }
}
