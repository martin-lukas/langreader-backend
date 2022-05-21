package dev.mlukas.langreader.text;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class LangReaderApplicationTest {
    private final ApplicationContext context;

    LangReaderApplicationTest(ApplicationContext context) {
        this.context = context;
    }

    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}
