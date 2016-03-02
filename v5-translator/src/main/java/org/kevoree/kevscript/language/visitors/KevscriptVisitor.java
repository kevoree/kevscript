package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.language.assignable.Assignable;
import org.kevoree.kevscript.language.assignable.StringAssignable;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.LoopContext;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.CustomException;

import java.util.ArrayList;
import java.util.List;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<String> {

    private final Context rootContext;

    public KevscriptVisitor() {
        this.rootContext = new RootContext();
    }

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
        for (Left_add_definitionContext elem : ctx.list_add_members.members) {
            final String elementId = elem.getText();
            boolean res = rootContext.getSetInstances().add(elementId);
            if (!res) {
                // TODO dealing with function scopes
                throw new CustomException("instance " + elementId + " already declared in this scope");
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
        final String varName = ctx.varName.getText();
        final Assignable value = new AssignableVisitor().visit(ctx.val);
        rootContext.getMapIdentifiers().put(varName, value);
        return "";
    }

    @Override
    public String visitLong_identifier(Long_identifierContext ctx) {
        final String identifier = ctx.short_identifier(0).getText();
        if (rootContext.getMapIdentifiers().containsKey(identifier)) {
            return rootContext.getMapIdentifiers().get(identifier).toText();
        } else {
            throw new CustomException("Can't find " + identifier);
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
        if (!rootContext.getSetInstances().contains(groupId)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "attach " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitDetach(DetachContext ctx) {
        final String groupId = ctx.groupId.getText();
        if (!rootContext.getSetInstances().contains(groupId)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "detach " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitBind(BindContext ctx) {
        final String groupId = ctx.chan.getText();
        if (!rootContext.getSetInstances().contains(groupId)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "bind " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitUnbind(UnbindContext ctx) {
        final String groupId = ctx.chan.getText();
        if (!rootContext.getSetInstances().contains(groupId)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "unbind " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitLong_identifiers(Long_identifiersContext ctx) {
        final List l = new ArrayList<>();
        for (Long_identifierContext x : ctx.long_identifier()) {
            final String textId = x.getText();
            if (!rootContext.getSetInstances().contains(textId)) {
                throw new CustomException("instance " + textId + " not found");
            }
            l.add(textId);
        }
        return StringUtils.join(l, ", ");
    }

    @Override
    public String visitAssignable(AssignableContext ctx) {
        return super.visitAssignable(ctx);
    }


    @Override
    public String visitFunction_operation(Function_operationContext ctx) {
        rootContext.getSetInstances().add(ctx.functionName.getText());
        // TODO flatten function content
        return "";
    }

    @Override
    public String visitArray(ArrayContext ctx) {
        return super.visitArray(ctx);
    }

    @Override
    public String visitAssignables(AssignablesContext ctx) {
        return super.visitAssignables(ctx);
    }

    @Override
    public String visitBasic_operation(Basic_operationContext ctx) {
        return super.visitBasic_operation(ctx);
    }

    @Override
    public String visitFor_loop(For_loopContext ctx) {
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for(AssignableContext loopCurrentElement : ctx.iterator.assignable()) {
            final LoopContext context = new LoopContext(rootContext);
            if(ctx.index != null) {
                final String indexVariableName = ctx.index.getText();
                context.getMapIdentifiers().put(indexVariableName, new StringAssignable(String.valueOf(index)));
            }

            final String valueVariableName = ctx.val.getText();
            context.getMapIdentifiers().put(valueVariableName, new AssignableVisitor().visit(loopCurrentElement));

            sb.append(new ForLoopVisitor(context).visit(ctx.body));
            sb.append('\n');
            index++;
        }
        return sb.toString();
    }

    @Override
    public String visitFunction_call(Function_callContext ctx) {
        return super.visitFunction_call(ctx);
    }

    @Override
    public String visitKeyAndValue(KeyAndValueContext ctx) {
        return super.visitKeyAndValue(ctx);
    }

    @Override
    public String visitLeft_add_definition(Left_add_definitionContext ctx) {
        return super.visitLeft_add_definition(ctx);
    }

    @Override
    public String visitLeft_add_definitions(Left_add_definitionsContext ctx) {
        return super.visitLeft_add_definitions(ctx);
    }

    @Override
    public String visitLeft_hand_identifier(Left_hand_identifierContext ctx) {
        return super.visitLeft_hand_identifier(ctx);
    }

    @Override
    public String visitLeft_hand_identifiers(Left_hand_identifiersContext ctx) {
        return super.visitLeft_hand_identifiers(ctx);
    }

    @Override
    public String visitMetainit(MetainitContext ctx) {
        return super.visitMetainit(ctx);
    }

    @Override
    public String visitMetamerge(MetamergeContext ctx) {
        return super.visitMetamerge(ctx);
    }

    @Override
    public String visitMetaremove(MetaremoveContext ctx) {
        return super.visitMetaremove(ctx);
    }

    @Override
    public String visitNetinit(NetinitContext ctx) {
        return super.visitNetinit(ctx);
    }

    @Override
    public String visitNetmerge(NetmergeContext ctx) {
        return super.visitNetmerge(ctx);
    }

    @Override
    public String visitNetremove(NetremoveContext ctx) {
        return super.visitNetremove(ctx);
    }

    @Override
    public String visitObject(ObjectContext ctx) {
        return super.visitObject(ctx);
    }

    @Override
    public String visitRemove(RemoveContext ctx) {
        return super.visitRemove(ctx);
    }

    @Override
    public String visitSet(SetContext ctx) {

        // TODO : check that the key is a proper instance
        final String slash;
        if(ctx.SLASH() != null) {
            slash = "/" + ctx.frag.getText();
        }
        else {
            slash="";
        }
        return "set " + ctx.key.getText() + slash + " = " + new AssignableVisitor().visit(ctx.val).toText();
    }

    @Override
    public String visitShort_identifier(Short_identifierContext ctx) {
        return super.visitShort_identifier(ctx);
    }

    @Override
    public String visitSpecial_internal_operation(Special_internal_operationContext ctx) {
        return super.visitSpecial_internal_operation(ctx);
    }

    @Override
    public String visitStart(StartContext ctx) {
        return super.visitStart(ctx);
    }

    @Override
    public String visitStop(StopContext ctx) {
        return super.visitStop(ctx);
    }

    @Override
    public String visitString(StringContext ctx) {
        return super.visitString(ctx);
    }
}
