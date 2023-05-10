package com.sang.nv.education.iam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassSearchRequest;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassesCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.mapper.IamAutoMapper;
import com.sang.nv.education.iam.application.mapper.IamAutoMapperQuery;
import com.sang.nv.education.iam.application.service.ClassesService;
import com.sang.nv.education.iam.domain.Classes;
import com.sang.nv.education.iam.domain.command.ClassesCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.query.ClassSearchQuery;
import com.sang.nv.education.iam.domain.repository.ClassesDomainRepository;
import com.sang.nv.education.iam.domain.repository.KeyDomainRepository;
import com.sang.nv.education.iam.infrastructure.persistence.entity.ClassEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.DepartmentEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.KeyEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.ClassesEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.ClassesEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.DepartmentEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.KeyEntityRepository;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassesServiceImpl implements ClassesService {
    private final ClassesEntityRepository classesEntityRepository;
    private final IamAutoMapper autoMapper;
    private final IamAutoMapperQuery autoMapperQuery;
    private final ClassesDomainRepository classesDomainRepository;
    private final ClassesEntityMapper ClassesEntityMapper;
    private final KeyDomainRepository keyDomainRepository;
    private final KeyEntityRepository keyEntityRepository;
    private final ClassesEntityMapper classesEntityMapper;
    private final DepartmentEntityRepository departmentEntityRepository;
    private final SeqRepository seqRepository;

    public ClassesServiceImpl(ClassesEntityRepository ClassesEntityRepository,
                              IamAutoMapper autoMapper, IamAutoMapperQuery autoMapperQuery, ClassesDomainRepository classesDomainRepository,
                              ClassesEntityMapper ClassesEntityMapper, KeyDomainRepository keyDomainRepository,
                              KeyEntityRepository keyEntityRepository, ClassesEntityMapper classesEntityMapper,
                              DepartmentEntityRepository departmentEntityRepository, SeqRepository seqRepository) {
        this.classesEntityRepository = ClassesEntityRepository;
        this.autoMapper = autoMapper;
        this.autoMapperQuery = autoMapperQuery;
        this.classesDomainRepository = classesDomainRepository;
        this.ClassesEntityMapper = ClassesEntityMapper;
        this.keyDomainRepository = keyDomainRepository;
        this.keyEntityRepository = keyEntityRepository;
        this.classesEntityMapper = classesEntityMapper;
        this.departmentEntityRepository = departmentEntityRepository;
        this.seqRepository = seqRepository;
    }

    @Override
    public Classes create(ClassesCreateOrUpdateRequest request) {
        ClassesCreateOrUpdateCmd cmd = this.autoMapper.from(request);
        cmd.setCode(this.seqRepository.generateClassesCode());
        Optional<KeyEntity> optionalKeyEntity = this.keyEntityRepository.findById(request.getKeyId());
        if (optionalKeyEntity.isEmpty()) {
            throw new ResponseException(BadRequestError.KEYS_NOT_EXISTED);
        }
        Optional<DepartmentEntity> optionalDepartmentEntity = this.departmentEntityRepository.findById(request.getDepartmentId());
        if (optionalDepartmentEntity.isEmpty()) {
            throw new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED);
        }
        Classes classes = new Classes(cmd);
        this.classesDomainRepository.save(classes);
        return classes;
    }

    @Override
    public Classes update(String id, ClassesCreateOrUpdateRequest request) {
        ClassesCreateOrUpdateCmd cmd = this.autoMapper.from(request);
        Optional<Classes> optionalClasses = this.classesDomainRepository.findById(id);
        if (optionalClasses.isEmpty()) {
            throw new ResponseException(BadRequestError.CLASSES_NOT_EXISTED);
        }
        Classes Classes = optionalClasses.get();
        Classes.update(cmd);
        this.classesDomainRepository.save(Classes);
        return Classes;
    }

    @Override
    public PageDTO<Classes> search(ClassSearchRequest request) {
        ClassSearchQuery query = this.autoMapperQuery.from(request);
        Long count = this.classesEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<Classes> basePrices = this.ClassesEntityMapper.toDomain(this.classesEntityRepository.search(query));
        return PageDTO.of(basePrices, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public Classes getById(String id) {
        return this.classesDomainRepository.findById(id).orElseThrow(() -> new ResponseException(BadRequestError.CLASSES_NOT_EXISTED));
    }

    @Override
    public List<Classes> findByDepartmentId(String departmentId) {
        return this.ClassesEntityMapper.toDomain(this.classesEntityRepository.findByDepartmentId(departmentId));
    }

    @Override
    public PageDTO<Classes> autoComplete(BaseSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        Page<ClassEntity> classEntityPage = this.classesEntityRepository.autoComplete(request.getKeyword(), pageable);
        if (classEntityPage.getTotalElements() == 0) {
            return PageDTO.empty();
        }
        List<Classes> classes = this.classesEntityMapper.toDomain(classEntityPage.getContent());
        return new PageDTO<>(classes, classEntityPage.getNumber(), classEntityPage.getSize(), classEntityPage.getTotalElements());
    }

}
