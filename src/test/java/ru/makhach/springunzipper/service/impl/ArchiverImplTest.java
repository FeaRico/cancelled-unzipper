package ru.makhach.springunzipper.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.makhach.springunzipper.service.Archiver;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
class ArchiverImplTest {
    private final Archiver archiver;

    @Autowired
    ArchiverImplTest(Archiver archiver) {
        this.archiver = archiver;
    }

    @Test
    void testUnzip() {
        Path path = Paths.get("D:\\Downloads\\").resolve("Friday the 13th The Game.zip");
        Integer unzip = archiver.unzip(path);
        Assertions.assertEquals(1, unzip);
    }
}