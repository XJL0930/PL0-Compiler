package org.tjdx;

public class Token {
    private TokenType tokenType;
    private int lineNum;
    private int columnNum; //行号和列号，供错误处理定位用
    private String tokenValue;

    public Token(TokenType tokenType, int lineNum, int columnNum, String tokenValue) {
        this.tokenType = tokenType;
        this.lineNum = lineNum;
        this.columnNum = columnNum;
        this.tokenValue = tokenValue;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", lineNum=" + lineNum +
                ", columnNum=" + columnNum +
                ", tokenValue='" + tokenValue + '\'' +
                '}';
    }
}
