package org.kevoree.kevscript.language.visitors;

import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.lang3.StringUtils;
import org.kevoree.kevscript.KevScriptBaseVisitor;
import org.kevoree.kevscript.KevScriptParser;
import org.kevoree.kevscript.language.assignable.Assignable;
import org.kevoree.kevscript.language.assignable.FunctionAssignable;
import org.kevoree.kevscript.language.assignable.InstanceAssignable;
import org.kevoree.kevscript.language.assignable.StringAssignable;
import org.kevoree.kevscript.language.context.Context;
import org.kevoree.kevscript.language.context.RootContext;
import org.kevoree.kevscript.language.excpt.CustomException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.kevoree.kevscript.KevScriptParser.*;

/**
 * Created by mleduc on 01/03/16.
 */
public class KevscriptVisitor extends KevScriptBaseVisitor<String> {

    private final Context context;

    public KevscriptVisitor() {
        this.context = new RootContext();
    }

    public KevscriptVisitor(Context context) {
        this.context = context;
    }

    @Override
    public String visitScript(ScriptContext ctx) {
        final StringBuilder sb = new StringBuilder();
        for (ParseTree a : ctx.children) {
            final String visit = this.visit(a);
            sb.append(visit);
            sb.append('\n');
        }
        final String res1 = sb.toString();
        return cleanupBlankLines(res1);
    }

    private String cleanupBlankLines(String res1) {
        final StringBuilder sb2 = new StringBuilder();
        for(String a : res1.split("\n")) {
            if(!StringUtils.isBlank(a)) {
                sb2.append(a);
                sb2.append('\n');
            }
        }
        return sb2.toString();
    }

    @Override
    public String visitAdd(AddContext ctx) {
        final List instances = new ArrayList();
        /*for (LeftAddDefinitionContext elem : ctx.listAddMembers.members) {
            final String elementId = resolveInstanceIdentifier(elem);
            / *if (context.getMapIdentifiers().containsKey(elementId)) {
                throw new CustomException("instance " + elementId + " already declared in this scope");
            } * /
            context.getMapIdentifiers().put(elementId, new InstanceAssignable());
            instances.add(elementId);
        }*/

        return "add " ;//+ StringUtils.join(instances, ", ") + " : " + visit(ctx.typeDef);
    }

    /*private String resolveInstanceIdentifier(Left_add_definitionContext elem) {
        final List<String> resolved = new ArrayList<>();
        for(Long_identifier_chunkContext a : elem.long_identifier_chunk()) {
            if(a.dereference() != null) {
                final String atValue = a.dereference().assignable().getText();
                final Assignable referenceVariable = context.getMapIdentifiers().get(atValue);
                resolved.add(referenceVariable.toText());
            } else {
                resolved.add(a.getText());
            }
        }
        return StringUtils.join(resolved, '.');
    }*/

    @Override
    public String visitType(TypeContext ctx) {
        final String version = buildVersion(ctx);
        return "TODO"; //StringUtils.join(ctx.ID(), ".") + version;
    }

    private String buildVersion(TypeContext ctx) {
        final String version;
        /*final boolean hasSlash = ctx.SLASH() != null;
        if (hasSlash) {
            if (ctx.NUMERIC_VALUE() != null) {
                version = "/" + convertVersion(ctx.NUMERIC_VALUE().getText());
            } else {
                version = "/" + convertVersion(visit(ctx.long_identifier(0)));
            }
        } else {
            version = "";
        }*/
        return "TODO";
    }

    private String convertVersion(String text) {
        return StringUtils.join(StringUtils.rightPad(text, 3, "0").toCharArray(), '.');
    }

   /* @Override
    public String visitLet_operation(Let_operationContext ctx) {
        final String varName = ctx.varName.getText();
        final Assignable value = new AssignableVisitor(context).visit(ctx.val);
        context.getMapIdentifiers().put(varName, value);

        final String ret;
        if(value instanceof FunctionAssignable) {
            ret = value.toText();
        } else {
            ret = "";
        }
        return ret;
    }*/

