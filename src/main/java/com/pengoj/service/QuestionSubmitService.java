package com.pengoj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengoj.model.dto.question.QuestionQueryRequest;
import com.pengoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.pengoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.pengoj.model.entity.Question;
import com.pengoj.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pengoj.model.entity.User;
import com.pengoj.model.vo.QuestionSubmitVO;
import com.pengoj.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 彭丽菊
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-03-05 11:23:42
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {
    /**
     * 问题提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage,User loginUser );
}
