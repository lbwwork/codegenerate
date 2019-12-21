package view;

import com.mysql.jdbc.StringUtils;
import generate.CodeGenerate;

import javax.swing.*;
import javax.xml.soap.Text;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author lbw
 * 软件的页面类
 */
public class MainView extends JPanel{
    public static int HEIGHT = 400;
    public static int WIDTH = 700;
    private static CodeGenerate codeGenerate;
    private static String author;
    private static String tableName;
    private static String packagePaht;
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("代码生成");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(WIDTH, HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new FlowLayout());
        init(frame);
        frame.setVisible(true);
    }
    private static void init(JFrame frame){
        codeGenerate = new CodeGenerate();
        JButton generate = new JButton("生成");
        generate.setSize(50,20);
        final TextField authort = new TextField("作者名");
        final TextField tableNamet = new TextField("表名");
        final TextField packagePatht = new TextField("包路径");
        authort.setSize(50,10);
        tableNamet.setSize(50,10);
        packagePatht.setSize(50,10);
        frame.add(authort);
        frame.add(tableNamet);
        frame.add(packagePatht);
        generate.addActionListener(new ActionListener() {
            //点击按钮生成代码
            public void actionPerformed(ActionEvent e) {
                author = authort.getText().trim();
                packagePaht = packagePatht.getText().trim();
                tableName = tableNamet.getText().trim();
                codeGenerate.setAuthorName(author);
                codeGenerate.setPackageOutPath(packagePaht);
                codeGenerate.setVersion("V1.0");
                codeGenerate.setTableName(tableName);
                codeGenerate.init(tableName);
                String parse = codeGenerate.parse(codeGenerate.getColnames(), codeGenerate.getColTypes(), codeGenerate.getColSizes());
                File file = new File("D:/"+tableName+".java");
                if (!file.exists()){
                    try {
                        file.createNewFile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                try{
                    FileWriter fw = new FileWriter(file,false);
                    fw.write(parse);
                    fw.flush();
                    fw.close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        frame.add(generate);


    }
}
