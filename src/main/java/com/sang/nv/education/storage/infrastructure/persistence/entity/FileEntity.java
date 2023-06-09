package com.sang.nv.education.storage.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "file", indexes = {
        @Index(name = "file_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class FileEntity extends AuditableEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "file_name")
    private String fileName;
    @Column(name = "originFileName")
    private String originFileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "deleted")
    private Boolean deleted;
}
