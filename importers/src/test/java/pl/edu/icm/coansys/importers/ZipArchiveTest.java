/*
 * (C) 2010-2012 ICM UW. All rights reserved.
 */
package pl.edu.icm.coansys.importers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import static org.testng.Assert.*;
import pl.edu.icm.coansys.importers.utils.ZipArchive;

/**
 *
 * @author Artur Czeczko
 */
public class ZipArchiveTest {

    @org.testng.annotations.Test(groups = {"fast"})
    public void myTest() throws IOException {
        URL zipPath = this.getClass().getClassLoader().getResource("test_archive.zip");
        ZipArchive archive = new ZipArchive(zipPath.getPath());

        assertEquals(3, archive.listFiles().size());
        assertTrue(archive.listFiles().contains("directory/file1.txt"));
        assertEquals(2, archive.filter(".*txt").size());

        BufferedReader br = new BufferedReader(new InputStreamReader(archive.getFileAsInputStream("directory/file2.dat")));
        assertEquals("test file 2", br.readLine());
    }
}
