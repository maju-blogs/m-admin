package org.m.web.service;


import com.mybatisflex.core.service.IService;
import org.m.common.entity.po.PythonFilePo;

import java.io.Serializable;

/**
 * 脚本文件 服务层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
public interface IPythonFileService extends IService<PythonFilePo> {

    int saveByCode(PythonFilePo pythonFile);

    Object removeByCode(Serializable code);

    boolean updateById(PythonFilePo entity);

    String run(PythonFilePo pythonFile, String clientId);
}