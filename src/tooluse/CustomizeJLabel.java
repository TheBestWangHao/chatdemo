package tooluse;

import javax.swing.*;
import java.awt.*;

public class CustomizeJLabel {
    JLabel jLabel;
    Font font;
    public CustomizeJLabel(){
        font=new Font("楷体",Font.PLAIN,25);
    }
    public JLabel getJLabel(int x,int y,int length,int height,String text){
        jLabel=new JLabel(text);
        jLabel.setFont(font);
        jLabel.setBounds(x,y,length,height);
        return jLabel;
    }
    public JLabel getJLabel(int x,int y,int length,int height,String text,Font font) {
        jLabel=new JLabel(text);
        jLabel.setFont(font);
        jLabel.setBounds(x,y,length,height);
        return jLabel;
    }
}
