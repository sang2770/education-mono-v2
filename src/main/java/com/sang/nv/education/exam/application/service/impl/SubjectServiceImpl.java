package com.sang.nv.education.exam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.nv.education.exam.application.dto.request.BaseCreateOrUpdateRequest;
import com.sang.nv.education.exam.application.mapper.ExamAutoMapper;
import com.sang.nv.education.exam.application.service.SubjectService;
import com.sang.nv.education.exam.domain.Subject;
import com.sang.nv.education.exam.domain.command.BaseCreateOrUpdateCmd;
import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.SubjectEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.SubjectEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {
    private final SubjectEntityRepository subjectEntityRepository;
    private final ExamAutoMapper examAutoMapper;
    private final SubjectEntityMapper subjectEntityMapper;
    private final SeqRepository seqRepository;

    public SubjectServiceImpl(SubjectEntityRepository SubjectEntityRepository,
                              ExamAutoMapper examAutoMapper,
                              SubjectEntityMapper SubjectEntityMapper, SeqRepository seqRepository) {
        this.subjectEntityRepository = SubjectEntityRepository;
        this.examAutoMapper = examAutoMapper;
        this.subjectEntityMapper = SubjectEntityMapper;
        this.seqRepository = seqRepository;
    }

    @Override
    public Subject create(BaseCreateOrUpdateRequest request) {
        BaseCreateOrUpdateCmd cmd = this.examAutoMapper.from(request);
        cmd.setCode(this.seqRepository.generateSubjectCode());
        Subject Subject = new Subject(cmd);
        this.subjectEntityRepository.save(this.subjectEntityMapper.toEntity(Subject));
        return Subject;
    }

    @Override
    public Subject update(String id, BaseCreateOrUpdateRequest request) {
        BaseCreateOrUpdateCmd cmd = this.examAutoMapper.from(request);
        Optional<SubjectEntity> optionalSubject = this.subjectEntityRepository.findById(id);
        if (optionalSubject.isEmpty()) {
            throw new ResponseException(NotFoundError.SUBJECT_NOT_EXISTED);
        }
        Subject Subject = this.subjectEntityMapper.toDomain(optionalSubject.get());
        Subject.update(cmd);
        this.subjectEntityRepository.save(this.subjectEntityMapper.toEntity(Subject));
        return Subject;
    }

    @Override
    public PageDTO<Subject> search(BaseSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        Page<SubjectEntity> SubjectEntityPage = this.subjectEntityRepository.search(request.getKeyword(), pageable);
        List<Subject> Subject = SubjectEntityPage.getContent().stream().map(Subject::new).collect(Collectors.toList());
        return PageDTO.of(Subject, request.getPageIndex(), request.getPageSize(), SubjectEntityPage.getTotalElements());
    }

    @Override
    public Subject getById(String id) {
        return this.subjectEntityMapper.toDomain(this.subjectEntityRepository.findById(id).orElseThrow(() ->
                new ResponseException(NotFoundError.SUBJECT_NOT_EXISTED)));
    }

    @Override
    public void delete(String id) {
        Subject Subject = this.getById(id);
        Subject.deleted();
        this.subjectEntityRepository.save(this.subjectEntityMapper.toEntity(Subject));
    }

}
