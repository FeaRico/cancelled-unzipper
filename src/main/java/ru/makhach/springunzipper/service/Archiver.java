package ru.makhach.springunzipper.service;

import java.nio.file.Path;

public interface Archiver {
    Integer unzip(Path zip);
}
