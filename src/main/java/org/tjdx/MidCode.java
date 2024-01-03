package org.tjdx;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * create by xjl 2024/1/2
 * 单句三地址代码类
 */
public class MidCode {
    //运算符
    private String op;
    //左操作数
    private String left;
    //右操作数
    private String right;
    //目标符号
    private String des;

    public MidCode(String op,String left,String right,String des){
        setOp(op);
        setLeft(left);
        setRight(right);
        setDes(des);
    }
    public MidCode(){
        setOp("~");
        setLeft("~");
        setRight("~");
        setDes("~");
    }
}
