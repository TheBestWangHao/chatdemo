package tooluse;

import javax.swing.*;
import java.awt.*;

public class CustomizeJButton {
    Font font;
    public CustomizeJButton(){
        font=new Font("楷体",Font.PLAIN,15);
    }
    public JButton getButton(int x, int y, int width, int height, String text) {
        JButton but=new JButton(text);
        but.setFont(font);
        but.setBounds(x,y,width,height);
        return but;
    }
}
