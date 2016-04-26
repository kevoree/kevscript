package org.kevoree.kevscript.language;

import org.junit.*;
import org.kevoree.kevscript.language.utils.HttpServer;
import org.kevoree.kevscript.language.utils.UrlDownloader;

import java.net.URL;

/**
 *
 * Created by mleduc on 30/03/16.
 */
public class UrlDownloaderTest {


    private final UrlDownloader urlDownloader = new UrlDownloader();
    private HttpServer httpServer;

    @Before
    public void setUp() throws Exception {
        this.httpServer = new HttpServer(8080, "url_downloader_resources");
        this.httpServer.buildAndStartServer();
    }

    @Test
    @Ignore
    public void test1() throws Exception {
        final String result = urlDownloader.saveUrl(new URL("http://localhost:8080/file1.txt"));
        Assert.assertEquals("ok", result);
    }

    @After
    public void tearDown() throws Exception {
        this.httpServer.stop();

    }
}
