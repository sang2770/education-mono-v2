package com.sang.nv.education.storage.application.service;


import com.sang.nv.education.storage.application.dto.response.FileFirebaseResponse;
import com.sang.nv.education.storage.infrastructure.support.Constant;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileBaseService {
    String getImageUrl(String name, String token);

    FileFirebaseResponse save(MultipartFile file) throws IOException;

    List<FileFirebaseResponse> saveAll(List<MultipartFile> file);

    String save(BufferedImage bufferedImage, String originalFileName) throws IOException;

    void delete(String name) throws IOException;

    default String getExtension(String originalFileName) {
        return StringUtils.getFilenameExtension(originalFileName);
    }

    default String generateFileName(String originalFileName) {
        return UUID.randomUUID() + Constant.DOT + getExtension(originalFileName);
    }

    default byte[] getByteArrays(BufferedImage bufferedImage, String format) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            ImageIO.write(bufferedImage, format, baos);

            baos.flush();

            return baos.toByteArray();

        } catch (IOException e) {
            throw e;
        } finally {
            baos.close();
        }
    }
}
