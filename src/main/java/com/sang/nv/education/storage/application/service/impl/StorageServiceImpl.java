package com.sang.nv.education.storage.application.service.impl;


import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.commonmodel.query.BaseSearchQuery;
import com.sang.nv.education.iam.application.config.TemplateProperties;
import com.sang.nv.education.storage.application.dto.response.FileFirebaseResponse;
import com.sang.nv.education.storage.application.mapper.FileEntityMapper;
import com.sang.nv.education.storage.application.mapper.StorageAutoMapper;
import com.sang.nv.education.storage.application.service.FileBaseService;
import com.sang.nv.education.storage.application.service.StorageService;
import com.sang.nv.education.storage.domain.FileDomain;
import com.sang.nv.education.storage.domain.command.FileCmd;
import com.sang.nv.education.storage.domain.repository.FileDomainRepository;
import com.sang.nv.education.storage.infrastructure.persistence.repository.FileEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {
    private final TemplateProperties templateProperties;

    private final FileBaseService fileBaseService;

    private final FileEntityRepository fileEntityRepository;
    private final FileDomainRepository fileDomainRepository;

    private final FileEntityMapper fileEntityMapper;
    private final StorageAutoMapper autoMapper;

    @Value("${firebase.fcm-account-service-path}")
    private String path;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    public StorageServiceImpl(
            TemplateProperties templateProperties,
            FileBaseService fileBaseService,
            FileEntityRepository fileEntityRepository,
            FileDomainRepository fileDomainRepository,
            FileEntityMapper fileEntityMapper,
            StorageAutoMapper autoMapper) {
        this.templateProperties = templateProperties;
        this.fileBaseService = fileBaseService;
        this.fileEntityRepository = fileEntityRepository;
        this.fileDomainRepository = fileDomainRepository;
        this.fileEntityMapper = fileEntityMapper;
        this.autoMapper = autoMapper;
    }

    private void download(HttpServletResponse response, FileDomain fileDomain) throws IOException {
        response.addHeader("Content-Disposition", "attachment; filename=" + fileDomain.getFileName());
        response.addHeader("X-Action-Mesage", fileDomain.getFileName());
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");


        ClassPathResource serviceAccount = new ClassPathResource(path);
        Credentials credentials = GoogleCredentials.fromStream(serviceAccount.getInputStream());
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of(bucketName, fileDomain.getFileName()));
//        Path path = Paths.get(fileDomain.getFilePath());
//        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        OutputStream outputStream = response.getOutputStream();
        blob.downloadTo(outputStream);
//        outputStream.write(resource.getByteArray());
        outputStream.close();
        response.flushBuffer();
    }

    @Override
    public FileDomain firebaseUpload(MultipartFile file) throws IOException {
        FileFirebaseResponse fileFirebaseResponse = fileBaseService.save(file);
        FileDomain fileDomain = this.convertResponseToFileDomain(fileFirebaseResponse);
        this.fileDomainRepository.save(fileDomain);
        return fileDomain;
    }

    @Override
    public PageDTO<FileDomain> search(BaseSearchRequest request) {
        BaseSearchQuery query = this.autoMapper.from(request);
        Long count = this.fileEntityRepository.count(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<FileDomain> fileDomainList = this.fileEntityMapper.toDomain(this.fileEntityRepository.search(query));
        return PageDTO.of(fileDomainList, request.getPageIndex(), request.getPageSize(), fileDomainList.size());
    }

    @Override
    public FileDomain getById(String id) {
        return this.fileDomainRepository.getById(id);
    }

    @Override
    public List<FileDomain> uploadMultipleFile(List<MultipartFile> fileList) {
        List<FileFirebaseResponse> fileFirebaseResponses = this.fileBaseService.saveAll(fileList);
        List<FileDomain> fileDomainList = fileFirebaseResponses.stream().map(this::convertResponseToFileDomain).collect(Collectors.toList());
        this.fileDomainRepository.saveAll(fileDomainList);
        return fileDomainList;
    }

    @Override
    public List<FileDomain> getByIds(List<String> ids) {
        return this.fileEntityMapper.toDomain(this.fileEntityRepository.findByIds(ids));
    }

    private FileDomain convertResponseToFileDomain(FileFirebaseResponse fileFirebaseResponse) {
        FileCmd cmd = FileCmd.builder()
                .fileName(fileFirebaseResponse.getFileName())
                .filePath(fileFirebaseResponse.getFileViewUrl())
                .originFileName(fileFirebaseResponse.getOriginFileName())
                .build();
        FileDomain fileDomain = new FileDomain(cmd);
        return fileDomain;
    }
}
