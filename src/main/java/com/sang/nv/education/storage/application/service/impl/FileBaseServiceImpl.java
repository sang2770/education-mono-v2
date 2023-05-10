package com.sang.nv.education.storage.application.service.impl;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.storage.application.dto.response.FileFirebaseResponse;
import com.sang.nv.education.storage.application.service.FileBaseService;
import com.sang.nv.education.storage.infrastructure.support.BadRequestError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class FileBaseServiceImpl implements FileBaseService {

    @Value("${firebase.image-url}")
    private String imageUrl;

    @Value("${firebase.bucket-name}")
    private String bucketName;

    @Value("${firebase.fcm-account-service-path}")
    private String path;

    @EventListener
    public void init(ApplicationReadyEvent event) throws Exception {
        try {

            ClassPathResource serviceAccount = new ClassPathResource(path);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(bucketName)
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception ex) {

            throw new Exception(ex.getMessage());

        }
    }

    @Override
    public String getImageUrl(String name, String token) {
        return String.format(imageUrl, name, token);
    }

    @Override
    public FileFirebaseResponse save(MultipartFile file) throws IOException {
/*        Bucket bucket = StorageClient.getInstance().bucket();
        ClassPathResource serviceAccount = new ClassPathResource(path);
        Credentials token = GoogleCredentials.fromStream(serviceAccount.getInputStream());
        String id = generateFileName(file.getOriginalFilename(),"");
        bucket.create(id, file.getBytes(), file.getContentType());
        log.info(bucket.getStorage().get(BlobId.of(bucketName, id)).getSelfLink());
        return id;*/
        Bucket bucket = StorageClient.getInstance().bucket();
        String objectName = generateFileName(file.getOriginalFilename());
        BlobId blobId = BlobId.of(bucketName, objectName);
        String randomAccessToken = IdUtils.nextId();
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(Map.of("firebaseStorageDownloadTokens", randomAccessToken)).build();
        bucket.getStorage().create(blobInfo, file.getBytes());
        return FileFirebaseResponse.builder()
                .fileName(objectName)
                .originFileName(file.getOriginalFilename())
                .fileViewUrl(this.getImageUrl(objectName, randomAccessToken))
                .build();
    }

    @Override
    public List<FileFirebaseResponse> saveAll(List<MultipartFile> fileList) {
        List<FileFirebaseResponse> fileFirebaseResponses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(fileList)) {
            fileList.forEach(multipartFile -> {
                try {
                    fileFirebaseResponses.add(this.save(multipartFile));
                } catch (IOException e) {
                    throw new ResponseException(BadRequestError.FILE_INVALID);
                }
            });
        }
        return fileFirebaseResponses;
    }

    @Override
    public String save(BufferedImage bufferedImage, String originalFileName) throws IOException {
        byte[] bytes = getByteArrays(bufferedImage, getExtension(originalFileName));
        Bucket bucket = StorageClient.getInstance().bucket();
        String name = generateFileName(originalFileName);
        bucket.create(name, bytes);
        return name;
    }

    @Override
    public void delete(String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new IOException("file not found");
        }

        blob.delete();
    }
}
