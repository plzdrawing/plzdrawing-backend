package org.example.plzdrawing.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;
import org.example.plzdrawing.common.annotation.ValidHashTag;

public class HashTagValidator implements ConstraintValidator<ValidHashTag, List<String>> {
    private int maxCount;
    private int maxLength;
    private Pattern pattern;
    @Override
    public void initialize(ValidHashTag constraintAnnotation) {
        this.maxCount = constraintAnnotation.maxCount();
        this.maxLength = constraintAnnotation.maxLength();
        this.pattern = Pattern.compile(constraintAnnotation.allowedPattern());
    }

    @Override
    public boolean isValid(List<String> tags, ConstraintValidatorContext context) {
        if (tags == null)
            return true; // null 허용

        if (tags.size() > maxCount) {
            return false;
        }

        for (String tag : tags) {
            if (tag.length() > maxLength) {
                return false;
            }
            if (!pattern.matcher(tag).matches()) {
                return false;
            }
        }

        return true;
    }
}
