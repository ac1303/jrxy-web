package com.anchen.mysql;


import com.alibaba.druid.pool.DruidDataSourceFactory;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author 安宸
 * @create 2022/2/11 10:54
 */
final public class ConnMySQL {
    private static DataSource ds;
    static {
        //1.加载配置文件
        Properties pro = new Properties();
        InputStream is = ConnMySQL.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            pro.load(is);
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 关闭资源
     * @param stmt
     * @param conn
     */
    public static void close(PreparedStatement stmt, Connection conn){
        if (stmt!=null) {
            try {
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (conn!=null){
            try {
                conn.close();//归还连接
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn){
        if (rs!=null) {
            try {
                rs.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (stmt!=null) {
            try {
                stmt.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (conn!=null){
            try {
                conn.close();//归还连接
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 关闭资源
     * @return
     */
    public static DataSource getDataSource(){
        return ds;
    }
    public static void main(String[] args) throws Exception {
        //1.加载配置文件
        Properties pro = new Properties();
        InputStream is = ConnMySQL.class.getClassLoader().getResourceAsStream("druid.properties");
        pro.load(is);
        //2.获取连接池对象
        DataSource ds = DruidDataSourceFactory.createDataSource(pro);
        //3.获取连接
        Connection conn = ds.getConnection();
//        需要抛弃statement，防止sql注入
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery("select * from jrxy");
        while (resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
    }
}
