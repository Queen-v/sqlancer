package lama.mysql.ast;

import lama.mysql.ast.MySQLBinaryComparisonOperation.BinaryComparisonOperator;
import lama.mysql.ast.MySQLBinaryLogicalOperation.MySQLBinaryLogicalOperator;

public class MySQLBetweenOperation extends MySQLExpression {

	private final MySQLExpression expr;
	private final MySQLExpression left;
	private final MySQLExpression right;


	public MySQLBetweenOperation(MySQLExpression expr, MySQLExpression left, MySQLExpression right) {
		this.expr = expr;
		this.left = left;
		this.right = right;
	}
	
	public MySQLExpression getExpr() {
		return expr;
	}
	
	public MySQLExpression getLeft() {
		return left;
	}
	
	public MySQLExpression getRight() {
		return right;
	}
	
	
	@Override
	public MySQLConstant getExpectedValue() {
		MySQLExpression[] arr = new MySQLExpression[] {left, right, expr};
		MySQLConstant convertedExpr = MySQLComputableFunction.castToMostGeneralType(expr.getExpectedValue(), arr);
		MySQLConstant convertedLeft = MySQLComputableFunction.castToMostGeneralType(left.getExpectedValue(), arr);
		MySQLConstant convertedRight = MySQLComputableFunction.castToMostGeneralType(right.getExpectedValue(), arr);
		MySQLBinaryComparisonOperation leftComparison = new MySQLBinaryComparisonOperation(convertedLeft, convertedExpr, BinaryComparisonOperator.LESS_EQUALS);
		MySQLBinaryComparisonOperation rightComparison = new MySQLBinaryComparisonOperation(convertedExpr, convertedRight, BinaryComparisonOperator.LESS_EQUALS);
		MySQLBinaryLogicalOperation andOperation = new MySQLBinaryLogicalOperation(leftComparison, rightComparison, MySQLBinaryLogicalOperator.AND);
		return andOperation.getExpectedValue();
	}

}