package com.sang.nv.education.iam.domain.repository;


import com.sang.nv.education.common.web.support.DomainRepository;
import com.sang.nv.education.iam.domain.Role;

import java.util.List;

public interface RoleDomainRepository extends DomainRepository<Role, String> {
    List<Role> enrichList(List<Role> roles);
}
