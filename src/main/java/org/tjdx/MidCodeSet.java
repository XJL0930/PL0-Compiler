package org.tjdx;
import java.util.ArrayList;
import java.util.List;
/**
 * create by xjl 2024/1/2
 * 三地址代码集合
 */
public class MidCodeSet {
    private List<MidCode> allcode;
    //当前地址
    int address;
    public void add(String op,String left,String right,String des){address++;allcode.add(new MidCode(op,left,right,des));}
    public void add(MidCode midCode){address++;allcode.add(midCode);}
    public void add(int num,MidCode midCode){address++;allcode.add(num,midCode);}
    public int getAddress(){return address;}
    public void SetAddress(int address){this.address=address;}
    public List<MidCode> getAllcode(){return allcode;}
    public MidCodeSet(){this.address=-1;allcode=new ArrayList<>();}
}