    /*@Override
    public String visitLong_identifier(Long_identifierContext ctx) {
        final String identifier = ctx.identifiers.get(0).getText();
        // TODO : looking for mainGroup in identifiers but is really an instance !
        if (context.getMapIdentifiers().containsKey(identifier)) {
            return context.getMapIdentifiers().get(identifier).toText();
        } else {
            throw new CustomException("Can't find " + identifier);
        }
    }*/


    @Override
    public String visitAttach(AttachContext ctx) {
        final String groupId = ctx.groupId.getText();

        // TODO when calling a function, add the elements resolve to an instance in the instances context !
        if (!context.getMapIdentifiers().containsKey(groupId) || !(context.getMapIdentifiers().get(groupId) instanceof InstanceAssignable)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "attach " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitDetach(DetachContext ctx) {
        final String groupId = ctx.groupId.getText();
        if (!context.getMapIdentifiers().containsKey(groupId) || !(context.getMapIdentifiers().get(groupId) instanceof InstanceAssignable)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "detach " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitBind(BindContext ctx) {
        final String groupId = ctx.chan.getText();
        if (!context.getMapIdentifiers().containsKey(groupId) || !(context.getMapIdentifiers().get(groupId) instanceof InstanceAssignable)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "bind " + visit(ctx.nodes) + " " + groupId;
    }

    @Override
    public String visitUnbind(UnbindContext ctx) {
        final String groupId = ctx.chan.getText();
        if (!context.getMapIdentifiers().containsKey(groupId) || !(context.getMapIdentifiers().get(groupId) instanceof InstanceAssignable)) {
            throw new CustomException("instance " + groupId + " not found");
        }
        return "unbind " + visit(ctx.nodes) + " " + groupId;
    }

    /*@Override
    public String visitLong_identifiers(Long_identifiersContext ctx) {
        final List l = new ArrayList<>();
        for (Long_identifierContext x : ctx.long_identifier()) {
            final String textId = x.getText();
            if (!context.getMapIdentifiers().containsKey(textId) || !(context.getMapIdentifiers().get(textId) instanceof InstanceAssignable)) {
                throw new CustomException("instance " + textId + " not found");
            }
            l.add(textId);
        }
        return StringUtils.join(l, ", ");
    }*/

    /*@Override
    public String visitAssignable(AssignableContext ctx) {
        return new AssignableVisitor(this.context).visit(ctx).toText();
    }*/


   /* @Override
    public String visitFunction_operation(Function_operationContext ctx) {
        // we do not offer scope to functions.
        context.getSetFunctions().put(ctx.functionName.getText(), ctx);
        return "";
    }*/

    /*@Override
    public String visitArray(ArrayContext ctx) {
        return super.visitArray(ctx);
    }*/

    /*@Override
    public String visitAssignables(AssignablesContext ctx) {
        return super.visitAssignables(ctx);
    }*/

    /*@Override
    public String visitBasic_operation(Basic_operationContext ctx) {
        return super.visitBasic_operation(ctx);
    }*/

    /*@Override
    public String visitFor_loop(For_loopContext ctx) {
        final StringBuilder sb = new StringBuilder();
        int index = 0;
        for (AssignableContext loopCurrentElement : ctx.iterator.assignable()) {
            final RootContext context = new RootContext(this.context);
            if (ctx.index != null) {
                final String indexVariableName = ctx.index.getText();
                context.getMapIdentifiers().put(indexVariableName, new StringAssignable(String.valueOf(index)));
            }

            final String valueVariableName = ctx.val.getText();
            context.getMapIdentifiers().put(valueVariableName, new AssignableVisitor(context).visit(loopCurrentElement));

            sb.append(new KevscriptVisitor(context).visit(ctx.for_body()));
            index++;
        }
        return sb.toString();
    }*/

    @Override
    public String visitKeyAndValue(KeyAndValueContext ctx) {
        return super.visitKeyAndValue(ctx);
    }

    /*@Override
    public String visitLeft_add_definition(Left_add_definitionContext ctx) {
        return super.visitLeft_add_definition(ctx);
    }*/

    /*@Override
    public String visitLeft_add_definitions(Left_add_definitionsContext ctx) {
        return super.visitLeft_add_definitions(ctx);
    }*/

    /*@Override
    public String visitLeft_hand_identifier(Left_hand_identifierContext ctx) {
        return super.visitLeft_hand_identifier(ctx);
    }*/

    /*@Override
    public String visitLeft_hand_identifiers(Left_hand_identifiersContext ctx) {
        return super.visitLeft_hand_identifiers(ctx);
    }*/

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

    /*@Override
    public String visitObject(ObjectContext ctx) {
        return super.visitObject(ctx);
    }*/

    @Override
    public String visitRemove(RemoveContext ctx) {
        return super.visitRemove(ctx);
    }

    @Override
    public String visitSet(SetContext ctx) {
        final String slash;
        /*if (ctx.SLASH() != null) {
            slash = "/" + ctx.frag.getText();
        } else {
            slash = "";
        }
        final String text = ctx.key.getText();
        final String[] split = text.split("\\.");
        final List<String> splited = Arrays.asList(split);
        final String subId = StringUtils.join(splited.subList(0, split.length-1), '.');
        final String instance;
        if(context.getMapIdentifiers().containsKey(subId)) {
            final Assignable assignable = context.getMapIdentifiers().get(subId);
            if(assignable instanceof FunctionAssignable) {
                instance = ((FunctionAssignable)assignable).getRetValue().toText() + "." + splited.get(split.length-1);
            } else if(assignable instanceof  InstanceAssignable) {
                instance = text;
            } else {
                instance = assignable.toText();
            }
        } else {
            instance = text;

        }
        // resolve instance :
        // TODO : look for instance.key without the last parth (x.a in the case of x.a.b) and resolve it name;


        final Assignable visitAssignable = new AssignableVisitor(context).visit(ctx.val);
        final Assignable resolveAssignable = visitAssignable.resolve(context);
        final String assignableString = resolveAssignable.toText();
        return "set " + instance + slash + " = \"" + assignableString + "\"";
        */
        return "TODO";
    }

    /*@Override
    public String visitShort_identifier(Short_identifierContext ctx) {
        return super.visitShort_identifier(ctx);
    }*/

    /*@Override
    public String visitSpecial_internal_operation(Special_internal_operationContext ctx) {
        return super.visitSpecial_internal_operation(ctx);
    }*/

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

    /*@Override
    public String visitFor_body(For_bodyContext ctx) {
        final StringBuilder sb = new StringBuilder();
        for (ParseTree a : ctx.children) {
            final String visit = this.visit(a);
            sb.append(visit);
            sb.append('\n');
        }
        return sb.toString();
    }*/

    /*@Override
    public String visitLong_identifier_chunk(Long_identifier_chunkContext ctx) {
        return super.visitLong_identifier_chunk(ctx);
    }*/

    /*@Override
    public String visitDereference(DereferenceContext ctx) {
        return super.visitDereference(ctx);
    }*/

    /*@Override
    public String visitFunction_body(Function_bodyContext ctx) {
        final StringBuilder sb = new StringBuilder();
        if(ctx.children != null) {
            for (ParseTree a : ctx.children) {
                final String visit = this.visit(a);
                sb.append(visit);
                sb.append('\n');
            }
        }
        return sb.toString().trim();
    }*/

    /*@Override
    public String visitFunction_call(Function_callContext ctx) {
        return new AssignableVisitor(this.context).visit(ctx).toText();
    }*/
}
