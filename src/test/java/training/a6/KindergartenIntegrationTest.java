package training.a6;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import training.a6.child.application.ChildService;
import training.a6.child.domain.Child;
import training.a6.friendsgroup.application.FriendsGroupService;
import training.a6.friendsgroup.domain.FriendsGroup;
import training.a6.kindergartengroup.application.KindergartenGroupService;
import training.a6.kindergartengroup.domain.KindergartenGroup;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class KindergartenIntegrationTest {

    @Autowired
    private ChildService childService;

    @Autowired
    private FriendsGroupService friendsGroupService;

    @Autowired
    private KindergartenGroupService kindergartenGroupService;

    @Test
    public void shouldCreateChildAndAssignToGroups() {
        // Given
        Child child = childService.createChild("Max", "Mustermann", 5);
        FriendsGroup friendsGroup = friendsGroupService.createGroup("Bauklötze-Freunde", "Bauen");
        KindergartenGroup kindergartenGroup = kindergartenGroupService.createGroup("Marienkäfer", "Frau Schmidt", 20);

        // When
        friendsGroupService.addChildToGroup(friendsGroup.getId(), child.getId());
        kindergartenGroupService.addChildToGroup(kindergartenGroup.getId(), child.getId());

        // Then
        assertTrue(friendsGroup.hasChild(child.getId()));
        assertTrue(kindergartenGroup.hasChild(child.getId()));
        assertEquals(1, friendsGroupService.countGroupsForChild(child.getId()));
        assertEquals(1, kindergartenGroupService.countGroupsForChild(child.getId()));
    }

    @Test
    public void shouldCheckChildSocialBehavior() {
        // Given
        Child child = childService.createChild("Anna", "Test", 4);
        FriendsGroup group1 = friendsGroupService.createGroup("Gruppe 1", "Spielen");
        FriendsGroup group2 = friendsGroupService.createGroup("Gruppe 2", "Malen");

        // When
        friendsGroupService.addChildToGroup(group1.getId(), child.getId());
        friendsGroupService.addChildToGroup(group2.getId(), child.getId());

        // Then
        assertTrue(childService.isChildSocial(child.getId()));
        assertEquals(2, childService.getTotalGroupsForChild(child.getId()));
    }

    @Test
    public void shouldCheckKindergartenGroupCapacity() {
        // Given
        KindergartenGroup smallGroup = kindergartenGroupService.createGroup("Kleine Gruppe", "Test", 2);
        Child child1 = childService.createChild("Kind1", "Test", 3);
        Child child2 = childService.createChild("Kind2", "Test", 4);
        Child child3 = childService.createChild("Kind3", "Test", 5);

        // When
        boolean added1 = kindergartenGroupService.addChildToGroup(smallGroup.getId(), child1.getId());
        boolean added2 = kindergartenGroupService.addChildToGroup(smallGroup.getId(), child2.getId());
        boolean added3 = kindergartenGroupService.addChildToGroup(smallGroup.getId(), child3.getId());

        // Then
        assertTrue(added1);
        assertTrue(added2);
        assertFalse(added3); // Gruppe ist voll
        assertTrue(smallGroup.isFull());
        assertEquals(0, smallGroup.getAvailableSpots());
    }

    @Test
    public void shouldCheckFriendshipsBetweenChildren() {
        // Given
        Child child1 = childService.createChild("Tom", "Test", 5);
        Child child2 = childService.createChild("Lisa", "Test", 4);
        Child child3 = childService.createChild("Ben", "Test", 6);
        FriendsGroup sharedGroup = friendsGroupService.createGroup("Gemeinsame Freunde", "Zusammen spielen");

        // When
        friendsGroupService.addChildToGroup(sharedGroup.getId(), child1.getId());
        friendsGroupService.addChildToGroup(sharedGroup.getId(), child2.getId());

        // Then
        assertTrue(friendsGroupService.areChildrenInSameGroup(child1.getId(), child2.getId()));
        assertFalse(friendsGroupService.areChildrenInSameGroup(child1.getId(), child3.getId()));
    }
}
