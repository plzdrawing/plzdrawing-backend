package org.example.plzdrawing.api.chat.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.plzdrawing.api.chat.dto.request.ChatDto;
import org.example.plzdrawing.domain.chat.MessageType;

public class ChatDtoValidator implements ConstraintValidator<ValidChatDto, ChatDto> {

    @Override
    public boolean isValid(ChatDto chatDto, ConstraintValidatorContext context) {
        if (chatDto.getMessageType() == MessageType.TEXT) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (isEmpty(chatDto.getFileUrl())) {
            addViolation(context, "fileUrl", "파일 전송 시 fileUrl은 필수입니다.");
            valid = false;
        }
        if (isEmpty(chatDto.getFileName())) {
            addViolation(context, "fileName", "파일 전송 시 fileName은 필수입니다.");
            valid = false;
        }
        if (chatDto.getFileSize() == null || chatDto.getFileSize() <= 0) {
            addViolation(context, "fileSize", "파일 전송 시 fileSize는 필수입니다.");
            valid = false;
        }
        if (isEmpty(chatDto.getMimeType())) {
            addViolation(context, "mimeType", "파일 전송 시 mimeType은 필수입니다.");
            valid = false;
        }
        return valid;
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void addViolation(ConstraintValidatorContext context, String property, String message) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}