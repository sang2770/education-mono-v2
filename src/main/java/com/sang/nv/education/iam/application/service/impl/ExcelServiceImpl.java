package com.sang.nv.education.iam.application.service.impl;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.commonutil.Constants;
import com.sang.commonutil.DateUtils;
import com.sang.commonutil.StrUtils;
import com.sang.commonutil.StringPool;
import com.sang.nv.education.iam.application.config.TemplateProperties;
import com.sang.nv.education.iam.application.dto.request.User.UserExportRequest;
import com.sang.nv.education.iam.application.dto.response.ImportResult;
import com.sang.nv.education.iam.application.dto.response.ImportUserDTO;
import com.sang.nv.education.iam.application.dto.response.UserExportDTO;
import com.sang.nv.education.iam.application.service.ExcelService;
import com.sang.nv.education.iam.domain.Classes;
import com.sang.nv.education.iam.domain.Department;
import com.sang.nv.education.iam.domain.Key;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.domain.command.UserCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.query.ClassSearchQuery;
import com.sang.nv.education.iam.domain.repository.UserDomainRepository;
import com.sang.nv.education.iam.infrastructure.persistence.entity.ClassEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.ClassesEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.DepartmentEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.KeyEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.UserEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.ClassesEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.DepartmentEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.KeyEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.sang.nv.education.iam.infrastructure.support.enums.AccountType;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.iam.infrastructure.support.util.Const;
import com.sang.nv.education.iam.infrastructure.support.util.ExcelUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ExcelServiceImpl implements ExcelService {
    static int ROW_NUMBER = 1;
    static int COL_NUMBER = 8;
    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;
    private final ClassesEntityRepository classesEntityRepository;
    private final ClassesEntityMapper classesEntityMapper;
    private final DepartmentEntityRepository departmentEntityRepository;
    private final DepartmentEntityMapper departmentEntityMapper;
    private final KeyEntityRepository keyEntityRepository;
    private final KeyEntityMapper keyEntityMapper;
    private final TemplateProperties templateProperties;
    private final UserDomainRepository userDomainRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void exportUsers(UserExportRequest request, HttpServletResponse response) {
        ClassSearchQuery query = ClassSearchQuery.builder()
                .ids(request.getClassIds())
                .departmentIds(request.getDepartmentIds())
                .keyIds(request.getDepartmentIds()).build();
        List<Classes> classes = this.classesEntityMapper.toDomain(this.classesEntityRepository.findAll(query.getIds(), query.getDepartmentIds(), query.getKeyIds()));
        this.enrichClasses(classes);
        List<String> classIds = classes.stream().map(Classes::getId).collect(Collectors.toList());
        List<User> users = this.userEntityMapper.toDomain(this.userEntityRepository.findAllByClassIds(classIds));
//      // Enrich User
        users.forEach(user -> {
            Optional<Classes> classesOptional = classes.stream().filter(classes1 -> classes1.getId().equals(user.getClassId())).findFirst();
            if (classesOptional.isPresent()) {
                user.enrichClasses(classesOptional.get());
            }
        });
        String folder = templateProperties.getFolder();
        String fileName = templateProperties.getUser().getExportFileName();
        try (InputStream inputStream = new ClassPathResource((String.format("%s%s%s", folder, StringPool.FORWARD_SLASH, fileName))).getInputStream()) {
            String resultFile =
                    String.format(
                            "Danh_Sach_Nguoi_Dung_%s",
                            DateUtils.format(new Date(), Constants.DATE_TIME_FILE_RESULT)) + StringPool.XLSX;
            response.addHeader(
                    HttpHeaders.CONTENT_DISPOSITION, Constants.ATTACHMENT_FILE + resultFile);
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content" +
                    "-Disposition");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            OutputStream os = response.getOutputStream();
            Context context = new Context();
            List<UserExportDTO> userExportDTOS = users.stream().map(UserExportDTO::new).collect(Collectors.toList());
            context.putVar("users", userExportDTOS);
            log.info(userExportDTOS.toString());
//            context.putVar("classes", classes);
            JxlsHelper.getInstance().processTemplate(inputStream, os, context);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ResponseException(BadRequestError.IO_EXCEPTION);
        }
    }

    @Override
    public void downloadUserTemplate(HttpServletResponse response) {
        List<Classes> classes = this.classesEntityMapper.toDomain(this.classesEntityRepository.findAll());
        this.enrichClasses(classes);
        List<AccountType> accountTypes = Arrays.asList(AccountType.values());
        String folder = templateProperties.getFolder();
        String fileName = templateProperties.getUser().getImportFileName();
        try (InputStream inputStream = new ClassPathResource((String.format("%s%s%s", folder, StringPool.FORWARD_SLASH, fileName))).getInputStream()) {
            String resultFile =
                    String.format(
                            "Mau_Danh_Sach_Nguoi_Dung_%s",
                            DateUtils.format(new Date(), Constants.DATE_TIME_FILE_RESULT))
                            + StringPool.XLSX;
            response.addHeader(
                    HttpHeaders.CONTENT_DISPOSITION, Constants.ATTACHMENT_FILE + resultFile);
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content" +
                    "-Disposition");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            OutputStream os = response.getOutputStream();
            Context context = new Context();
            context.putVar("classes", classes);
            context.putVar("accountTypes", accountTypes);
            JxlsHelper.getInstance().processTemplate(inputStream, os, context);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ResponseException(BadRequestError.IO_EXCEPTION);
        }
    }

    @Override
    public ImportResult importUser(MultipartFile file, HttpServletResponse response) {
        List<User> createUsers = new ArrayList<>();
        List<User> updateUsers = new ArrayList<>();
        List<ImportUserDTO> importUserDTOS = readExcelFile(file, response);
        boolean checkTotalRecordSuccess = true;
        List<String> codes = importUserDTOS.stream()
                .filter(it -> Boolean.TRUE.equals(it.getCheck()))
                .map(item -> item.getValue().getCode())
                .collect(Collectors.toList());
        List<UserEntity> userEntities = this.userEntityRepository.findAllByCodes(codes);
        for (ImportUserDTO importUserDTO : importUserDTOS) {
            if (importUserDTO.getCheck()) {
                Optional<UserEntity> userEntity = userEntities.stream()
                        .filter(it -> Objects.equals(importUserDTO.getValue().getEmail(), it.getCode()))
                        .findFirst();
                if (userEntity.isPresent()) {
                    //update
                    User user = this.userEntityMapper.toDomain(userEntity.get());
                    user.update(importUserDTO.getValue());
                    updateUsers.add(user);
                } else {
                    //create
                    User user = new User(importUserDTO.getValue());
                    createUsers.add(user);
                }
            } else {
                checkTotalRecordSuccess = false;
            }
        }
        if (!CollectionUtils.isEmpty(createUsers) && Boolean.TRUE.equals(checkTotalRecordSuccess)) {
            this.userDomainRepository.saveAll(createUsers);
        } else if (!CollectionUtils.isEmpty(updateUsers) && Boolean.TRUE.equals(checkTotalRecordSuccess)) {
            this.userDomainRepository.saveAll(updateUsers);
        }
        return ImportResult.builder().status(checkTotalRecordSuccess).data(importUserDTOS).build();
    }

    public List<ImportUserDTO> readExcelFile(MultipartFile multipartFile, HttpServletResponse httpResponse) {
        List<ImportUserDTO> importUserDTOS = new ArrayList<>();
        int rowIndex = 0;
        try (XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            if (Objects.isNull(sheet)) {
                throw new ResponseException(BadRequestError.USER_INVALID);
            }
            ExcelUtils.removeEmptyRows(sheet);
            for (Row row : sheet) {
                if (rowIndex <= ROW_NUMBER) {
                    rowIndex++;
                    continue;
                }
                ImportUserDTO importUserDTO = new ImportUserDTO();
                importUserDTO.setRowIndex(rowIndex);
                UserCreateOrUpdateCmd cmd = new UserCreateOrUpdateCmd();
                StringBuilder error = new StringBuilder();
                for (Cell cell : row) {
                    String value = ExcelUtils.readCellContent(cell);

                    switch (cell.getColumnIndex()) {
                        case 1:
                            if (!StrUtils.isBlank(value)) {
                                cmd.setCode(value);
                                cmd.setUsername(value);
                                cmd.setPassword(this.passwordEncoder.encode(value));
                            } else {
                                error.append(Const.COL_USER_CODE_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            break;
                        case 2:
                            if (!StrUtils.isBlank(value) && value.length() <= 100) {
                                cmd.setFullName(value);
                            }
                            break;
                        case 3:
                            String emailRegex = ValidateConstraint.FORMAT.EMAIL_PATTERN;
                            if (StrUtils.isBlank(value)) {
                                error.append(Const.COL_EMAIL_NOT_EMPTY).append(StringPool.COMMA);
                                break;
                            }
                            if (!value.matches(emailRegex)) {
                                error.append(Const.COL_EMAIL_NOT_FORMAT).append(StringPool.COMMA);
                                break;
                            }
                            Optional<UserEntity> userEntity = this.userEntityRepository.findByAllEmail(value);
                            if (userEntity.isPresent()) {
                                error.append(Const.COL_EMAIL_EXISTED).append(StringPool.COMMA);
                                break;
                            } else {
                                cmd.setEmail(value);
                            }
                            break;
                        case 4:
                            String phoneRegex = ValidateConstraint.FORMAT.PHONE_NUMBER_PATTERN;
                            if (StrUtils.isBlank(value)) {
                                error.append(Const.COL_PHONE_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            if (!value.matches(phoneRegex)) {
                                error.append(Const.COL_PHONE_NOT_EMPTY).append(StringPool.NEW_LINE);
                                break;
                            }
                            cmd.setPhoneNumber(value);
                            break;
                        case 5:
                            if (!StrUtils.isBlank(value)) {
                                try {
                                    LocalDate localDate = LocalDate.parse(value);
                                    cmd.setDayOfBirth(localDate);
                                } catch (Exception e) {
                                    error.append(Const.COL_DATE_OF_BIRTH_NOT_FORMAT).append(StringPool.COMMA);
                                    break;
                                }
                            }
                            break;
                        case 6:
                            if (StrUtils.isBlank(value)) {
                                error.append(Const.TYPE_USER_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            if (value.trim().equals(UserType.MANAGER.toString())) {
                                cmd.setUserType(UserType.MANAGER);
                            } else {
                                cmd.setUserType(UserType.STUDENT);
                            }
                            break;
                        case 7:
                            if (!StrUtils.isBlank(value)) {
                                List<ClassEntity> classes = this.classesEntityRepository.findAllByCode(List.of(value));
                                if (CollectionUtils.isEmpty(classes)) {
                                    error.append(Const.CLASS_NOT_FOUND).append(StringPool.COMMA);
                                    break;
                                }
                                cmd.setClassId(value);
                            }
                            break;
                        default:
                            break;
                    }
                }

                if (error.length() <= 0) {
                    importUserDTO.setCheck(true);
                    importUserDTO.setValue(cmd);

                } else {
                    importUserDTO.setCheck(false);
                    importUserDTO.setErrors(error.toString());
                }
                importUserDTOS.add(importUserDTO);
                rowIndex++;
            }
        } catch (Exception e) {
            log.error("readExcelFile error: {}", e.getMessage());
            throw new ResponseException(BadRequestError.USER_INVALID);
        }
        return importUserDTOS;
    }


    private void enrichClasses(List<Classes> classes) {
        if (CollectionUtils.isEmpty(classes)) {
            return;
        }
        List<String> departmentIds = classes.stream().map(Classes::getDepartmentId).collect(Collectors.toList());
        List<String> keyIds = classes.stream().map(Classes::getKeyId).collect(Collectors.toList());
        List<Department> departments = this.departmentEntityMapper.toDomain(this.departmentEntityRepository.findByIds(departmentIds));
        List<Key> keys = this.keyEntityMapper.toDomain(this.keyEntityRepository.findByIds(keyIds));
        classes.forEach(classes1 -> {
            Optional<Department> department = departments.stream().filter(department1 -> department1.getId().equals(classes1.getDepartmentId())).findFirst();
            Optional<Key> key = keys.stream().filter(key1 -> key1.getId().equals(classes1.getKeyId())).findFirst();
            classes1.enrichDepartment(department.orElse(null));
            classes1.enrichKey(key.orElse(null));
        });
    }


}
