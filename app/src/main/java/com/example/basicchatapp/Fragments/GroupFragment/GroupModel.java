package com.example.basicchatapp.Fragments.GroupFragment;

public class GroupModel {

    private String groupPhotoUrl, groupName;
    private boolean isStarred;

    public GroupModel(String groupPhotoUrl, String groupName, boolean isStarred) {
        this.groupPhotoUrl = groupPhotoUrl;
        this.groupName = groupName;
        this.isStarred = isStarred;
    }

    public String getGroupPhotoUrl() {
        return groupPhotoUrl;
    }

    public void setGroupPhotoUrl(String groupPhotoUrl) {
        this.groupPhotoUrl = groupPhotoUrl;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }
}
