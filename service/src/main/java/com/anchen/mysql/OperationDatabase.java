package com.anchen.mysql;

import com.anchen.model.Jrxy;

import java.sql.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author 安宸
 * @create 2022/2/11 12:29
 */
public class OperationDatabase {
//    查询是否存在
    public static boolean isExist(String columnValue) {
        Connection conn = null;
        PreparedStatement pr = null;
        ResultSet resultSet = null;
        try {
            conn = ConnMySQL.getConnection();
            String sql = "select * from jrxy where studentID = ?";
            pr = conn.prepareStatement(sql);
            pr.setString(1, columnValue);
            resultSet = pr.executeQuery();
            if (resultSet.next()) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }finally {
            ConnMySQL.close(resultSet, pr, conn);
        }
    }
//    插入数据
    public static boolean insert(Jrxy student) {
        Connection conn = null;
        PreparedStatement pr = null;
        int resultSet = 0;
        try{
            conn = ConnMySQL.getConnection();
            String sql= "INSERT INTO jrxy VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pr = conn.prepareStatement(sql);
            pr.setLong(1, student.getStudentId());
            pr.setString(2, student.getPsw());
            pr.setString(3, student.getAddressProvince());
            pr.setString(4, student.getAddressCity());
            pr.setString(5, student.getAddressRegion());
            pr.setString(6, student.getAddress());
            pr.setDouble(7, student.getLon());
            pr.setDouble(8, student.getLat());
            pr.setString(9, student.getPushChannel());
            pr.setString(10, student.getServerChanKey());
            pr.setString(11, student.getPushDeerKey());
            pr.setLong(12, student.getErrorCount());
            resultSet = pr.executeUpdate();
            if (resultSet>0) {
                return true;
            }else {
                return false;
            }
        }catch (SQLException e) {
            return false;
        }finally {
            ConnMySQL.close(pr, conn);
        }
    }
}
