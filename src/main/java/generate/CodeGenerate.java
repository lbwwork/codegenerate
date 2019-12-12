package generate;

import util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;

/**
 * @author lbw
 */
public class CodeGenerate {
    /**
     * 指定实体生成所在的包的路径
     */
    private String packageOutPath = "";
    /**
     * 作者名字
     */
    private String authorName = "";
    /**
     * 表名
     */
    private String tableName = "";
    /**
     *  列名数组
     */
    private String[] colnames;
    /**
     * 列类型数组
     */
    private String[] colTypes;
    /**
     * 列大小数组
     */
    private int[] colSizes;
    /**
     * 版本
     */
    private String version = "V1.0";
    /**
     * 默认路径
     */
    private String defaultPath = "/src/main/java/";

    public void init(String tableName){
        try {
            Connection conn = JdbcUtil.getConn();
            String sql = " select * from " + tableName;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSetMetaData metaData = ps.getMetaData();
            int size = metaData.getColumnCount();
            colnames = new String[size];
            colTypes = new String[size];
            colSizes = new int[size];
            for (int i = 0;i < size;i++){
                colnames[i] = metaData.getColumnName(i + 1);
                colTypes[i] = metaData.getColumnTypeName(i + 1);
                colSizes[i] = metaData.getColumnDisplaySize(i +1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String parse(String[] colnames,String[] colTypes,int[] colSizes){
        StringBuffer str = new StringBuffer();
        //生成包路径
        str.append("package " + packageOutPath + ";\r\n");
        str.append("import java.util.Date;\r\n");
        str.append("import java.sql.*;\r\n");
        str.append("import java.lang.*;\r\n");
        str.append("\r\n");
        //生成注释
        str.append("/**\r\n");
        str.append(" * @author : " + authorName + "\r\n");
        str.append(" * @version : 1.0" + "\r\n");
        str.append(" */\r\n");
        //实体部分 生成变量
        parseAttrs(str);
        //实体部分 生成get/set方法
        parseMethod(str);






        return str.toString();
    }
    private void parseAttrs(StringBuffer str){
        for (int i = 0;i < colnames.length;i++){
            String attrName = generateAttrName(colnames[i]);
            str.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + attrName + ";\r\n");
        }
    }
    private void parseMethod(StringBuffer str){
        for (int i = 0;i < colnames.length;i++){
            String attrName = generateAttrName(colnames[i]);
            //set
            str.append("\tpublic void set" + generateMethodAttrName(attrName) + "(" + sqlType2JavaType(colTypes[i]) + attrName + ") { \r\n");
            str.append("\t  this." + attrName + " = " + attrName + ";\r\n");
            str.append("\t}\r\n");
            //get
            str.append("\tpublic "+sqlType2JavaType(colTypes[i])+ " get"+attrName+"(){\r\n");
            str.append("\t  return "+attrName + ";\r\n");
            str.append("\t}\r\n");
        }
        //toString
        str.append("\tpublic void toString(){ \r\n");
        str.append("\t  System.out.println(");
        for (int i = 0; i < colnames.length; i++) {
            String attrName = generateAttrName(colnames[i]);
            str.append("\""+attrName+"\"="+attrName);
        }
        str.append("\")\r\n");
        str.append("\t}");
    }
    private String generateMethodAttrName(String dbName){
        String name = dbName.toLowerCase();
        String[] split = name.split("_");
        StringBuffer attrName = new StringBuffer();
        for (String s : split) {
            String first = s.substring(0,1);
            s = first.toUpperCase() + s.substring(1);
            attrName.append(s);
        }
        return attrName.toString();
    }
    private String generateAttrName(String dbName){
        String name = dbName.toLowerCase();
        String[] split = name.split("_");
        StringBuffer attrName = new StringBuffer();
        int count = 0;
        for (String s : split) {
            if (count > 0){
                String first = s.substring(0,1);
                s = first.toUpperCase() + s.substring(1);
            }
            count++;
            attrName.append(s);
        }
        return attrName.toString();
    }

    private String sqlType2JavaType(String sqlType) {

        if (sqlType.equalsIgnoreCase("bit")) {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("int") || sqlType.equalsIgnoreCase("int identity")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("image") || sqlType.equalsIgnoreCase("varbinary(max)")
                || sqlType.equalsIgnoreCase("varbinary") || sqlType.equalsIgnoreCase("udt")
                || sqlType.equalsIgnoreCase("timestamp") || sqlType.equalsIgnoreCase("binary")) {
            return "Byte[]";
        } else if (sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("nvarchar(max)")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nvarchar(ntext)")
                || sqlType.equalsIgnoreCase("uniqueidentifier") || sqlType.equalsIgnoreCase("xml")
                || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("varchar(max)")
                || sqlType.equalsIgnoreCase("text") || sqlType.equalsIgnoreCase("varchar")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("real")) {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("smallint") || sqlType.equalsIgnoreCase("tinyint")) {
            return "Short";
        } else if (sqlType.equalsIgnoreCase("date") || sqlType.equalsIgnoreCase("datetime")
                || sqlType.equalsIgnoreCase("time") || sqlType.equalsIgnoreCase("datetime2")) {
            return "Date";
        } else {
            System.out.println("数据类型异常，类型为：" + sqlType);
        }

        return null;
    }

    public String getPackageOutPath() {
        return packageOutPath;
    }

    public void setPackageOutPath(String packageOutPath) {
        this.packageOutPath = packageOutPath;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String[] getColnames() {
        return colnames;
    }

    public String[] getColTypes() {
        return colTypes;
    }

    public int[] getColSizes() {
        return colSizes;
    }
}
