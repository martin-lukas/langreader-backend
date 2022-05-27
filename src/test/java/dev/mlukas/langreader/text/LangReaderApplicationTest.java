package dev.mlukas.langreader.text;

import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

// TODO: Enable after implementing initialization for test DB
//@SpringBootTest
class LangReaderApplicationTest {
    private final ApplicationContext context;

    LangReaderApplicationTest(ApplicationContext context) {
        this.context = context;
    }

//    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}
