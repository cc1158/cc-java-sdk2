package com.cc.sdk2.jsdk.commons.http.entities;


import com.cc.sdk2.jsdk.commons.http.ContentTypeEnum;
import com.cc.sdk2.jsdk.commons.utils.IoUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

public class FormDataRequestEntity extends BaseRequestEntity<FormDataRequestEntity> {

    private final String BOUNDARY_START_LINE = "--${boundary}";
    private final String BOUNDARY_PREFIX = "WebKitFormBoundaryr";
    private final String BOUNDARY_END_LINE = "--${boundary}--";


    private final String boundary = BOUNDARY_PREFIX + reqUid;

    public void addMultipartFormData(String key, Object value) {
        formParams.put(key, value);
    }

    public void addMultipartFormData(String key, File file) {
        PostFile postFile = new PostFile(file);
        formParams.put(key, postFile);
    }

    @Override
    public String getContentType() {
        return ContentTypeEnum.MULTIPART_FORM.value + boundary;
    }

    @Override
    public byte[] genSendData(String charset) throws UnsupportedEncodingException {
        byte[] ret = new byte[0];
        for (String key : formParams.keySet()) {
            Object value = formParams.get(key);
            StringBuilder boundaryLine = new StringBuilder();
            byte[] src;
            int destP;
            if (value instanceof PostFile) {
                //文件开始
                PostFile postFile = (PostFile) value;
                boundaryLine.append("\r\n")
                        .append(BOUNDARY_START_LINE.replace("${boundary}", boundary))
                        .append("\r\n")
                        .append("Content-Disposition: form-data; name=\"").append(key).append("\"; filename=\"").append(postFile.getFileAbsPath()).append("\"")
                        .append("\r\n")
                        .append("Content-Type: ").append(postFile.getFileContentType()).append("\r\n\r\n")
                ;
                src = boundaryLine.toString().getBytes(Charset.forName(charset));
                destP = ret.length;
                ret = Arrays.copyOf(ret, ret.length + src.length);
                System.arraycopy(src, 0, ret, destP, src.length);
                //文件内容
                try {
                    destP = ret.length;
                    src = IoUtil.read(postFile.getInput());
                    ret = Arrays.copyOf(ret, ret.length + src.length);
                    System.arraycopy(src, 0, ret, destP, src.length);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    postFile.dispose();
                }
            } else {
                //k-v数据
                boundaryLine.append("\r\n")
                        .append(BOUNDARY_START_LINE.replace("${boundary}", boundary))
                        .append("\r\n")
                        .append("Content-Disposition: form-data; name=\"").append(key).append("\";")
                        .append("\r\n\r\n")
                        .append(value)
                ;
                destP = ret.length;
                src = boundaryLine.toString().getBytes(Charset.forName(charset));
                ret = Arrays.copyOf(ret, ret.length + src.length);
                System.arraycopy(src, 0, ret, destP, src.length);
            }
            //数据换行
            destP = ret.length;
            src = "\r\n".getBytes();
            ret = Arrays.copyOf(ret, ret.length + src.length);
            System.arraycopy(src, 0, ret, destP, src.length);
        }
        //end line 边界
        int lastP = ret.length;
        String end = BOUNDARY_END_LINE.replace("${boundary}", boundary) + "\r\n\r\n";
        byte[] b4 = end.getBytes();
        ret = Arrays.copyOf(ret, ret.length + b4.length);
        System.arraycopy(b4, 0, ret, lastP, b4.length);
        return ret;
    }

    /**
     * http传输文件
     */
    static class PostFile {
        /**
         * 文件类型使用
         */
        private String fileContentType = "application/octet-stream";
        private String fileName;
        private String fileAbsPath;
        private String extName;
        private FileInputStream input;
        private long size;

        public PostFile(File file) {
            if (file == null || !file.exists() || file.isDirectory()) {
                throw new RuntimeException("file can not be found");
            }
            fileName = file.getName();
            fileAbsPath = file.getAbsolutePath();
            extName = file.getName().contains(".") ? file.getName().substring(file.getName().indexOf(".")) : "";
            autoSetPropeties(file.getName());
            try {
                input = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
            size = file.length();
        }

        private void autoSetPropeties(String fileName) {
            if (fileName.endsWith(".txt")) {
                this.fileContentType = "text/plain";
            } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                this.fileContentType = "image/jpeg";
            } else if (fileName.endsWith(".gif")) {
                this.fileContentType = "image/gif";
            } else if (fileName.endsWith(".doc")) {
                this.fileContentType = "application/msword";
            } else if (fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                this.fileContentType = "application/vnd.ms-excel";
            } else if (fileName.endsWith(".pdf")) {
                this.fileContentType = "application/pdf";
            } else if (fileName.endsWith(".mp3")) {
                this.fileContentType = "audio/mp3";
            } else if (fileName.endsWith(".mp4")) {
                this.fileContentType = "video/mpeg4";
            }
        }

        /**
         * 释放文件
         */
        protected void dispose() {
            if (this.input != null) {
                try {
                    this.input.close();
                } catch (IOException e) {
                }
            }
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileAbsPath() {
            return fileAbsPath;
        }

        public String getExtName() {
            return extName;
        }

        public FileInputStream getInput() {
            return input;
        }

        public long getSize() {
            return size;
        }

        public String getFileContentType() {
            return fileContentType;
        }
    }
}
