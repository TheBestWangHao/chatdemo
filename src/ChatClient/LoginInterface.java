package ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginInterface extends JFrame {
    Container loginPanel;
    public  LoginInterface()
    {
        loginPanel=this.getContentPane();
        loginPanel.setLayout(null);
        this.setBounds(500,300,600,330);
        this.setVisible(true);
        ((JPanel)loginPanel).setOpaque(false);
        JLabel backgroundLabel = new JLabel("");
        ImageIcon background = new ImageIcon("1.jpg");
        backgroundLabel.setBounds(0, 0, background.getIconWidth(),background.getIconHeight());
        backgroundLabel.setIcon(background);
        this.getLayeredPane().add(backgroundLabel, new Integer(Integer.MIN_VALUE));
        addComment();
        this.setContentPane(loginPanel);
        this.setTitle("登录界面");
    }
    public void addComment()
    {
        Font font=new Font("楷体",Font.PLAIN,25);
        Font remindFont=new Font("黑体",Font.PLAIN,17);
        JButton loginBut;
        JLabel accountLabel,passWordLabel,buildLabel,reBuildLabel;
        JTextField accountTF;
        JPasswordField passwordField;
        loginBut=new JButton("登      录");
        loginBut.setBounds(120,200,300,60);
        loginBut.setFont(new Font("微软雅黑",Font.BOLD,25));
        accountLabel=new JLabel("账号：");
        accountLabel.setBounds(30,10,80,70);
        accountLabel.setFont(font);
        passWordLabel=new JLabel("密码：");
        passWordLabel.setFont(font);
        passWordLabel.setBounds(30,110,80,70);
        buildLabel=new JLabel("注册账号");
        buildLabel.setBounds(465,10,160,70);
        buildLabel.setFont(font);
        buildLabel.setVisible(true);
        buildLabel.setForeground(Color.blue);
        reBuildLabel=new JLabel("忘记密码");
        reBuildLabel.setBounds(465,110,160,70);
        reBuildLabel.setForeground(Color.blue);
        reBuildLabel.setFont(font);
        accountTF=new JTextField();
        accountTF.setBounds(100,30,350,35);
        accountTF.setFont(new Font("黑体",Font.PLAIN,20));
        accountTF.enableInputMethods(false);
        passwordField=new JPasswordField();
        passwordField.setBounds(100,130,350,35);
        passwordField.setFont(new Font("黑体",Font.PLAIN,20));
        passwordField.setEchoChar('*');
        loginPanel.add(accountLabel);
        loginPanel.add(accountTF);
        loginPanel.add(buildLabel);
        loginPanel.add(reBuildLabel);
        loginPanel.add(passWordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginBut);
        buildLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.HAND_CURSOR);
                buildLabel.setForeground(Color.red);
            }
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.DEFAULT_CURSOR);
                buildLabel.setForeground(Color.blue);
            }
        });
        reBuildLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                reBuildLabel.setForeground(Color.red);
                setCursor(Cursor.HAND_CURSOR);
            }
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.DEFAULT_CURSOR);
                reBuildLabel.setForeground(Color.blue);
            }
        });
    }
}
