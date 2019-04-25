package Serve.service;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 本类为在 文件判断类，作用为，当有用户成功登陆时，在指定目录下，检索此目录下的receiver_id,看是否有与登陆者id相同的receiver_id
 * 如果有相同的，就将此文件所有信息加入到fileInfoList当中。fileInfoList存储着所有需要发送的文件的文件信息，其存储的是一个长度为
 * 6的字符串型数组，每个数组包含着一个待发送文件的信息
 */
public class FileJudger {
    String judge_id;
    String timeTag;
    String sender_id;
    String receiver_id;
    String fileName;
    String fileType;
    String filePath;
    String fileRootPath=FileReceive.rootPath;
    String splitString=FileReceive.splitString;
    List<String> fileNameList;

    /**
     * 若想使用此类，应当传入待接收者的id进来，用以进行比对，判断哪些文件需要进行发送.
     * @param judge_id
     */
    public FileJudger(String judge_id){
        this.judge_id=judge_id;
    }

    /**
     * 本类主要的逻辑处理方法，其将一个目录下的所有文件传入到fileParse进行解析，并得到所有文件的所有信息，并依次与接收者id进行比
     * 对，当发现文件的receiver_id，与接收者id相等时，将此文件脸颊到fileInfoList集合当中，最后返回此集合。
     * @return
     */
    public List<String[]> getFile(){
        List<String[]> fileInfoList=new LinkedList<>();
        File dir = new File(fileRootPath);
        File[] files = dir.listFiles();
        String[] fileInfo=null;
        if (files != null) {
            for(File file:files){
                fileInfo=fileParse(file);
                if(fileInfo[2].equals(judge_id)){
                    fileInfoList.add(fileInfo);
                }
            }
        }
        return fileInfoList;
    }

    /**
     * 文件名解析方法，内部封装了对文件名进行解析的方法，将文件名解析为六部分，分别依次为：
     * 时间戳 发送者id 接收者id 文件名 文件类型 文件路径
     * 然后以字符串数组形式返回
     * @param file
     * @return
     */
    public String[] fileParse(File file){
        String allInfo=file.getName();
        String[] fileInfo = allInfo.split(splitString);
        timeTag=fileInfo[0];
        sender_id = fileInfo[1];
        receiver_id = fileInfo[2];
        fileName = fileInfo[3];
        String[] types = allInfo.split("\\.");
        fileType = types[types.length - 1];
        String[] judgeFileInfo=null;
        judgeFileInfo[0]=timeTag;
        judgeFileInfo[1]=sender_id;
        judgeFileInfo[2]=receiver_id;
        judgeFileInfo[3]=fileName;
        judgeFileInfo[4]=fileType;
        judgeFileInfo[5]=file.getPath();
        return judgeFileInfo;
    }
}
