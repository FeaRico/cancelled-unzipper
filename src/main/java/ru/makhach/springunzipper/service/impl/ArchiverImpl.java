package ru.makhach.springunzipper.service.impl;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.FileNameUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import ru.makhach.springunzipper.service.Archiver;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ArchiverImpl implements Archiver {
    @Override
    public Integer unzip(Path zip) {
        Path parent = zip.getParent();
        String name = zip.toFile().getName();
        Path target = parent.resolve(FileNameUtils.getBaseName(name));

        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();

        Integer countFiles = 0;

        try (InputStream is = Files.newInputStream(zip);
             ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(ArchiveStreamFactory.ZIP, is)) {

            ArchiveEntry entry;

            while ((entry = ais.getNextEntry()) != null) {
                if (Thread.currentThread().isInterrupted())
                    break;

                Path path = Paths.get(target.toString(), entry.getName());
                File file = path.toFile();

                if (entry.isDirectory()) {
                    if (!file.isDirectory())
                        file.mkdirs();
                } else {
                    File parentFile = file.getParentFile();
                    if (!parentFile.isDirectory())
                        parentFile.mkdirs();
                    try (OutputStream outputStream = Files.newOutputStream(path)) {
                        if (!Thread.currentThread().isInterrupted()) {
                            IOUtils.copy(ais, outputStream);
                            countFiles++;
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countFiles;
    }
}
