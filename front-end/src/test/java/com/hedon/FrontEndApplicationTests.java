package com.hedon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FrontEndApplicationTests {

    @Test
    void contextLoads() {
        int i = 0xffff;
        System.out.println(i);
    }

}
