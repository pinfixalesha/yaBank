package ru.yandex.practicum.yaBank.exchangeApplicationTest;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseContractTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String baseUrl;

    @BeforeEach
    public void setUp() {
        this.baseUrl = "http://localhost:" + port;
    }
}
