package training.a6.friendsgroup.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import training.a6.child.application.ChildService;
import training.a6.child.domain.ChildId;
import training.a6.friendsgroup.domain.FriendsGroup;
import training.a6.friendsgroup.domain.FriendsGroupId;
import training.a6.friendsgroup.domain.FriendsGroupRepository;

import java.util.List;

@Service
public class FriendsGroupService {

    private final FriendsGroupRepository friendsGroupRepository;
    private final ChildService childService;

    public FriendsGroupService(FriendsGroupRepository friendsGroupRepository,
                              @Lazy ChildService childService) {
        this.friendsGroupRepository = friendsGroupRepository;
        this.childService = childService;
    }

    public FriendsGroup createGroup(String name, String activity) {
        FriendsGroup group = new FriendsGroup(name, activity);
        return friendsGroupRepository.save(group);
    }

    public FriendsGroup findById(FriendsGroupId friendsGroupId) {
        return friendsGroupRepository.findById(friendsGroupId).orElse(null);
    }

    public void addChildToGroup(FriendsGroupId groupId, ChildId childId) {
        FriendsGroup group = findById(groupId);
        if (group != null) {
            group.addChildById(childId);
            friendsGroupRepository.save(group);
        }
    }

    public int countGroupsForChild(ChildId childId) {
        List<FriendsGroup> allGroups = friendsGroupRepository.findAll();
        return (int) allGroups.stream()
                .filter(group -> group.hasChild(childId))
                .count();
    }

    public boolean areChildrenInSameGroup(ChildId child1, ChildId child2) {
        if (childService.findById(child1) == null || childService.findById(child2) == null) {
            return false;
        }
        
        List<FriendsGroup> allGroups = friendsGroupRepository.findAll();
        return allGroups.stream()
                .anyMatch(group -> group.hasChild(child1) && group.hasChild(child2));
    }
}
