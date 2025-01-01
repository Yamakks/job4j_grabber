package ru.job4j.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirFileCache extends AbstractCache<String, String> {

    private final String cachingDir;

    public DirFileCache(String cachingDir) {
        if (cachingDir == null || cachingDir.isBlank()) {
            throw new IllegalArgumentException("Дирректория не может быть null или empty");
        }
        this.cachingDir = cachingDir;
    }


    @Override
    protected String load(String key) {
        Path filePath = Paths.get(cachingDir, key);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("Файл не найден:" + filePath);
        }

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла:" + filePath, e);
        }
    }
}
