package org.kevoree.kevscript;

import org.junit.Test;
import org.kevoree.Model;
import org.kevoree.modeling.KCallback;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
//import java.util.function.Consumer;

/**
 * Created by leiko on 3/1/16.
 */
public class TestInterpreter {

    @Test
    public void test() throws IOException, URISyntaxException {
       /* Files.list(Paths.get(getClass().getResource("/").toURI())).forEach(new Consumer<Path>() {
            @Override
            public void accept(Path path) {
                if (path.toString().endsWith(".kevs")) {
                    try {
                        testKevScript(path.toFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });*/
    }

    private void testKevScript(File file) throws IOException {
        System.out.println("Testing: " + file.getName());
        KevScriptInterpreter kevs = new KevScriptInterpreter();
        kevs.parse(file, new KCallback<Model>() {
            @Override
            public void on(Model model) {
                System.out.println(model.toJSON());
            }
        });
    }
}
