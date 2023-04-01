package ru.makhach.springunzipper.models;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Supplier;

public class ArchiveTask implements CancelledTask<Integer> {
    private final Path path;
    private final Supplier<Integer> supplier;

    public ArchiveTask(Path path, Supplier<Integer> supplier) {
        this.path = path;
        this.supplier = supplier;
    }

    @Override
    public Integer call() throws Exception {
        return supplier.get();
    }

    @Override
    public boolean cancel() {
        String name = path.toFile().getName();
        Path parent = path.getParent();
        String baseName = FileNameUtils.getBaseName(name);
        Path path = parent.resolve(baseName);
        try {
            return FileSystemUtils.deleteRecursively(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
