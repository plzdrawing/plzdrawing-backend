package org.example.plzdrawing.alarm.service;

import static org.example.plzdrawing.common.error.CommonErrorCode.FCM_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.example.plzdrawing.alarm.dto.FCMMessage;
import org.example.plzdrawing.common.exception.RestApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class FCMService {

    private static final Logger log = LoggerFactory.getLogger(FCMService.class);
    private static final OkHttpClient client = new OkHttpClient();

    private final ObjectMapper objectMapper;
    private final String apiUrl;
    private final String firebaseConfigPath;
    private final String googleScope;

    public FCMService(
            ObjectMapper objectMapper,
            @Value("${fcm.api-url}") String apiUrl,
            @Value("${fcm.firebase-config-path}") String firebaseConfigPath,
            @Value("${fcm.google.scope}") String googleScope
    ) {
        this.objectMapper = objectMapper;
        this.apiUrl = apiUrl;
        this.firebaseConfigPath = firebaseConfigPath;
        this.googleScope = googleScope;
    }

    public void sendMessageTo(String targetToken, String title, String body, String link) throws IOException {
        String message = makeMessage(targetToken, title, body, link);

        RequestBody requestBody = RequestBody.create(
                message, MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                log.info("fcm 결과 : {}", response.body().string());
            } else {
                log.warn("FCM 응답 바디가 비어 있습니다.");
            }
        }
    }

    private String makeMessage(String targetToken, String title, String body, String link)
            throws JsonProcessingException {

        FCMMessage fcmMessage = new FCMMessage(
                false,
                new FCMMessage.Message(
                        new FCMMessage.Notification(title, body, null),
                        targetToken,
                        new FCMMessage.Webpush(new FCMMessage.FcmOptions(link))
                )
        );

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of(googleScope));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.error("FCM AccessToken 발급 중 오류 발생", e);
            throw new RestApiException(FCM_ERROR.getErrorCode());
        }
    }
}
