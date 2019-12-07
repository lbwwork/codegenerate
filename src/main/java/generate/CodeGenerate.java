package generate;

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

}
