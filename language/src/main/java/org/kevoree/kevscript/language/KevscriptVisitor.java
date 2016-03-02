package org.kevoree.kevscript.language;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.KevScriptBaseVisitor;

import java.util.*;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<String> {

    private Map<String, String> mapIdentifiers = new HashMap<>();
    private Set<String> setInstances = new HashSet<>();

    @Override
    public String visitScript(ScriptContext ctx) {
        StringBuilder sb = new StringBuilder();
        for (ParseTree a : ctx.children) {
            sb.append(this.visit(a));
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String visitAdd(AddContext ctx) {
        final List instances = new ArrayList();
        for(Left_add_definitionContext elem : ctx.list_add_members.members) {
            final String elementId = elem.getText();
            boolean res = setInstances.add(elementId);
            if(!res) {
                // TODO dealing with function scopes
                throw new IllegalArgumentException("instance " + elementId + " already declared in this scope");
            }
            instances.add(elementId);
        }

        return "add " + StringUtils.join(instances, ", ") + " : " + visit(ctx.typeDef);
    }

    @Override
    public String visitType(TypeContext ctx) {
        final String version = buildVersion(ctx);
        return StringUtils.join(ctx.ID(), ".") + version;
    }

    private String buildVersion(TypeContext ctx) {
        final String version;
        final boolean hasSlash = ctx.SLASH() != null;
        if (hasSlash) {
            if (ctx.NUMERIC_VALUE() != null) {
                version = "/" + convertVersion(ctx.NUMERIC_VALUE().getText());
            } else {
                version = "/" + convertVersion(visit(ctx.long_identifier(0)));
            }
        } else {
            version = "";
        }
        return version;
    }

    private String convertVersion(String text) {
        return StringUtils.join(StringUtils.rightPad(text, 3, "0").toCharArray(), '.');
    }

    @Override
    public String visitLet_operation(Let_operationContext ctx) {
        mapIdentifiers.put(ctx.varName.getText(), visit(ctx.val));
        return "";
    }

    @Override
    public String visitLong_identifier(Long_identifierContext ctx) {
        if (mapIdentifiers.containsKey(ctx.short_identifier(0).getText())) {
            return mapIdentifiers.get(ctx.short_identifier(0).getText());
        } else {
            throw new IllegalArgumentException("Can't find " + ctx.short_identifier(0).getText());
        }
    }

    @Override
    public String visitSq_string(Sq_stringContext ctx) {
        return ctx.value.getText();
    }

    @Override
    public String visitDq_string(Dq_stringContext ctx) {
        return ctx.value.getText();
    }

    @Override
    public String visitAttach(AttachContext ctx) {
        final String groupId = ctx.groupId.getText();
        if(!setInstances.contains(groupId)) {
            throw  new IllegalArgumentException("instance " + groupId + " not found");
        }
        return "attach " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitDetach(DetachContext ctx) {
        final String groupId = ctx.groupId.getText();
        if(!setInstances.contains(groupId)) {
            throw  new IllegalArgumentException("instance " + groupId + " not found");
        }
        return "detach " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitBind(BindContext ctx) {
        final String groupId = ctx.chan.getText();
        if(!setInstances.contains(groupId)) {
            throw  new IllegalArgumentException("instance " + groupId + " not found");
        }
        return "bind " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitUnbind(UnbindContext ctx) {
        final String groupId = ctx.chan.getText();
        if(!setInstances.contains(groupId)) {
            throw  new IllegalArgumentException("instance " + groupId + " not found");
        }
        return "unbind " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitLong_identifiers(Long_identifiersContext ctx) {
        final List l = new ArrayList<>();
        for( Long_identifierContext x : ctx.long_identifier()) {
            final String textId = x.getText();
            if(!setInstances.contains(textId)) {
                throw new IllegalArgumentException("instance " + textId + " not found");
            }
            l.add(textId);
        }
        return StringUtils.join(l, ", ");
    }


}
