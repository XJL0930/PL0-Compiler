package org.tjdx;

public class GrammarException extends RuntimeException{
    private String message;
    private int lineNum;
    int columnNum;

    private String buildMessage(){
        return "行号："+lineNum+" 列号：" +columnNum+" 错误信息："+message;
    }
    public GrammarException(int errorType,int lineNum,int columnNum){
        this.lineNum = lineNum;
        this.columnNum = columnNum;
        switch (errorType){
            case 0:
                this.message = "关键字PROGRAM缺失！";
                break;
            case 1:
                this.message = "此处应为标识符！";
                break;
            case 2:
                this.message = "此处应为{CONST VAR,<标识符>,IF,WHILE,BEGIN,#}之一！";
                break;
            case 3:
                this.message = "此处应为CONST！";
                break;
            case 4:
                this.message = "此处应为赋值符:=！";
                break;
            case 5:
                this.message = "此处应为整常数！";
                break;
            case 6:
                this.message = "此处应为{, ;}之一！";
                break;
            case 7:
                this.message = "此处应为{VAR,<标识符>,IF,WHILE,BEGIN,#}之一！";
                break;
            case 8:
                this.message = "此处应为VAR！";
                break;
            case 9:
                this.message = "该标识符已经被声明！";
                break;
            case 10:
                this.message = "此处应为{<标识符>,IF,WHILE,BEGIN,#，END}之一！";
                break;
            case 11:
                this.message = "此处应为已经声明的常量或变量！";
                break;
            case 12:
                this.message = "此处应为{+ - 标识符 整数 (}之一！";
                break;
            case 13:
                this.message = "此处应为右括号！";
                break;
            case 14:
                this.message = "此处应为{标识符 整数 左括号}之一！";
                break;
            case 15:
                this.message = "此处应为{* / + - # ; THEN DO END = <> < <= > >= )}之一！";
                break;
            case 16:
                this.message = "此处应为{* / + - # ; THEN DO END = <> < <= > >= )}之一！";
                break;
            case 17:
                this.message = "此处应为IF！";
                break;
            case 18:
                this.message = "此处应为THEN！";
                break;
            case 19:
                this.message = "此处应为{= <> < <= > >= }之一！";
                break;
            case 20:
                this.message = "此处应为WHILE！";
                break;
            case 21:
                this.message = "此处应为DO！";
                break;
            case 22:
                this.message = "此处应为BEGIN！";
                break;
            case 23:
                this.message = "此处应为{; END}之一！";
                break;

        }
        throw new GrammarException(buildMessage());
    }

    public GrammarException(String message){
        super(message);
    }
}
