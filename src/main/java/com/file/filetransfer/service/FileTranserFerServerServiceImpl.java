package com.file.filetransfer.service;

import com.file.filetransfer.dao.IFileTranserFerServerServiceDao;
import com.file.filetransfer.model.FileBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileTranserFerServerServiceImpl {

    @Autowired
    private IFileTranserFerServerServiceDao dao;

    public FileBean getFileInfoByCode(String code)throws  Exception{
        return dao.getFileInfoByCode(code);
    }

    public int insertFile(FileBean file) throws Exception{
        return dao.insertFile(file);
    }
    public int deleteTmpData() throws Exception{
        return dao.deleteTmpData();
    }
    public int updateTmpLinuxAddressByCode(String code) throws Exception{
        return  dao.updateTmpLinuxAddressByCode(code);
    }
}
