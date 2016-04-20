package org.kevoree.kevscript.language.cli;

import org.apache.commons.cli.*;
import org.apache.commons.io.IOUtils;
import org.kevoree.kevscript.language.KevscriptInterpreter;
import org.kevoree.kevscript.language.commands.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by mleduc on 20/04/16.
 */
public class ApplicationCLI {
    public static void main(String[] args) throws IOException {
        final Options options = new Options();
        options.addOption("s" ,"script", true, "kevscript to execute");

        final CommandLineParser commandLineParser = new DefaultParser();
        try {
            final CommandLine commandLine = commandLineParser.parse(options, args);
            final String scriptpath = commandLine.getOptionValue("script");

            System.out.println(scriptpath);
            final String script = IOUtils.toString(new FileInputStream(new File(scriptpath)));
            final Commands res = new KevscriptInterpreter().interpreter(script, new File(".").getCanonicalPath());
            System.out.println(res);
        } catch (ParseException e) {
            System.err.println("Wrong options : " + e.getMessage());
            new HelpFormatter().printHelp("kevscript-cli", options);
        }
    }
}
