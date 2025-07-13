package training.a6.friendsgroup.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training.a6.child.domain.Child;
import training.a6.child.domain.ChildId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FriendsGroup {
    
    @EmbeddedId
    private FriendsGroupId id;
    
    @Column(nullable = false)
    private String name;
    
    @Column
    private String activity;
    
    // Korrekte Abhängigkeit: FriendsGroup -> Child (spezifisch zu allgemein)
    @ElementCollection
    @CollectionTable(name = "friends_group_children", joinColumns = @JoinColumn(name = "friends_group_id"))
    @Column(name = "child_id")
    @Convert(converter = training.a6.child.domain.ChildIdConverter.class)
    private List<ChildId> childIds = new ArrayList<>();
    
    public FriendsGroup(String name, String activity) {
        this.id = new FriendsGroupId(java.util.UUID.randomUUID());
        this.name = name;
        this.activity = activity;
    }
    
    // PROBLEM: Diese Methode erstellt zyklische Abhängigkeit
    public void addChild(Child child) {
        this.childIds.add(child.getId());
    }
    
    public void addChildById(ChildId childId) {
        if (!childIds.contains(childId)) {
            this.childIds.add(childId);
        }
    }
    
    public void removeChild(ChildId childId) {
        this.childIds.remove(childId);
    }
    
    public boolean hasChild(ChildId childId) {
        return childIds.contains(childId);
    }
    
    public int getChildCount() {
        return childIds.size();
    }
    
    public List<ChildId> getChildIds() {
        return new ArrayList<>(childIds);
    }
}
