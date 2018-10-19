package net.opensource.freedom.util;


import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZipUtilsTest {

    @Test
    public void unzip() throws Exception {
        File file = new File("");
        Path unzip = Files.createTempDirectory("unzip");
        System.out.println(unzip.toString());
        ZipUtils.unzip(file, unzip.toFile());
    }


}
