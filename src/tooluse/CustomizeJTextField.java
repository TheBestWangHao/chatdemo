package tooluse;

import javax.swing.*;
import java.awt.*;

public class CustomizeJTextField {
    JTextField jTf;
    public JTextField getJTextField(int x, int y, int length, int height, Font font){
        jTf=new JTextField();
        jTf.setBounds(x,y,length,height);
        jTf.setFont(font);
        return jTf;
    }
    public JTextField getJTextField(int x, int y, int length, int height){
        jTf=new JTextField();
        jTf.setBounds(x,y,length,height);
        return jTf;
    }
}
