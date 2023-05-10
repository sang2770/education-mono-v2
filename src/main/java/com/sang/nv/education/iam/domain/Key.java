package com.sang.nv.education.iam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.command.KeyCreateOrUpdateCmd;
import com.sang.nv.education.iam.infrastructure.persistence.entity.KeyEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Key extends AuditableDomain {
    String id;
    String name;
    Boolean deleted;

    public Key(KeyCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.deleted = Boolean.FALSE;
    }

    public Key(KeyEntity keyEntity) {
        this.id = keyEntity.getId();
        this.name = keyEntity.getName();
        this.deleted = keyEntity.getDeleted();
    }

    public void update(KeyCreateOrUpdateCmd cmd) {
        this.name = cmd.getName();
        this.deleted = Boolean.FALSE;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
