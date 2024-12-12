package cat.tecnocampus.fgcstations.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.util.List;

@Entity
public class Friend {
    @EmbeddedId
    private FriendId id;

    @ManyToOne
    private User user;

    public Friend() {
    }

    public Friend(User user, String friend) {
        this.user = user;
        this.id = new FriendId();
        this.id.setFriend(friend);
        this.id.setUsername(user.getUsername());
    }

    public String getUsername() {
        return id.getUsername();
    }

    public String getFriend() {
        return id.getFriend();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friend friend)) return false;

        return id.equals(friend.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public FriendId getId() {
        return id;
    }
}
