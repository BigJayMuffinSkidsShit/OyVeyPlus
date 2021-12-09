package me.muffin.oyveyplus.api.friends;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuckyouthinkimboogieman
 */

public class FriendManager {
    private final List<Friend> friends;

    public FriendManager() {
        this.friends = new ArrayList<>();
    }

    public List<Friend> getFriends() { return friends; }

    public Friend getFriend(String name) { return friends.stream().filter(friend -> friend.getName().equalsIgnoreCase(name)).findFirst().orElse(null); }

    public boolean isFriend(String name) { return getFriend(name) != null; }
}
