package org.example.plzdrawing.domain.chat;

public enum MessageType {
    TEXT {
        @Override
        public String getDisplayContent(String message) {
            return message;
        }
    },
    FILE {
        @Override
        public String getDisplayContent(String message) {
            return "파일이 전송되었습니다.";
        }
    }
    ;

    public abstract String getDisplayContent(String message);
}
