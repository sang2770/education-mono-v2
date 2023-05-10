package com.sang.nv.education.storage.infrastructure.domainrepository;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.storage.application.mapper.FileEntityMapper;
import com.sang.nv.education.storage.domain.FileDomain;
import com.sang.nv.education.storage.domain.repository.FileDomainRepository;
import com.sang.nv.education.storage.infrastructure.persistence.entity.FileEntity;
import com.sang.nv.education.storage.infrastructure.persistence.repository.FileEntityRepository;
import com.sang.nv.education.storage.infrastructure.support.BadRequestError;
import org.springframework.stereotype.Service;

@Service
public class FileDomainRepositoryImpl extends AbstractDomainRepository<FileDomain, FileEntity, String> implements FileDomainRepository {
    private final FileEntityRepository fileEntityRepository;
    private final FileEntityMapper fileEntityMapper;

    public FileDomainRepositoryImpl(FileEntityRepository fileEntityRepository, FileEntityMapper fileEntityMapper) {
        super(fileEntityRepository, fileEntityMapper);
        this.fileEntityRepository = fileEntityRepository;
        this.fileEntityMapper = fileEntityMapper;
    }

    @Override
    public FileDomain getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(BadRequestError.FILE_NOT_FOUND));
    }
}
