package ru.makhach.springunzipper.service.impl;

import org.springframework.stereotype.Service;
import ru.makhach.springunzipper.service.KeyGenerator;

import java.util.UUID;

@Service
public class KeyGeneratorImpl implements KeyGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
