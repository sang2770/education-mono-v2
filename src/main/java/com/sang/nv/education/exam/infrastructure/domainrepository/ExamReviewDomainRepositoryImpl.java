package com.sang.nv.education.exam.infrastructure.domainrepository;


import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.exam.domain.ExamReview;
import com.sang.nv.education.exam.domain.ExamReviewFile;
import com.sang.nv.education.exam.domain.repository.ExamReviewDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamReviewEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.ExamReviewFileEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamReviewEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.ExamReviewFileEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamReviewEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.ExamReviewFileEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewStatus;
import com.sang.nv.education.exam.infrastructure.support.exception.NotFoundError;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.storage.application.service.StorageService;
import com.sang.nv.education.storage.domain.FileDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExamReviewDomainRepositoryImpl extends AbstractDomainRepository<ExamReview, ExamReviewEntity, String> implements ExamReviewDomainRepository {

    private final ExamReviewEntityRepository examReviewEntityRepository;
    private final ExamReviewEntityMapper examReviewEntityMapper;
    private final ExamReviewFileEntityMapper examReviewFileEntityMapper;
    private final ExamReviewFileEntityRepository examReviewFileEntityRepository;
    private final UserService userService;
    private final StorageService storageService;

    public ExamReviewDomainRepositoryImpl(ExamReviewEntityRepository examReviewEntityRepository, ExamReviewEntityMapper examReviewEntityMapper, ExamReviewFileEntityMapper examReviewFileEntityMapper, ExamReviewFileEntityRepository examReviewFileEntityRepository, UserService userService, StorageService storageService) {
        super(examReviewEntityRepository, examReviewEntityMapper);
        this.examReviewEntityRepository = examReviewEntityRepository;
        this.examReviewEntityMapper = examReviewEntityMapper;
        this.examReviewFileEntityMapper = examReviewFileEntityMapper;
        this.examReviewFileEntityRepository = examReviewFileEntityRepository;
        this.userService = userService;
        this.storageService = storageService;
    }

    @Override
    public ExamReview getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(NotFoundError.QUESTION_NOT_EXISTED));
    }

    @Override
    public List<ExamReview> getByUserExamId(String id) {
        List<ExamReview> examReviewList = examReviewEntityMapper.toDomain(examReviewEntityRepository.findByUserExamId(id));
        this.enrichList(examReviewList);
        return examReviewList;
    }

    @Override
    public Boolean checkExist(String id) {
        Long count = examReviewEntityRepository.countById(id, ExamReviewStatus.DONE);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<ExamReview> getAllByPeriodRoom(String periodId, String roomId) {
        List<ExamReview> examReviewList = examReviewEntityMapper.toDomain(examReviewEntityRepository.findAllByPeriodRoom(periodId, roomId));
        this.enrichList(examReviewList);
        return examReviewList;
    }

    @Override
    protected List<ExamReview> enrichList(List<ExamReview> examReviewList) {
        // enrich
        List<String> examReviewIds = examReviewList.stream().map(ExamReview::getId).collect(Collectors.toList());
        List<ExamReviewFileEntity> examReviewFileEntities = examReviewFileEntityRepository.findByExamReviewIds(examReviewIds);
        List<ExamReviewFile> examReviewFiles = this.examReviewFileEntityMapper.toDomain(examReviewFileEntities);
        List<String> reviewFileIds = examReviewFiles.stream().map(ExamReviewFile::getFileId).collect(Collectors.toList());
        List<FileDomain> fileDomains = this.storageService.getByIds(reviewFileIds);
        examReviewFiles.forEach(examReviewFile -> {
            Optional<FileDomain> fileDomain = fileDomains.stream().filter(item -> Objects.equals(item.getId(), examReviewFile.getFileId())).findFirst();
            if (fileDomain.isPresent()){
                FileDomain  fileDomain1 = fileDomain.get();
                examReviewFile.enrichFileName(fileDomain1.getFileName());
                examReviewFile.enrichViewUrl(fileDomain1.getFilePath());
            }
        });
        examReviewList.forEach(examReview -> {
            List<ExamReviewFile> examReviewFile = examReviewFiles.stream().filter(item -> item.getExamReviewId().equals(examReview.getId())).collect(Collectors.toList());
            examReview.enrichFile(examReviewFile);
        });

        // enrich User
        List<String> userIds = examReviewList.stream().map(ExamReview::getUserId).collect(Collectors.toList());
        List<User> users = this.userService.findByIds(userIds);
        examReviewList.forEach(examReview -> {
            User user = users.stream().filter(u -> u.getId().equals(examReview.getUserId())).findFirst().orElse(null);
            examReview.enrichUser(user);
        });
        return examReviewList;
    }

    @Override
    public ExamReview save(ExamReview domain) {
        if (!CollectionUtils.isEmpty(domain.getExamReviewFiles()))
        {
            List<ExamReviewFileEntity> examReviewFileEntities = examReviewFileEntityMapper.toEntity(domain.getExamReviewFiles());
            this.examReviewFileEntityRepository.saveAll(examReviewFileEntities);
        }
        return super.save(domain);
    }

    @Override
    protected ExamReview enrich(ExamReview examReview) {
        // enrich answer
        List<ExamReviewFileEntity> examReviewFileEntities = examReviewFileEntityRepository.findByExamReviewId(examReview.getId());
        if (!CollectionUtils.isEmpty(examReviewFileEntities))
        {
            examReview.enrichFile(examReviewFileEntityMapper.toDomain(examReviewFileEntities));
        }
        return super.enrich(examReview);
    }


}

