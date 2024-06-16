package org.m.common.entity.po;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 脚本文件 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Data
@Builder
@Schema(name = "脚本文件")
@Table(value = "py_python_file")
public class PythonFilePo {

    /**
     * 主键
     */
    @Schema(description = "主键")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    /**
     * 编码
     */
    @Schema(description = "编码")
    @Column(value = "code")
    private String code;

    /**
     * 层级
     */
    @Schema(description = "层级")
    @Column(value = "level")
    private Integer level;

    /**
     * 父级id
     */
    @Schema(description = "父级id")
    @Column(value = "parent")
    private Integer parent;

    /**
     * 中文名称
     */
    @Schema(description = "中文名称")
    @Column(value = "name")
    private String name;

    /**
     * 文件名称
     */
    @Schema(description = "文件名称")
    @Column(value = "file_name")
    private String fileName;

    /**
     * 脚本代码
     */
    @Schema(description = "脚本代码")
    @Column(value = "python_code")
    private String pythonCode;

    @Schema(description = "")
    @Column(value = "status")
    private Integer status;

    /**
     * 1文件 2文件夹
     */
    @Schema(description = "1文件 2文件夹")
    @Column(value = "type")
    private Integer type;


}
