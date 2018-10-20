package net.opensource.freedom.util;


import org.junit.Test;
import org.springframework.boot.loader.archive.Archive;
import org.springframework.boot.loader.archive.ExplodedArchive;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class ZipUtilsTest {

    @Test
    public void unzip() throws Exception {
        File file = new File("");
        Path unzip = Files.createTempDirectory("unzip");
        System.out.println(unzip.toString());
        ZipUtils.unzip(file, unzip.toFile());
    }

    public static String process(URL url) {
        return url.toString().replace("!", "").replace("jar:file:", "");
    }

    public static void todoMethod() {
        try {
            File f = new File("/Users/lvtu/workspace/util4j/target/boot.jar");
            File unzip = ZipUtils.unzip(f);
//            Archive explodedArchive = new JarFileArchive(f);
            Archive explodedArchive = new ExplodedArchive(unzip);
            List<Archive> nestedArchives = explodedArchive.getNestedArchives(entry -> entry.getName().endsWith(".jar"));
            List<URL> urlList = nestedArchives.stream().map(nestedArchive -> {
                try {
                    return nestedArchive.getUrl();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
            urlList.forEach(url -> {
                System.out.println(url);
            });
            List<JarFile> jarFiles = urlList.stream().map(url -> {
                try {
                    return new JarFile(process(url));
//                    return new JarFile();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList());
            System.out.println(jarFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
