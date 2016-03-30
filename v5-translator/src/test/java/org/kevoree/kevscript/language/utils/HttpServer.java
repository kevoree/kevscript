package org.kevoree.kevscript.language.utils;

import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static io.undertow.Handlers.resource;

/**
 * Created by mleduc on 30/03/16.
 */
public class HttpServer {

    private final int port;
    private Undertow server;

    public HttpServer(int port) {
        this.port = port;
    }

    public void buildAndStartServer() {
        final File fileFromURL = getFileFromURL();
        final PathResourceManager resourceManager = new PathResourceManager(fileFromURL.toPath(), 100);
        final ResourceHandler resource = resource(resourceManager);
        final ResourceHandler handler = resource.setDirectoryListingEnabled(true);
        server = Undertow.builder()
                .addHttpListener(8080, "localhost")
                .setHandler(handler)
                .build();
        server.start();
    }

    private File getFileFromURL() {
        URL url = this.getClass().getClassLoader().getResource("url_downloader_resources");
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file;
        }
    }

    public void stop() {
        server.stop();
    }
}
