package com.pengoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pengoj.annotation.AuthCheck;
import com.pengoj.common.BaseResponse;
import com.pengoj.common.ErrorCode;
import com.pengoj.common.ResultUtils;
import com.pengoj.constant.UserConstant;
import com.pengoj.exception.BusinessException;
import com.pengoj.model.dto.question.QuestionQueryRequest;
import com.pengoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.pengoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.pengoj.model.entity.Question;
import com.pengoj.model.entity.QuestionSubmit;
import com.pengoj.model.entity.User;
import com.pengoj.model.vo.QuestionSubmitVO;
import com.pengoj.service.QuestionSubmitService;
import com.pengoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 提交记录的ID
     */
    @PostMapping("/")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long result = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(result);
    }


    /**
     * 分页获取列表（仅管理员，普通用户只能看到非答案，提交代码等公开信息）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        // 从数据库中查询原始的题目提交分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser = userService.getLoginUser(request);
        // 返回托米奈信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage, loginUser));
    }

}
