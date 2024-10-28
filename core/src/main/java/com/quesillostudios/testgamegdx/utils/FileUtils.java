package com.quesillostudios.testgamegdx.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileUtils {

    public static FileHandle GetClassPath(String filepath) {
        return Gdx.files.classpath(filepath);
    }

    public static FileHandle GetInternalPath(String filepath) {
        return Gdx.files.internal(filepath);
    }

    public static FileHandle GetLocalPath(String filepath) {
        return Gdx.files.local(filepath);
    }

    public static FileHandle GetExternalPath(String filepath) {
        return Gdx.files.external(filepath);
    }
}
