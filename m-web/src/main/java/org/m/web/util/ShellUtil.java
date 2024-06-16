package org.m.web.util;

public class ShellUtil {

    public static String getExePath(String bin, String name) {
        if (name.endsWith(".sh")) {
            return "/bin/sh";
        }
        return bin;
    }
}
