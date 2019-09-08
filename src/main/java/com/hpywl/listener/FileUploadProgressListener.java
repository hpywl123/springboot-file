package com.hpywl.listener;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;
import com.hpywl.entity.Progress;

/**
 * 上传文件监听器
 * 我们要获得上传文件的实时详细信息，必须继承org.apache.commons.fileupload.ProgressListener类，获得信息的时候将进度条对象Progress放在该监听器的session对象中。
 */
@Component
public class FileUploadProgressListener implements ProgressListener {
    private HttpSession session;
    public void setSession(HttpSession session){
        this.session=session;
        Progress status = new Progress();//保存上传状态
        session.setAttribute("status", status);
    }

    @Override
    public void update(long bytesRead, long contentLength, int items) {
        Progress status = (Progress) session.getAttribute("status");
        status.setBytesRead(bytesRead);//已读取数据长度
        status.setContentLength(contentLength);//文件总长度
        status.setItems(items);//正在保存第几个文件

    }


}
