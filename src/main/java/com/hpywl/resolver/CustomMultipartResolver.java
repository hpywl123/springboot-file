package com.hpywl.resolver;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.hpywl.listener.FileUploadProgressListener;

/**
 * SpringMVC默认有一个文件解析器CommonsMultipartResolver用来解析上传的文件，
 * 我们需要重写该类，自己重写的监听器放到org.apache.commons.fileupload.FileUpload中，还需要将session放到自定义的监听器中。
 */
@Configuration
public class CustomMultipartResolver extends CommonsMultipartResolver {

    // 注入第二步写的FileUploadProgressListener
    @Autowired     // 此处一定要检查是否注入成功了，不成功的话会报错
    private FileUploadProgressListener progressListener;

    public void setFileUploadProgressListener(FileUploadProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        progressListener.setSession(request.getSession());
        fileUpload.setProgressListener(progressListener);
        try {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadBase.SizeLimitExceededException ex) {
            throw new MaxUploadSizeExceededException(fileUpload.getSizeMax(), ex);
        } catch (FileUploadException ex) {
            throw new MultipartException("Could not parse multipart servlet request", ex);
        }
    }

}
