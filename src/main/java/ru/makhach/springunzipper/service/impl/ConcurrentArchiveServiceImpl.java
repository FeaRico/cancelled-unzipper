package ru.makhach.springunzipper.service.impl;

import org.springframework.stereotype.Service;
import ru.makhach.springunzipper.models.ArchiveTask;
import ru.makhach.springunzipper.service.Archiver;
import ru.makhach.springunzipper.service.ConcurrentArchiveService;
import ru.makhach.springunzipper.service.KeyGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class ConcurrentArchiveServiceImpl implements ConcurrentArchiveService {
    private final ThreadPoolExecutor threadPoolExecutor;
    private final Archiver archiver;
    private final KeyGenerator keyGenerator;
    private final Map<String, Future<Integer>> taskMap;

    public ConcurrentArchiveServiceImpl(ThreadPoolExecutor threadPoolExecutor, Archiver archiver, KeyGenerator keyGenerator) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.archiver = archiver;
        this.keyGenerator = keyGenerator;
        this.taskMap = new ConcurrentHashMap<>();
    }

    @Override
    public String process(String zipPath) {
        Path path = Paths.get(zipPath);
        if (!path.toFile().exists())
            throw new IllegalStateException("Not exists file");

        String key = keyGenerator.generate();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ArchiveTask archiveTask = new ArchiveTask(path, () -> {

            new Thread(() -> {
                try {
                    countDownLatch.await();
                    Future<Integer> future = taskMap.get(key);
                    if (future != null) {
                        taskMap.remove(key);
                        System.out.printf("Задача завершена: %x распакованных файлов.%n", future.get());
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }).start();

            Integer count = archiver.unzip(path);
            countDownLatch.countDown();
            return count;
        });

        Future<Integer> future = threadPoolExecutor.submit(archiveTask);
        this.taskMap.put(key, future);

        return key;
    }

    @Override
    public boolean cancel(String key) {
        Future<Integer> future = taskMap.get(key);
        if (future != null) {
            taskMap.remove(key);
            return future.cancel(true);
        }
        return false;
    }
}
