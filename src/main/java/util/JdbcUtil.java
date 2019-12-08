package util;

import org.apache.commons.dbcp.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class JdbcUtil {
    private static BasicDataSource ds;
    static {
        //创建数据源对象
        ds = new BasicDataSource();
        //读取配置文件
        Properties pro = new Properties();
        InputStream ips = JdbcUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            pro.load(ips);
            String driver = pro.getProperty("driver");
            String url = pro.getProperty("url");
            String username = pro.getProperty("username");
            String password = pro.getProperty("password");
            //设置连接
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            //初始连接数量
            ds.setInitialSize(3);
            //设置最大连接数量
            ds.setMaxActive(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConn() throws Exception{
        //获取连接对象
        Connection conn = ds.getConnection();
        return conn;
    }
}
