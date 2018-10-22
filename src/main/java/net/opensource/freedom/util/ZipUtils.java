package net.opensource.freedom.util;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public abstract class ZipUtils {

    public static void unzip(File srcZipFile, File targetDir) {
        try (ZipFile zipFile = new ZipFile(srcZipFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(targetDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    InputStream in = zipFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    out.close();
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static File unzip(File srcZipFile) {
        try {
            Path tempDirectory = Files.createTempDirectory("util4j-unzip");
            unzip(srcZipFile, tempDirectory.toFile());
            return tempDirectory.toFile();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void unzip(InputStream inputStream, File targetDir) {
        try (ZipInputStream zis = new ZipInputStream(inputStream)) {
            while (true) {
                ZipEntry entry = zis.getNextEntry();
                if (Objects.isNull(entry)) {
                    break;
                }

                File entryDestination = new File(targetDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    entryDestination.getParentFile().mkdirs();
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(zis, out);
                    out.close();
                }

            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private final static Pattern OuterJarArchivePattern = Pattern.compile("file:(.*\\.jar)!(.*\\.jar)");

    public static String proceedIfWithOuterJar(String path) {
        Matcher matcher = OuterJarArchivePattern.matcher(path);
        if (matcher.find()) {
            String outerJarPath = matcher.group(1);
            String nestedJarPath = matcher.group(2);
            return unzip(new File(outerJarPath)) + nestedJarPath;
        }
        return path;
    }

    public static void main(String[] args) {
        String file = "file:/Users/lvtu/workspace/sandbox-boot-agent/target/sandbox-boot-agent.jar!/sandbox/lib/sandbox-agent.jar!/";
        System.out.println(proceedIfWithOuterJar(file));
    }

}
