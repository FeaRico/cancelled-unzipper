package ru.makhach.springunzipper.service;

public interface ConcurrentArchiveService {
    String process(String zipName);

    boolean cancel(String key);
}
