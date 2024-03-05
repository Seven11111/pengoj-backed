package com.pengoj.model.dto.question;

import lombok.Data;

/**
 * @author Mr.Peng
 * @version 1.0
 * @description: TODO
 * @date 2024/3/5 11:57
 */
@Data
public class JudgeConfig {
    /**
     * @description: 时间限制 （ms）
     * @author 彭
     * @date 2024/3/5 11:58
     * @version 1.0
     */
    private Long timeLimit;
    /**
     * @description: 内存限制 （KB）
     * @author 彭
     * @date 2024/3/5 11:58
     * @version 1.0
     */
    private Long memoryLimit;

    /**
     * @description: 栈空间限制 （KB）
     * @author 彭
     * @date 2024/3/5 11:58
     * @version 1.0
     */
    private Long stackLimit;
}
