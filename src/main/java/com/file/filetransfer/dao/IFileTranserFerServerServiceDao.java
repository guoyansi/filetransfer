package com.file.filetransfer.dao;

import com.file.filetransfer.model.FileBean;

import java.util.List;

public interface IFileTranserFerServerServiceDao {
    //List<FileBean> getTestList() throws  Exception;

    /**
     * 根据code获取file info
     *
     * @param code
     * @return
     * @throws Exception
     */
    FileBean getFileInfoByCode(String code) throws Exception;

    /**
     * 插入数据
     *
     * @param bean
     * @return
     * @throws Exception
     */
    int insertFile(FileBean bean) throws Exception;

    /**
     * 清空临时数据
     *
     * @return
     * @throws Exception
     */
    int deleteTmpData() throws Exception;

    /**
     * 根据code将临时数据转成正式数据
     * @param code
     * @return
     * @throws Exception
     */
    int updateTmpLinuxAddressByCode(String code) throws Exception;
}
