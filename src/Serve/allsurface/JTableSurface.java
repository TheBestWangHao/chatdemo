package Serve.allsurface;

import tooluse.ConnectDB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class JTableSurface {
    JScrollPane jScrollPane;
    JPanel jTablePanel;
    JPopupMenu popupMenu;
    JTable table;
    JComboBox<String> cbx;
    public static DefaultTableModel dm;
    private static int TABLE_WIDTH=1000,TABLE_HEIGHT=635;
    public JTableSurface() {
        jTablePanel=new JPanel();
        jTablePanel.setLayout(null);
        addOtherComponent();
        showUserTable();
        setJcomBox();
    }
    public void addOtherComponent(){
        JButton selectBut,addRowBut,changeViewBut;
        selectBut=getButton(900,15,80,30,"搜索");
        addRowBut=getButton(50,690,100,33,"新增行");
        changeViewBut=getButton(800,690,120,33,"切换视图");
        JTextField selectTF=new JTextField();
        selectTF.setBounds(200,15,550,30);
        jTablePanel.add(selectBut);
        jTablePanel.add(addRowBut);
        jTablePanel.add(changeViewBut);
        jTablePanel.add(selectTF);
    }
    //此部分为显示JTable的方法，其中有一个计时器，定期刷新JTabel
    public void showUserTable() {
        Vector<String> name = new Vector<>();                           //存储DefaultTableModel的列名
        name.add("id");
        name.add("昵称");
        name.add("密码");
        name.add("性别");
        name.add("年龄");
        Vector<Vector>listVector=new Vector<>();                        //存储所有DefaultTableModel的数据
        try {
            Connection connectDB = new ConnectDB().getConnection();     //通过做好的数据库连接工具类连接数据库
            String sql = "select *from user";
            Statement statement = connectDB.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                Vector<Object>data=new Vector<>();                      //存储DefaultTableModel的一行数据
                data.add(rs.getString(1));
                data.add(rs.getString(2));
                data.add(rs.getString(3));
                data.add(rs.getString(4));
                data.add(rs.getString(5));
                data.add(rs.getString(6));
                data.add(rs.getString(7));
                listVector.add(data);
            }
            dm=new DefaultTableModel(listVector,name);
            table=new JTable(dm);
            table.setBounds(0,50,TABLE_WIDTH,TABLE_HEIGHT);
            jScrollPane=new JScrollPane(table);
            jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane.setBounds(0,50,TABLE_WIDTH,TABLE_HEIGHT);
            jScrollPane.setVisible(true);
            jTablePanel.add(jScrollPane);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        Timer timer=new Timer();                                        //任务器，0秒后开始执行，每秒执行一次
        timer.schedule(new TimerTask() {
                public void run(){
                table.updateUI();                                       //刷新JTable
            }
        },0,1000);
    }
    public void setJcomBox() {
        Vector<String> vector=new Vector<>();
        vector.add("id查询");
        vector.add("昵称查询");
        vector.add("年龄查询");
        vector.add("性别查询");
        cbx=new JComboBox<>(vector);
        cbx.setBounds(770,15,120,30);
        jTablePanel.add(cbx);
        cbx.setFont(new Font("楷体",Font.PLAIN,20));
    }
    public void setPopupMenu(){
        MenuItem item_id,item_name,item_sex,item_age;
        item_id=new MenuItem("id查询");
        item_age=new MenuItem("年龄查询");
        item_sex=new MenuItem("性别查询");
        item_name=new MenuItem("昵称查询");
        popupMenu=new JPopupMenu();
    }
    public JPanel getJpanel()
    {
        return jTablePanel;
    }
    private JButton getButton(int x,int y,int width,int height,String text)
    {
        Font font=new Font("楷体",Font.PLAIN,20);
        JButton but=new JButton(text);
        but.setFont(font);
        but.setBounds(x,y,width,height);
        return but;
    }
}
