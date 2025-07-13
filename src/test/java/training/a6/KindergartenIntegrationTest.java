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

    @Test
    public void shouldCountFriendsGroupsForChild() {
        // Given
        Child child = childService.createChild("Emma", "Test", 4);
        FriendsGroup group1 = friendsGroupService.createGroup("Maler", "Malen");
        FriendsGroup group2 = friendsGroupService.createGroup("Sportler", "Sport");
        FriendsGroup group3 = friendsGroupService.createGroup("Leser", "Lesen");

        // When - Kind tritt verschiedenen Freundesgruppen bei
        friendsGroupService.addChildToGroup(group1.getId(), child.getId());
        friendsGroupService.addChildToGroup(group2.getId(), child.getId());
        friendsGroupService.addChildToGroup(group3.getId(), child.getId());

        // Then - Zähle Freundesgruppen für das Kind
        assertEquals(3, friendsGroupService.countGroupsForChild(child.getId()));
        
        // Verify child is in each group
        assertTrue(group1.hasChild(child.getId()));
        assertTrue(group2.hasChild(child.getId()));
        assertTrue(group3.hasChild(child.getId()));
    }

    @Test
    public void shouldCountKindergartenGroupsForChild() {
        // Given
        Child child = childService.createChild("Luca", "Test", 3);
        KindergartenGroup group1 = kindergartenGroupService.createGroup("Früh-Gruppe", "Frau Müller", 15);
        KindergartenGroup group2 = kindergartenGroupService.createGroup("Spät-Gruppe", "Herr Klein", 12);

        // When - Kind wird zu verschiedenen Kindergartengruppen hinzugefügt
        kindergartenGroupService.addChildToGroup(group1.getId(), child.getId());
        kindergartenGroupService.addChildToGroup(group2.getId(), child.getId());

        // Then - Zähle Kindergartengruppen für das Kind
        assertEquals(2, kindergartenGroupService.countGroupsForChild(child.getId()));
        
        // Verify child is in each group
        assertTrue(group1.hasChild(child.getId()));
        assertTrue(group2.hasChild(child.getId()));
    }

    @Test
    public void shouldCountAllGroupsForChildAcrossServices() {
        // Given
        Child child = childService.createChild("Sofia", "Test", 5);
        
        // Erstelle verschiedene Gruppen
        FriendsGroup friendsGroup1 = friendsGroupService.createGroup("Puzzle-Freunde", "Puzzeln");
        FriendsGroup friendsGroup2 = friendsGroupService.createGroup("Musik-Freunde", "Singen");
        KindergartenGroup kindergartenGroup1 = kindergartenGroupService.createGroup("Mittags-Gruppe", "Frau Weber", 18);
        KindergartenGroup kindergartenGroup2 = kindergartenGroupService.createGroup("Nachmittags-Gruppe", "Herr Fischer", 10);

        // When - Kind tritt allen Gruppen bei
        friendsGroupService.addChildToGroup(friendsGroup1.getId(), child.getId());
        friendsGroupService.addChildToGroup(friendsGroup2.getId(), child.getId());
        kindergartenGroupService.addChildToGroup(kindergartenGroup1.getId(), child.getId());
        kindergartenGroupService.addChildToGroup(kindergartenGroup2.getId(), child.getId());

        // Then - Überprüfe separate und kombinierte Zählungen
        assertEquals(2, friendsGroupService.countGroupsForChild(child.getId()));
        assertEquals(2, kindergartenGroupService.countGroupsForChild(child.getId()));
        assertEquals(4, childService.getTotalGroupsForChild(child.getId())); // Gesamt über alle Services

        // Verify social behavior based on friends groups
        assertTrue(childService.isChildSocial(child.getId())); // > 1 friends group
    }

    @Test
    public void shouldHandleChildNotInAnyGroup() {
        // Given
        Child child = childService.createChild("Noah", "Test", 6);
        
        // When - Kind ist in keiner Gruppe
        
        // Then - Alle Zählungen sollten 0 sein
        assertEquals(0, friendsGroupService.countGroupsForChild(child.getId()));
        assertEquals(0, kindergartenGroupService.countGroupsForChild(child.getId()));
        assertEquals(0, childService.getTotalGroupsForChild(child.getId()));
        assertFalse(childService.isChildSocial(child.getId())); // Nicht sozial wenn ≤ 1 Freundesgruppe
    }
}
