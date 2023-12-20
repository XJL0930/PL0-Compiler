package org.tjdx;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LexAnalysis {
    private static String[] keyWords = {"PROGRAM","BEGIN","END","CONST","VAR","WHILE","DO","IF","THEN"};

    private static char currentChar = ' ';    //当前指针指向的字符

    private static char[] fileContent;
    private static int fileLength;
    private static int searchPoint = 0; //指针，指向当次分析的字符
    private static String currentString = "" ;  //当次分析的字符串

    private static LexAnalysis lexAnalysisInstance;

    private LexAnalysis(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            List<Character> chars = new ArrayList<>();
            int r;
            while ((r = reader.read()) != -1) {
                chars.add((char) r);
            }
            // 末尾添加EOF字符
            chars.add('\0');

            fileContent = new char[chars.size()];
            for (int i = 0; i < chars.size(); i++) {
                fileContent[i] = chars.get(i);
            }
            fileLength = chars.size();
            getChar();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static LexAnalysis getLexAnalysisInstance(String filePath){
        if (lexAnalysisInstance == null) {
            lexAnalysisInstance = new LexAnalysis(filePath);
        }
        return lexAnalysisInstance;
    }

    public  Token analysis(){
        currentString = "";

        if(currentChar == ' '){
            while(currentChar==' '){
                getChar();
            }
            return new Token(TokenType.SPACE,findPosition()[0],findPosition()[1],"-" );
        }
        else if (currentChar == '\n'||currentChar =='\r'){
            while(currentChar=='\n'||currentChar =='\r'){
                getChar();
            }
            return new Token(TokenType.NEWLINE,findPosition()[0],findPosition()[1],"-" );
        }
        else if (currentChar == '\t'){
            while(currentChar=='\t'){
                getChar();
            }
            return new Token(TokenType.TABLE,findPosition()[0],findPosition()[1],"-" );
        }
        else if (currentChar == '\0'){
            return new Token(TokenType.EOF,findPosition()[0],findPosition()[1],"-" );
        }
        else if (isLetter()){   //首位是字母，保留字或标识符
            while(isLetter()||isDigit()){
                currentString +=currentChar;
                getChar();
            }
            for(int i = 0;i< keyWords.length;i++){
                if (currentString.equals(keyWords[i])){
                    return new Token(TokenType.valueOf(currentString),findPosition()[0],findPosition()[1],"-");
                }
            }
            return new Token(TokenType.ID,findPosition()[0],findPosition()[1],currentString);
        }
        else if(isDigit()){
            while (isDigit()){
                currentString +=currentChar;
                getChar();
            }
            return new Token(TokenType.INT,findPosition()[0],findPosition()[1],currentString);
        }
        else if (currentChar == '+'){
            getChar();
            return new Token(TokenType.PLUS,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == '-'){
            getChar();
            return new Token(TokenType.SUB,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == '*'){
            getChar();
            return new Token(TokenType.MULTI,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == '/'){
            getChar();
            return new Token(TokenType.DIV,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == ':'){
            getChar();
            if(currentChar == '='){
                getChar();
                return new Token(TokenType.ASSIGN,findPosition()[0],findPosition()[1],"-");
            }
            else throw new LexException("赋值符号不完整！行号为"+findPosition()[0]+" 列号为"+findPosition()[1]);
        }
        else if (currentChar == '='){
            getChar();
            return new Token(TokenType.EQUAL,findPosition()[0],findPosition()[1],"-");
        }
        else if(currentChar=='<'){
            getChar();
            if(currentChar=='>'){
                getChar();
                return new Token(TokenType.UNEQUAL,findPosition()[0],findPosition()[1],"-");
            }
            else if(currentChar=='='){
                getChar();
                return new Token(TokenType.SMEQUAL,findPosition()[0],findPosition()[1],"-");
            }
            else{
                return new Token(TokenType.SMALLER,findPosition()[0],findPosition()[1],"-");
            }
        }
        else if(currentChar=='>'){
            getChar();
            if(currentChar=='='){
                getChar();
                return new Token(TokenType.GREQUAL,findPosition()[0],findPosition()[1],"-");
            }
            else{
                return new Token(TokenType.GREATER,findPosition()[0],findPosition()[1],"-");
            }
        }
        else if (currentChar == '('){
            getChar();
            return new Token(TokenType.LPAR,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == ')'){
            getChar();
            return new Token(TokenType.RPAR,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == ';'){
            getChar();
            return new Token(TokenType.SEMI,findPosition()[0],findPosition()[1],"-");
        }
        else if (currentChar == ','){
            getChar();
            return new Token(TokenType.COMMA,findPosition()[0],findPosition()[1],"-");
        }
        else{
            throw new LexException("不支持的符号"+currentChar+"！行号为"+findPosition()[0]+" 列号为"+findPosition()[1]);
        }
    }

    private  void getChar(){
        if(currentChar!='\0'){
            currentChar = fileContent[searchPoint];
            searchPoint++;
        }
        else{
            throw new RuntimeException("已到文件末尾！");
        }
    }

    private  int[] findPosition(){
        int line = 1;
        int column = 1;
        for (int i = 0; i < searchPoint; i++) {
            if (fileContent[i] == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }

        return new int[]{line, column};
    }

    private  boolean isLetter() {
        if(Character.isLetter(currentChar)) {
            return true;
        }
        return false;
    }

    private  boolean isDigit() {
        if(Character.isDigit(currentChar)) {
            return true;
        }
        return false;
    }
}
