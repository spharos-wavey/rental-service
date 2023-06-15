package xyz.wavey.rentalservice.base.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class KakaoOpenFeignConfig {

    @Value("${kakao.key.admin}")
    private String kakaoPayApiKey;

    @Value("${kakao.content-type}")
    private String kakaoApiContentType;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new KakaoOpenFeignHeaderInterceptor(kakaoPayApiKey, kakaoApiContentType);
    }
}
