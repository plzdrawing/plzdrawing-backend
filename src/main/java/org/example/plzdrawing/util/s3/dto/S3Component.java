package org.example.plzdrawing.util.s3.dto;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class S3Component {

    @Value("{spring.cloud.aws.s3.bucket}")
    private String bucket;
}
