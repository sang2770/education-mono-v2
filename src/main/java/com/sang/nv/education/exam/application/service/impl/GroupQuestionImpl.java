package com.sang.nv.education.exam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.commonpersistence.support.SqlUtils;
import com.sang.nv.education.exam.application.dto.request.question.GroupQuestionCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapper;
import com.sang.nv.education.exam.application.service.GroupQuestionService;
import com.sang.nv.education.exam.domain.GroupQuestion;
import com.sang.nv.education.exam.domain.command.GroupQuestionCreateOrUpdateCmd;
import com.sang.nv.education.exam.infrastructure.persistence.entity.GroupQuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.GroupQuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.GroupQuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.SubjectEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupQuestionImpl implements GroupQuestionService {
    private final GroupQuestionEntityRepository groupQuestionEntityRepository;
    private final ExamAutoMapper examAutoMapper;
    private final GroupQuestionEntityMapper groupQuestionEntityMapper;
    private final SubjectEntityRepository subjectEntityRepository;
    private final SeqRepository seqRepository;

    public GroupQuestionImpl(GroupQuestionEntityRepository GroupQuestionEntityRepository,
                             ExamAutoMapper examAutoMapper,
                             GroupQuestionEntityMapper GroupQuestionEntityMapper, SubjectEntityRepository subjectEntityRepository, SeqRepository seqRepository) {
        this.groupQuestionEntityRepository = GroupQuestionEntityRepository;
        this.examAutoMapper = examAutoMapper;
        this.groupQuestionEntityMapper = GroupQuestionEntityMapper;
        this.subjectEntityRepository = subjectEntityRepository;
        this.seqRepository = seqRepository;
    }

    @Override
    public GroupQuestion create(GroupQuestionCreateOrUpdateRequest request) {
        GroupQuestionCreateOrUpdateCmd cmd = this.examAutoMapper.from(request);
        Optional<SubjectEntity> subject = this.subjectEntityRepository.findById(cmd.getSubjectId());
        if (subject.isEmpty()) {
            throw new ResponseException(NotFoundError.SUBJECT_NOT_EXISTED);
        }
        cmd.setCode(this.seqRepository.generateGroupQuestionCode());
        GroupQuestion GroupQuestion = new GroupQuestion(cmd);
        this.groupQuestionEntityRepository.save(this.groupQuestionEntityMapper.toEntity(GroupQuestion));
        return GroupQuestion;
    }

    @Override
    public GroupQuestion update(String id, GroupQuestionCreateOrUpdateRequest request) {
        GroupQuestionCreateOrUpdateCmd cmd = this.examAutoMapper.from(request);
        Optional<GroupQuestionEntity> optionalGroupQuestion = this.groupQuestionEntityRepository.findById(id);
        if (optionalGroupQuestion.isEmpty()) {
            throw new ResponseException(NotFoundError.GROUP_NOT_EXISTED);
        }
        GroupQuestion GroupQuestion = this.groupQuestionEntityMapper.toDomain(optionalGroupQuestion.get());
        GroupQuestion.update(cmd);
        this.groupQuestionEntityRepository.save(this.groupQuestionEntityMapper.toEntity(GroupQuestion));
        return GroupQuestion;
    }

    @Override
    public PageDTO<GroupQuestion> search(BaseSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        Page<GroupQuestionEntity> GroupQuestionEntityPage = this.groupQuestionEntityRepository.search(SqlUtils.encodeKeyword(request.getKeyword()), request.getSubjectIds(), pageable);
        List<GroupQuestion> GroupQuestion = GroupQuestionEntityPage.getContent().stream().map(GroupQuestion::new).collect(Collectors.toList());
        return PageDTO.of(GroupQuestion, request.getPageIndex(), request.getPageSize(), GroupQuestionEntityPage.getTotalElements());
    }

    @Override
    public GroupQuestion getById(String id) {
        return this.groupQuestionEntityMapper.toDomain(this.groupQuestionEntityRepository.findById(id).orElseThrow(() ->
                new ResponseException(NotFoundError.GROUP_NOT_EXISTED)));
    }

    @Override
    public void delete(String id) {
        GroupQuestion groupQuestion = this.getById(id);
        groupQuestion.deleted();
        this.groupQuestionEntityRepository.save(this.groupQuestionEntityMapper.toEntity(groupQuestion));
    }

}
