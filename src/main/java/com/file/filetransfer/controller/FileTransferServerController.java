package com.file.filetransfer.controller;

import com.file.filetransfer.item.base.HttpResult;
import com.file.filetransfer.item.ex.MyException;
import com.file.filetransfer.model.DownloadForm;
import com.file.filetransfer.model.FileBean;
import com.file.filetransfer.model.UploadForm;
import com.file.filetransfer.service.FileTranserFerServerServiceImpl;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@CrossOrigin
public class FileTransferServerController {

    @Autowired
    private FileTranserFerServerServiceImpl service;

    @Value("${config.uploadAddr}")
    private String uploadAddr;

    @Value("${config.host}")
    private String host;

    @Value("${spring.profiles.active}")
    private String env;

    @RequestMapping("/")
    public ModelAndView demo(ModelAndView view) {
        List<String> list = new ArrayList<String>();
        list.add("java入门到放弃");
        list.add("mysql删库到跑路");
        view.addObject("list", list);
        view.addObject("name", "bbbb");
        view.setViewName("demo/1");
        System.out.println("111=77===哈哈======= yyy");
        return view;
    }

    @RequestMapping("/index")
    public ModelAndView index(ModelAndView view) {
        view.addObject("env", env);
        view.setViewName("demo/index");
        return view;
    }

    private String getNowDate() {
        Calendar cl = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(cl.getTime());
    }

    private String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private String getFileName(String uuid) {
        return getNowDate() + "_" + uuid;
    }

    private String getExtName(String str) {
        int index = str.lastIndexOf(".");
        return str.substring(index);
    }

    @RequestMapping("/upload")
    @ResponseBody
    public HttpResult upload(@RequestParam("file") MultipartFile file, UploadForm form, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("utf-8");
        if (file.isEmpty()) {
            return HttpResult.error("空的上传文件");
        }
        String itemCode = form.getItemCode();
        if (itemCode == null || itemCode.isEmpty()) {
            return HttpResult.error("请指定参数itemCode");
        }
        String relativePath = File.separator + itemCode + File.separator + getNowDate();
        String factRelativePath = File.separator + "upload" + File.separator + "repertory" + relativePath;
        String tempRelativePath = File.separator + "upload" + File.separator + "tmp" + relativePath;
        String tmpPath = uploadAddr + tempRelativePath;
        String orgName = file.getOriginalFilename();
        String extName = getExtName(orgName);
        String uuid = getUuid();
        String fileName = getFileName(uuid);
        String targetName = fileName + extName;
        File targetFile = new File(tmpPath, targetName);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();//创建目录
        }
        FileOutputStream fos = new FileOutputStream(targetFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(file.getBytes());
        bos.flush();
        bos.close();
        FileBean bean = new FileBean();
        bean.setFile_name(targetName);
        bean.setTmp_linux_address(tmpPath);
        bean.setFact_linux_address(uploadAddr + factRelativePath);
        bean.setCode(uuid);
        //插入上传的数据
        int i = service.insertFile(bean);
        if (i != 1) {
            return HttpResult.error("文件insert时异常，上传失败");
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("tmp", host + tempRelativePath + File.separator + targetName);
        map.put("fact", host + factRelativePath + File.separator + targetName);
        map.put("code", uuid);
        return HttpResult.success("上传成功", map);
    }

    @ResponseBody
    @RequestMapping("/moveFileToRep/{code}")
    public HttpResult moveFile(@PathVariable("code") String code) {
        try {
            if (code == null || code.isEmpty()) {
                throw new MyException("参数code不能为空");
            }
            FileBean bean = service.getFileInfoByCode(code);
            File orgFile = new File(bean.getTmp_linux_address());
            File targetFile = new File(bean.getFact_linux_address());
            if (!targetFile.exists()) {
                targetFile.getParentFile().mkdirs();//创建目录
            }
            orgFile.renameTo(targetFile);
            int i = service.updateTmpLinuxAddressByCode(code);
            if (i != 1) {
                HttpResult.error("数据库操作失败");
            }
        } catch (MyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return HttpResult.success("文件转移成功");
    }

    @RequestMapping("/ajax")
    @ResponseBody
    public String ajax() throws Exception {
        return "success。";
    }


    private void downloadFile(FileBean bean,HttpServletResponse res) {
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            res.setHeader("content-type", "application/octet-stream");
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment;filename=" + bean.getFile_name());
            byte[] buff = new byte[1024];

            os = res.getOutputStream();
            File targetFile = new File(bean.getFact_linux_address()+File.separator+bean.getFile_name());
            FileInputStream fis = new FileInputStream(targetFile);
            bis = new BufferedInputStream(fis);
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (MyException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/dw/{code}")
    public void dw(HttpServletResponse res, @PathVariable("code") String code) {

        try {
            FileBean bean = service.getFileInfoByCode(code);
            downloadFile(bean,res);
        } catch (MyException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/ftpToDownloadFile")
    public void ftpToDownloadFile(DownloadForm form, HttpServletResponse res) {
        String IPADDR = "10.2.194.235";
        int PORT = 22;
        String NAME = "root";
        String PWSD = "123456";
        try {
            // 获取SFTP连接
            JSch jsch = new JSch();
            Session sshSession = jsch.getSession(NAME, IPADDR, PORT);
            sshSession.setPassword(PWSD);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();

            Channel channel = sshSession.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;
            System.out.println("已连接============");
            // 读取远程文件并复制
            String path = "/opt/doc/test.docx";
            InputStream inputStream = sftp.get(path);
            String uuid = getUuid();
            String fileName=getFileName(uuid) + getExtName(path);
            String tmpPath=uploadAddr + File.separator + "tmp" + File.separator;
            File file = new File( tmpPath+fileName);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }

            outputStream.flush();
            inputStream.close();
            outputStream.close();
            System.out.println("已下载远程文件到当前服务器=============");
            System.out.println("开始浏览器下载");
            FileBean bean=new FileBean();
            bean.setFile_name(fileName);
            bean.setFact_linux_address(tmpPath);
            //下载到浏览器
            downloadFile(bean,res);
            System.out.println("浏览器已下载文件，删除服务器上的临时文件");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @RequestMapping("/list")
    public HttpResult getFileList() throws Exception {
        return HttpResult.success("请求成功", service.getFileInfoByCode("code123"));
    }
}
