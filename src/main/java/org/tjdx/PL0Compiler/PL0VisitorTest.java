package org.tjdx.PL0Compiler;

public class PL0VisitorTest extends PL0Compiler.PL0BaseVisitor<Void> {
    @Override
    public Void visitProgram(PL0Compiler.PL0Parser.ProgramContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Program");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitProgramHeader(PL0Compiler.PL0Parser.ProgramHeaderContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Program Header");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSubProgram(PL0Compiler.PL0Parser.SubProgramContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("SubProgram");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstantDeclaration(PL0Compiler.PL0Parser.ConstantDeclarationContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Constant Declaration");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitConstantDefinition(PL0Compiler.PL0Parser.ConstantDefinitionContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Constant Definition");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitVariableDeclaration(PL0Compiler.PL0Parser.VariableDeclarationContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Variable Declaration");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIdentifier(PL0Compiler.PL0Parser.IdentifierContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Identifier" + " : " + ctx.getText());
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUnsignedInt(PL0Compiler.PL0Parser.UnsignedIntContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("UnsignedInt" + " : " + ctx.getText());
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitStatement(PL0Compiler.PL0Parser.StatementContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Statement");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAssignmentStatement(PL0Compiler.PL0Parser.AssignmentStatementContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Assignment Statement");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitIfStatement(PL0Compiler.PL0Parser.IfStatementContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("If Statement");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitWhileStatement(PL0Compiler.PL0Parser.WhileStatementContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("While Statement");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCompoundStatement(PL0Compiler.PL0Parser.CompoundStatementContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Compound Statement");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitEmptyStatement(PL0Compiler.PL0Parser.EmptyStatementContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Empty Statement");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpression(PL0Compiler.PL0Parser.ExpressionContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Expression");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTerm(PL0Compiler.PL0Parser.TermContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Term");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFactor(PL0Compiler.PL0Parser.FactorContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Factor");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCondition(PL0Compiler.PL0Parser.ConditionContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Condition");
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAdditionOperator(PL0Compiler.PL0Parser.AdditionOperatorContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Addition Operator" + " : " + ctx.getText());
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitMultiplicationOperator(PL0Compiler.PL0Parser.MultiplicationOperatorContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Multiplication Operator" + " : " + ctx.getText());
        visitChildren(ctx);
        return null;
    }

    @Override
    public Void visitRelationOperator(PL0Compiler.PL0Parser.RelationOperatorContext ctx) {
        System.out.print("-".repeat(ctx.depth()*2-2));
        System.out.println("Relation Operator" + " : " + ctx.getText());
        visitChildren(ctx);
        return null;
    }
}
