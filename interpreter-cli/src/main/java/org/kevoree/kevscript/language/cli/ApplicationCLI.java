package org.kevoree.kevscript.language.cli;

import org.kevoree.kevscript.language.utils.StringUtils;

/**
 * Created by mleduc on 20/04/16.
 */
public class ApplicationCLI {
    public static void main(String[] args) {
        System.out.println("Hello :");
        for(String a : args) {
            System.out.println(a);
        }
    }
}
