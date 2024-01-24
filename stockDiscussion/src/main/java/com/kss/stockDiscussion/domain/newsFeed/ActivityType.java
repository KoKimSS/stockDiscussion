package com.kss.stockDiscussion.domain.newsFeed;

public enum ActivityType {
    POST("글 작성"),
    COMMENT("댓글 작성"),
    FOLLOW("팔로우");

    private final String displayName;

    ActivityType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
