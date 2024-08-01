package ru.otus.hw.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.Caterpillar;
import ru.otus.hw.models.Chrysalis;

import java.util.List;

@SpringBootTest
public class CaterpillarGatewayTest {

    @Autowired
    CaterpillarGateway caterpillarGateway;

    @Test
    public void caterpillarGatewayTest() {
        List<Chrysalis> chrysalis = caterpillarGateway.process(List.of(new Caterpillar(1, 1)));

        Assertions.assertNotNull(chrysalis);
        Assertions.assertEquals(1, chrysalis.size());
        Assertions.assertNotNull(chrysalis.get(0));
    }
}
