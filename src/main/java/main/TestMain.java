package main;

import generate.CodeGenerate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author lbw
 * 测试类
 */
public class TestMain {
    public static void main(String[] args) {
        CodeGenerate codeGenerate = new CodeGenerate();
        codeGenerate.setAuthorName("xb");
        codeGenerate.setPackageOutPath("cn.xiaobao");
        codeGenerate.setVersion("V1.0");
        codeGenerate.setTableName("t_address");
        codeGenerate.init("t_address");
        String parse = codeGenerate.parse(codeGenerate.getColnames(), codeGenerate.getColTypes(), codeGenerate.getColSizes());
        System.out.println(parse);
        File file = new File("D:/TAddress.java");
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try{
            FileWriter fw = new FileWriter(file,false);
            fw.write(parse);
            fw.flush();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
