package org.kevoree.kevscript.language;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kevoree.kevscript.language.utils.HttpServer;
import org.kevoree.kevscript.language.utils.UrlDownloader;

import java.net.URL;

/**
 * Created by mleduc on 30/03/16.
 */
public class UrlDownloaderTest {


    private final UrlDownloader urlDownloader = new UrlDownloader();
    private final HttpServer httpServer = new HttpServer(8080);

    @Before
    public void setUp() throws Exception {
        this.httpServer.buildAndStartServer();

    }

    @Test
    public void test1() throws Exception {
        final String result = urlDownloader.saveUrl(new URL("http://localhost:8080/file1.txt"));
        Assert.assertEquals("ok", result);
    }

    @After
    public void tearDown() throws Exception {
        this.httpServer.stop();

    }
}
