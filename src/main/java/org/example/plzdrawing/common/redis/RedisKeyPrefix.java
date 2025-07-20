package org.example.plzdrawing.common.redis;

public class RedisKeyPrefix {

    public static final String CHATROOM_PRESENCE = "chatroom:presence:";
    public static final String CHATROOM_UNREAD = "chatroom:unread:";
    public static final String EMAIL_AUTH_NUMBER = "AuthNumber:";
    public static final String REISSUE_PREFIX = "Reissue:";

    private RedisKeyPrefix() {}
}
