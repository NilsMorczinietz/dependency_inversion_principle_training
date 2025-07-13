package training.a6.child.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import training.a6.friendsgroup.domain.FriendsGroupId;
import training.a6.kindergartengroup.domain.KindergartenGroupId;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Child {
    
    @EmbeddedId
    private ChildId id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private int age;
    
    @ElementCollection
    @CollectionTable(name = "child_friends_groups", joinColumns = @JoinColumn(name = "child_id"))
    @Column(name = "friends_group_id")
    @Convert(converter = training.a6.friendsgroup.domain.FriendsGroupIdConverter.class)
    private List<FriendsGroupId> friendsGroupIds = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "child_kindergarten_groups", joinColumns = @JoinColumn(name = "child_id"))
    @Column(name = "kindergarten_group_id")
    @Convert(converter = training.a6.kindergartengroup.domain.KindergartenGroupIdConverter.class)
    private List<KindergartenGroupId> kindergartenGroupIds = new ArrayList<>();
    
    public Child(String firstName, String lastName, int age) {
        this.id = new ChildId(java.util.UUID.randomUUID());
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}