package com.pengoj.model.dto.questionsubmit;

import lombok.Data;

/**
 * @author Mr.Peng
 * @version 1.0
 * @description: 判题信息
 * @date 2024/3/5 11:57
 */
@Data
public class JudgeInfo {
    /**
     * @description: 程序执行信息
     * @author 彭
     * @date 2024/3/5 11:58
     * @version 1.0
     */
    private String message;
    /**
     * @description: 消耗内存 （KB）
     * @author 彭
     * @date 2024/3/5 11:58
     * @version 1.0
     */
    private Long memory;

    /**
     * @description: 执行时间 （KB）
     * @author 彭
     * @date 2024/3/5 11:58
     * @version 1.0
     */
    private Long time;
}
