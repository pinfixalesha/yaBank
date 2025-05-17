package ru.yandex.practicum.yaBank.gatewayService.configurations;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilterConfiguration implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Логирование входящего запроса
        ServerHttpRequestDecorator decoratedRequest=logRequest(exchange.getRequest());

        // Заменяем исходный запрос на наш декорированный
        return chain.filter(exchange.mutate().request(decoratedRequest).build())
                .then(Mono.fromRunnable(() -> {
                    logResponse(exchange.getResponse());
                }));
    }

    private ServerHttpRequestDecorator logRequest(ServerHttpRequest request) {
        // Логируем URL и заголовки запроса
        System.out.println("========= Incoming Request =========");
        System.out.println("URI: " + request.getURI());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Headers: " + request.getHeaders());

        // Оборачиваем оригинальный запрос
        return new ServerHttpRequestDecorator(request) {
            private boolean bodyLogged = false;

            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().map(dataBuffer -> {
                    if (!bodyLogged) {
                        // Читаем содержимое DataBuffer
                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.asByteBuffer().get(content); // безопасное чтение
                        String body = new String(content, StandardCharsets.UTF_8);

                        // Логируем тело запроса
                        System.out.println("Request Body: " + body);
                        bodyLogged = true;

                        // Возвращаем буфер обратно в цепочку обработки
                        return dataBuffer;
                    }
                    return dataBuffer;
                });
            }
        };
    }

    private void logResponse(ServerHttpResponse response) {
        System.out.println("========= Outgoing Response =========");
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Headers: " + response.getHeaders());
    }
}