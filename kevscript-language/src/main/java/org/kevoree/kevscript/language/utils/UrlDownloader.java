package org.kevoree.kevscript.language.utils;

import java.io.*;
import java.net.URL;

/**
 * Created by mleduc on 30/03/16.
 */
public class UrlDownloader {
    public String saveUrl(final URL url) throws IOException {
        BufferedReader in = null;
        final StringBuilder sb = new StringBuilder();
        try {
            final Reader in1 = new InputStreamReader(new BufferedInputStream(url.openStream()));
            in = new BufferedReader(in1);

            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return sb.toString().trim();
    }
}
