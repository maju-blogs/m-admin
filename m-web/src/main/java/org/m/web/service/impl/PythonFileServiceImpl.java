package org.m.web.service.impl;


import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryCondition;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.StringUtil;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.m.common.entity.po.ConfigPo;
import org.m.common.entity.po.PythonFilePo;
import org.m.web.mapper.ConfigMapper;
import org.m.web.mapper.PythonFileMapper;
import org.m.web.service.IPythonFileService;
import org.m.common.inter.ISseEmitterService;
import org.m.web.util.ShellUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.max;
import static org.m.common.entity.po.table.ConfigPoTableDef.CONFIG_PO;
import static org.m.common.entity.po.table.PythonFilePoTableDef.PYTHON_FILE_PO;

/**
 * 脚本文件 服务层实现。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Service
@Slf4j
public class PythonFileServiceImpl extends ServiceImpl<PythonFileMapper, PythonFilePo> implements IPythonFileService {
    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ISseEmitterService sseEmitterService;

    @Override
    public int saveByCode(PythonFilePo pythonFile) {
        String maxCode = mapper.selectObjectByQueryAs(QueryWrapper.create().select(max(PYTHON_FILE_PO.CODE)).
                from(PYTHON_FILE_PO).where(PYTHON_FILE_PO.LEVEL.eq(pythonFile.getLevel()).and(PYTHON_FILE_PO.PARENT.eq(pythonFile.getParent()))), String.class);
        String parentMaxCode = super.mapper.selectObjectByQueryAs(QueryWrapper.create().select(max(PYTHON_FILE_PO.CODE)).
                from(PYTHON_FILE_PO).where(PYTHON_FILE_PO.ID.eq(pythonFile.getParent())), String.class);
        if (StrUtil.isNotBlank(maxCode)) {
            int code = Integer.parseInt(maxCode) + 1;
            pythonFile.setCode(StrUtil.fillBefore(code + "", '0', pythonFile.getLevel() * 3));
        } else {
            if (StrUtil.isNotBlank(parentMaxCode)) {
                pythonFile.setCode(parentMaxCode + "001");
            } else {
                pythonFile.setCode("001");
            }
        }
        pythonFile.setStatus(1);
        return mapper.insert(pythonFile);
    }

    @Override
    public Object removeByCode(Serializable code) {
        return mapper.deleteByQuery(QueryWrapper.create().from(PYTHON_FILE_PO).where(PYTHON_FILE_PO.CODE.likeLeft(code)));
    }

    @Override
    public boolean updateById(PythonFilePo entity) {
        ConfigPo configPo = configMapper.selectOneByCondition(QueryCondition.createEmpty());
        String pythonPath = configPo.getPythonPath() + "/";
        String code = entity.getCode();
        String[] split = StrUtil.split(code, 3);
        List<PythonFilePo> pythonFilePos = mapper.selectListByQuery(QueryWrapper.create().from(PYTHON_FILE_PO).where(PYTHON_FILE_PO.CODE.likeLeft(split[0])).orderBy(PYTHON_FILE_PO.LEVEL, true));
        for (PythonFilePo pythonFilePo : pythonFilePos) {
            if (pythonFilePo.getType() == 2) {
                pythonPath = pythonPath + pythonFilePo.getFileName() + File.separator;
            }
            if (pythonFilePo.getType() == 1 && pythonFilePo.getCode().equals(code)) {
                pythonPath = pythonPath + pythonFilePo.getFileName() + File.separator;
            }
        }
        pythonPath = pythonPath.substring(0, pythonPath.length() - 1);
        if (StringUtil.isNotBlank(entity.getPythonCode())) {
            FileUtil.writeString(entity.getPythonCode(), pythonPath, StandardCharsets.UTF_8);
        }
        mapper.update(entity);
        return true;
    }

    @Override
    public String run(PythonFilePo entity, String clientId) {
        ConfigPo configPo = configMapper.selectOneByCondition(QueryCondition.createEmpty());
        String pythonPath = configPo.getPythonPath() + "/";
        String code = entity.getCode();
        String[] split = StrUtil.split(code, 3);
        List<PythonFilePo> pythonFilePos = mapper.selectListByQuery(QueryWrapper.create().from(PYTHON_FILE_PO).where(PYTHON_FILE_PO.CODE.likeLeft(split[0])).orderBy(PYTHON_FILE_PO.LEVEL, true));
        for (PythonFilePo pythonFilePo : pythonFilePos) {
            if (pythonFilePo.getType() == 2) {
                pythonPath = pythonPath + pythonFilePo.getFileName() + File.separator;
            }
            if (pythonFilePo.getType() == 1 && pythonFilePo.getCode().equals(code)) {
                pythonPath = pythonPath + pythonFilePo.getFileName() + File.separator;
            }
        }
        pythonPath = pythonPath.substring(0, pythonPath.length() - 1);
        if (StringUtil.isNotBlank(entity.getPythonCode())) {
            FileUtil.writeString(entity.getPythonCode(), pythonPath, StandardCharsets.UTF_8);
        }
        mapper.update(entity);
        log.info("开始调用:{}", pythonPath);
        String[] params = {ShellUtil.getExePath(configPo.getPythonBin(), pythonPath), pythonPath};
        ProcessBuilder processBuilder = new ProcessBuilder(params);
        Process process = null;
        try {
            process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sseEmitterService.sendMessageToOneClient(clientId, line);
                output.append(line).append("\n");
            }
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.forName("UTF-8")));
            while ((line = reader.readLine()) != null) {
                sseEmitterService.sendMessageToOneClient(clientId, line);
                output.append(line).append("\n");
            }
            process.waitFor();
            log.info("调用成功:{}", output);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}