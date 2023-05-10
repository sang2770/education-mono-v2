package com.sang.nv.education.iam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonmodel.query.BaseSearchQuery;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.nv.education.iam.application.dto.request.DepartmentCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.mapper.IamAutoMapper;
import com.sang.nv.education.iam.application.service.DepartmentService;
import com.sang.nv.education.iam.domain.Department;
import com.sang.nv.education.iam.domain.command.DepartmentCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.repository.DepartmentDomainRepository;
import com.sang.nv.education.iam.infrastructure.persistence.entity.DepartmentEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.DepartmentEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.DepartmentEntityRepository;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentEntityRepository departmentEntityRepository;
    private final IamAutoMapper autoMapper;
    private final DepartmentDomainRepository departmentDomainRepository;
    private final DepartmentEntityMapper departmentEntityMapper;
    private final SeqRepository seqRepository;

    public DepartmentServiceImpl(DepartmentEntityRepository departmentEntityRepository,
                                 IamAutoMapper autoMapper,
                                 DepartmentDomainRepository departmentDomainRepository,
                                 DepartmentEntityMapper departmentEntityMapper, SeqRepository seqRepository) {
        this.departmentEntityRepository = departmentEntityRepository;
        this.autoMapper = autoMapper;
        this.departmentDomainRepository = departmentDomainRepository;
        this.departmentEntityMapper = departmentEntityMapper;
        this.seqRepository = seqRepository;
    }

    @Override
    public Department create(DepartmentCreateOrUpdateRequest request) {
        DepartmentCreateOrUpdateCmd cmd = this.autoMapper.from(request);
        cmd.setCode(this.seqRepository.generateDepartmentCode());
        Department department = new Department(cmd);
        this.departmentDomainRepository.save(department);
        return department;
    }

    @Override
    public Department update(String id, DepartmentCreateOrUpdateRequest request) {
        DepartmentCreateOrUpdateCmd cmd = this.autoMapper.from(request);
        Optional<Department> optionalDepartment = this.departmentDomainRepository.findById(id);
        if (optionalDepartment.isEmpty()) {
            throw new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED);
        }
        Department department = optionalDepartment.get();
        department.update(cmd);
        this.departmentDomainRepository.save(department);
        return department;
    }

    @Override
    public PageDTO<Department> search(BaseSearchRequest request) {
        BaseSearchQuery query = this.autoMapper.from(request);
        Long count = this.departmentEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<Department> basePrices = this.departmentEntityMapper.toDomain(this.departmentEntityRepository.search(query));
        return PageDTO.of(basePrices, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public Department getById(String id) {
        return this.departmentDomainRepository.findById(id).orElseThrow(() -> new ResponseException(BadRequestError.DEPARTMENT_NOT_EXISTED));
    }

    @Override
    public PageDTO<Department> autoComplete(BaseSearchRequest request) {
        Pageable pageable = PageableMapperUtil.toPageable(request);
        Page<DepartmentEntity> departmentEntityPage = this.departmentEntityRepository.autoComplete(request.getKeyword(), pageable);
        if (departmentEntityPage.getTotalElements() == 0) {
            return PageDTO.empty();
        }
        List<Department> departments = this.departmentEntityMapper.toDomain(departmentEntityPage.getContent());
        return new PageDTO<>(departments, departmentEntityPage.getNumber(), departmentEntityPage.getSize(), departmentEntityPage.getTotalElements());
    }

}
