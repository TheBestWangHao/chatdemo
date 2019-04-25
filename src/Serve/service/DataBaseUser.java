package Serve.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import tooluse.ConnectDB;
import tooluse.GetTime;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *类全称：数据库使用者，此类封装了本项目中对数据库的所有操作，根据相应的方法调用即可进行相应的数据库操作
 */
public class DataBaseUser {
    Statement statement;
    Connection connection;
    JTextArea errorMessageArea;
    public DataBaseUser(){

    }
    public DataBaseUser(Statement statement){
        this.statement=statement;
    }

    /**
     * 验证登录账号密码是否正确
     * @param id            传入的账号
     * @param password      传入的密码
     * @return              true为正确，false为错误
     */
    public boolean loginIsRight(String id,String password){
        ResultSet resultSet=null;
        String sql="select *from user where id=? and password=?";
        try {
            PreparedStatement pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, id);
            pstatement.setString(2, password);
            resultSet = pstatement.executeQuery();
            if (resultSet.next()) {
                resultSet.close();
                return true;
            }

            else{
                resultSet.close();
                return false;
            }
        } catch(SQLException e){
            errorMessageArea.append(GetTime.getTime()+"登陆检测出现异常！内容为："+e.getMessage());
        }

        return true;
    }

    /**
     * 获取目标用户的 好友列表的所有好友id以及名称
     * @param id    用户的id
     * @return      一个Map集合，键为id，值为名字
     */
    public Map<String,String> getFriends(String id){
        ResultSet resultSet=null;
        Map<String,String> result=new HashMap<>();
        String sql="select friend_id,friend_name from friend where id='"+id+"'";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
            }
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.put(resultSet.getString(1),resultSet.getString(2));
            }
            resultSet.close();
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"获取好友列表出现异常！内容为："+e.getMessage());
        }
        return result;
    }

    /**
     * 根据传入的用户id进行查询，意即查询此用户所加入的所有群聊
     * @param user_id 用户id
     * @return        返回一个包含该用户所有群聊名称的LIST集合
     */
    public List<String> getGroupChat(String user_id){
        List<String> groupName=new LinkedList<>();
        ResultSet resultSet;
        String sql="select groupname from groupchat";
        try {
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                groupName.add(resultSet.getString(1));
                resultSet.close();
            }
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"搜索用户出现异常！内容为："+e.getMessage());
        }
        return groupName;
    }

    /**
     * 判断一用户是否在线的方法
     * @param id    判断的目标
     * @return      true为在线，false为不在线
     */
    public boolean isOnline(String id){
        ResultSet resultSet;
        String sql="select isonline from user";
        try {
            resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                resultSet.close();
                return true;
            }
            resultSet.close();
            return false;
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"判断在线状况出现异常！内容为："+e.getMessage());
        }
        return false;
    }
    public boolean register(String id,String password,String name,String sex,String age){
        String sql;
        try {
            sql="select *from user where id='"+id+"'";
            ResultSet resultSet=statement.executeQuery(sql);
            if(!resultSet.next()){
                return false;
            }
            sql="INSERT INTO user(id,password,name,sex,age) VALUES(?,?,?,?,?)";
            PreparedStatement pstatement = connection.prepareStatement(sql);
            pstatement.setString(1, id);
            pstatement.setString(2, password);
            pstatement.setString(3, name);
            pstatement.setString(2, sex);
            pstatement.setString(2, age);
            pstatement.executeQuery();
            resultSet.close();
            return true;
        } catch(SQLException e){
            errorMessageArea.append(GetTime.getTime()+"注册出现异常！内容为："+e.getMessage());
            System.out.println("数据库操纵者的注册功能出现异常！");
        }
        return false;
    }
    public Boolean deleteFriend(String id){
        String sql;
        try{
            sql="DELETE * FROM user WHERE id='"+id+"'";
            statement.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"删除好友出现异常！内容为："+e.getMessage());
            return false;
        }
    }
    public List<String> searchUser(String id){
        ResultSet resultSet=null;
        List<String> result=new LinkedList<>();
        String sql="select id,name,sex,age from user where id='"+id+"'";
        try {
            resultSet = statement.executeQuery(sql);
            for (int i=1;i<5;i++){
                result.add(resultSet.getString(i));
            }
            resultSet.close();
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"搜索好友出现异常！内容为："+e.getMessage());
        }
        return result;
    }
    public String searchGroup(String groupname){
        String master_id=null;
        String sql="select master_id from bigrange where groupname='"+groupname+"'";
        try {
             ResultSet resultSet = statement.executeQuery(sql);
            master_id=resultSet.getString(1);
            resultSet.close();
            return master_id;
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"搜索好友出现异常！内容为："+e.getMessage());
        }
        return master_id;
    }
    public Boolean deleteGroup(String id,String groupName){
        String sql;
        try{
            sql="DELETE * FROM group WHERE user_id='"+id+"' and groupname='"+groupName+"'";
            statement.executeUpdate(sql);
            return true;
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"删除群聊出现异常！内容为："+e.getMessage());
            return false;
        }
    }
    public String getUser_IP(String user_id){
        String user_ip=null;
        String sql="select ip from user where id='"+user_id+"'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            user_ip=resultSet.getString(1);
            resultSet.close();
            System.out.println("查到IP了");
            return user_ip;
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"搜索好友出现异常！内容为："+e.getMessage());
        }
        return user_ip;
    }
    public String searchUser_name(String user_id){
        String user_name=null;
        String sql="select name from user where id='"+user_id+"'";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            user_name=resultSet.getString(1);
            resultSet.close();
            System.out.println("数据库已查询到用户名");
            return user_name;
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"搜索好友出现异常！内容为："+e.getMessage());
        }
        return user_name;
    }
    /**
     * 当用户上线或者离离线时调用此方法，将数据库中的该用户的online字段设置为yes或者no,同时刷新IP。
     * @param ip            当用户上线时，应为一个String 字符串，表示其IP地址，离线时可为空
     * @param isOnline      当用户上线时为true，离线时为false
     */
    public  void setOnlineState(String user_id,String ip,boolean isOnline){
        String sql;
        if(isOnline)
            sql="update user set ip='"+ip+"',isonline=yes where id='"+user_id+"'";
        else
            sql="update user set ip=null,isonline=no where id='"+user_id+"'";
        try {
            statement.executeUpdate(sql);
        }catch (SQLException e){
            errorMessageArea.append(GetTime.getTime()+"更新用户在线状态！内容为："+e.getMessage());
        }
    }
    public Boolean createGroup(String groupName,String master_id){
        String sql;
        try {
            sql="select *from bigrange where groupname=?";
            PreparedStatement pstatement = connection.prepareStatement(sql);
            pstatement.setString(1,groupName);
            ResultSet resultSet=pstatement.executeQuery();
            if(!resultSet.next()){
                return false;
            }
            sql="INSERT INTO bigrange(groupname,master_id) VALUES(?,?)";
            pstatement = connection.prepareStatement(sql);
            pstatement.setString(1,groupName);
            pstatement.setString(2,master_id);
            pstatement.executeQuery();
            return true;
        } catch(SQLException e){
            errorMessageArea.append(GetTime.getTime()+"注册出现异常！内容为："+e.getMessage());
            System.out.println("数据库操纵者的注册功能出现异常！");
        }
        return false;
    }
}

