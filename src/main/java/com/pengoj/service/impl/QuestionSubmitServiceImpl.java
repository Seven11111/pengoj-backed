package com.pengoj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pengoj.common.ErrorCode;
import com.pengoj.constant.CommonConstant;
import com.pengoj.exception.BusinessException;
import com.pengoj.mapper.QuestionSubmitMapper;
import com.pengoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.pengoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.pengoj.model.entity.Question;
import com.pengoj.model.entity.QuestionSubmit;
import com.pengoj.model.entity.User;
import com.pengoj.model.enums.QuestionSubmitLanguageEnum;
import com.pengoj.model.enums.QuestionSubmitStatusEnum;
import com.pengoj.model.enums.UserRoleEnum;
import com.pengoj.model.vo.QuestionSubmitVO;
import com.pengoj.service.QuestionService;
import com.pengoj.service.QuestionSubmitService;
import com.pengoj.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 彭丽菊
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-03-05 11:23:42
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;


    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //检验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();

        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已提交题目
        long userId = loginUser.getId();

        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);

        questionSubmit.setLanguage(language);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");


        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }

        return questionSubmit.getId();
    }

    /**
     * 获取查询包装类（用户根据哪些字段进行查询，根据前端传来的请求对象，得到 mybatis 框架支持的查询QueryWrapper类）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();

        Long userId = questionSubmitQueryRequest.getUserId();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(status), "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);

        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        // 脱敏：（仅管理员，普通用户只能看到非答案，提交代码等公开信息）
        long questionId = questionSubmit.getId();

        long userId = loginUser.getId();
        // 处理脱敏
        String userRole = loginUser.getUserRole();
        if (userId != questionSubmit.getUserId() && !UserRoleEnum.getEnumByValue(userRole).equals(UserRoleEnum.ADMIN)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }


        // 填充信息
        List<QuestionSubmitVO> questionVOList = questionSubmitList.stream().map(questionSubmit -> {
            return getQuestionSubmitVO(questionSubmit, loginUser);
        }).collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionVOList);

        return questionSubmitVOPage;
    }

}




