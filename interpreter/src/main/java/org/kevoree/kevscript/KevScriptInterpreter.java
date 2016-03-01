package org.kevoree.kevscript;

import org.KevoreeModel;
import org.KevoreeView;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.kevoree.Model;
import org.kevoree.modeling.KCallback;
import org.kevoree.modeling.memory.manager.DataManagerBuilder;
import org.kevoree.modeling.scheduler.impl.DirectScheduler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 *
 * Created by leiko on 3/1/16.
 */
public class KevScriptInterpreter {

    public void parse(final String script, final KCallback<Model> callback) {
        if (callback == null) {
            throw new IllegalArgumentException("The callback parameter cannot be null");
        } else {
            final KevoreeModel kModel = new KevoreeModel(DataManagerBuilder.create().withScheduler(new DirectScheduler()).build());
            kModel.connect(new KCallback() {
                @Override
                public void on(Object o) {
                    KevoreeView kView = kModel.universe(0).time(0);
                    Model model = kView.createModel();

                    KevScriptLexer l = new KevScriptLexer(new ANTLRInputStream(script));
                    KevScriptParser p = new KevScriptParser(new CommonTokenStream(l));
                    p.addErrorListener(new BaseErrorListener() {
                        @Override
                        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                            throw new IllegalStateException("failed to parse at line " + line + ":" + charPositionInLine + " due to " + msg, e);
                        }
                    });

                    ParseTree tree = p.script();
                    ParseTreeWalker walker = new ParseTreeWalker();
                    walker.walk(new KevScriptWalker(model, kView), tree);

                    callback.on(model);
                }
            });
        }
    }

    public void parse(final File file, final KCallback<Model> callback) throws IOException {
        this.parse(FileUtils.readFileToString(file), callback);
    }

    public void parse(final InputStream is, final KCallback<Model> callback) throws IOException {
        this.parse(IOUtils.toString(is), callback);
    }
}
