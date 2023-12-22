// Generated from E:/program/PL0-Compiler/src/main/java/org/tjdx/PL0.g4 by ANTLR 4.13.1
package PL0Compiler;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PL0Parser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PL0Visitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PL0Parser#program}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgram(PL0Parser.ProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#programHeader}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProgramHeader(PL0Parser.ProgramHeaderContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#subProgram}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSubProgram(PL0Parser.SubProgramContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#constantDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDeclaration(PL0Parser.ConstantDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#constantDefinition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConstantDefinition(PL0Parser.ConstantDefinitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(PL0Parser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(PL0Parser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#unsignedInt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnsignedInt(PL0Parser.UnsignedIntContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStatement(PL0Parser.StatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#assignmentStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignmentStatement(PL0Parser.AssignmentStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#ifStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfStatement(PL0Parser.IfStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#whileStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhileStatement(PL0Parser.WhileStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#compoundStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompoundStatement(PL0Parser.CompoundStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#emptyStatement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptyStatement(PL0Parser.EmptyStatementContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(PL0Parser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(PL0Parser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactor(PL0Parser.FactorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondition(PL0Parser.ConditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#additionOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdditionOperator(PL0Parser.AdditionOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#multiplicationOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiplicationOperator(PL0Parser.MultiplicationOperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PL0Parser#relationOperator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationOperator(PL0Parser.RelationOperatorContext ctx);
}