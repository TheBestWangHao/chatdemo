package tooluse;

import java.awt.*;
import java.text.SimpleDateFormat;

/*此类为专用于获得当前系统时间的类，因为在实际代码编写中，发现很多地方需要用到当前时间，如果每次都用大量代码获取，
违反了代码的重用性，因此写出此类，使以最方便的方式获取当前系统时间。且此类中的方法都为静态方法，直接调用即可*/


/*此类功能有：1 按照一般格式获取当前时间
*            2 按照用户自定义的字体获取当前时间
*            3 按照用户的设定返回带有时间的JLabel                 */
public class GetTime {

    public static String getTime() {
        java.util.Date date=new java.util.Date();
        SimpleDateFormat smDate=new SimpleDateFormat("yyyy-MM-dd");
        String time=smDate.format(date);
        time=time+" :  ";
        return time;
    }
    public static String getTimeTag(){
        java.util.Date date=new java.util.Date();
        String timeTag=String.valueOf(date.getTime());
        return timeTag;
    }

}
