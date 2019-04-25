package Serve.service;

public class HttpAgreement {
    public static final String  LOGIN_REQUEST="##*$@@#login";                           //登录请求暗号
    public static final String  CAN_LOGIN_RESPONSE="##*$@@#can_login";                  //允许登录暗号
    public static final String  CANNOT_LOGIN_RESPONSE="##*$@@#cannot_login";            //拒绝登录暗号
    public static final String  USER_INFO_REQUEST="##*$@@#user_info";                   //获取当前账号信息暗号
    public static final String  FRIEND_LIST_REQUEST="##*$@@#friend_list";               //获取好友列表请求
    public static final String  GROUP_LIST_REQUEST="##*$@@#group_list";                 //获取群聊列表请求
    public static final String  LOGOUT_MESSAGE="##*$@@#logout";                         //登出暗号
    public static final String  SEND_TEXT_BEGIN="##*$@@#text_begin";                    //文字传输暗号
    public static final String  SEND_PICTURE_BEGIN="##*$@@#picture_begin";              //图片传输暗号
    public static final String  SEARCH_USER_REQUEST="##*$@@#search_user";               //搜索用户请求暗号
    public static final String  NOT_FOUND_USER_RESPONSE="##*$@@#not_found_user";        //未查询到指定用户暗号
    public static final String  SEARCH_GROUP_REQUEST="##*$@@#search_group_request";     //查询群聊暗号
    public static final String  NOT_FOUND_GROUP_RESPONSE="##*$@@#not_found_group";      //未查询到指定群聊暗号
    public static final String  OVER_MESSAGE="##*$@@#over";                             //内容结束暗号
    public static final String  SEND_CHAT_TEXT_BEGIN="##*$@@#chat_text_begin";          //聊天文字传输暗号
    public static final String  SEND_CHAT_PICTURE_BEGIN="##*$@@#chat_picture_begin";    //聊天文字传输暗号
    public static final String  REGIST_SUCCESS="##*$@@#regist_success";                 //聊天图片传输暗号
    public static final String  CLOSED_SERVICE="##*$@@#close_service";                  //服务器已关闭，拒绝访问暗号
    public static final String  CONNECT_CLOSE="##*$@@#connect_close";                   //服务器与客户端连接断开暗号
    public static final String  DELETE_FRIEND_SUCCESS="##*$@@#delete_friend_success";   //删除好友成功暗号
    public static final String  DELETE_FRIEND_FAIL="##*$@@#delete_friend_fail";         //删除好友失败暗号
    public static final String  DELETE_GROUP_FAIL="##*$@@#delete_group_fail";           //删除好友失败暗号
    public static final String  DELETE_GROUP_SUCCESS="##*$@@#delete_group_success";     //删除好友失败暗号
    public static final String  CREATE_GROUP_REQUEST="##*$@@#create_group_request";     //创建群聊请求
    public static final String  CREATE_GROUP_SUCCESS="##*$@@#create_group_success";     //创建群聊成功暗号
    public static final String  CREATE_GROUP_FAIL="##*$@@#create_group_fail";           //创建群聊失败暗号
    public static final String  REGISTE_REQUEST="##*$@@#registe_request";               //未查询到指定群聊暗号
    public static final String  USER_ONLINE="##*$@@#user_online";                       //用户在线暗号
    public static final String  USER_NOTONLINE="##*$@@#user_notonline";                 //用户不在线暗号
    public static final String  ISONLINE_REQUEST="##*$@@#isonline";                     //用户在线暗号
    public static final String  DELETE_FRIEDN_REQUEST="##*$@@#delete_friend_request";   //删除好友请求
    public static final String  DELETE_GROUP_REQUEST="##*$@@#delete_group_request";     //删除群聊请求
    public static final String  SEARCH_USER_NAME_REQUEST="##*$@@#search_user_name";     //查询用户名请求
}
