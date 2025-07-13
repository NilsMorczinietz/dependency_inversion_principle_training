package training.a6.friendsgroup.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    
    public void addChildById(ChildId childId) {
        if (!childIds.contains(childId)) {
            this.childIds.add(childId);
        }
    }
    
    public boolean hasChild(ChildId childId) {
        return childIds.contains(childId);
    }
}