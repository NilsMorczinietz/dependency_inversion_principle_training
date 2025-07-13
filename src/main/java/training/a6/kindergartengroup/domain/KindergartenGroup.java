package training.a6.kindergartengroup.domain;

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
public class KindergartenGroup {
    
    @EmbeddedId
    private KindergartenGroupId id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String teacher;
    
    @Column(nullable = false)
    private int maxChildren;
    
    @ElementCollection
    @CollectionTable(name = "kindergarten_group_children", joinColumns = @JoinColumn(name = "kindergarten_group_id"))
    @Column(name = "child_id")
    @Convert(converter = training.a6.child.domain.ChildIdConverter.class)
    private List<ChildId> childIds = new ArrayList<>();
    
    public KindergartenGroup(String name, String teacher, int maxChildren) {
        this.id = new KindergartenGroupId(java.util.UUID.randomUUID());
        this.name = name;
        this.teacher = teacher;
        this.maxChildren = maxChildren;
    }
    
    public void addChildById(ChildId childId) {
        if (childIds.size() < maxChildren && !childIds.contains(childId)) {
            this.childIds.add(childId);
        }
    }
    
    public boolean hasChild(ChildId childId) {
        return childIds.contains(childId);
    }
    
    public boolean isFull() {
        return childIds.size() >= maxChildren;
    }
    
    public int getAvailableSpots() {
        return maxChildren - childIds.size();
    }
}