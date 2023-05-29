package com.sang.nv.education.exam.application.service.impl;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.Constants;
import com.sang.commonutil.DateUtils;
import com.sang.commonutil.StrUtils;
import com.sang.commonutil.StringPool;
import com.sang.nv.education.exam.application.config.TemplateQuestionProperties;
import com.sang.nv.education.exam.application.dto.response.ImportQuestionDTO;
import com.sang.nv.education.exam.application.dto.response.ImportQuestionResult;
import com.sang.nv.education.exam.application.service.ExamExcelService;
import com.sang.nv.education.exam.domain.GroupQuestion;
import com.sang.nv.education.exam.domain.Question;
import com.sang.nv.education.exam.domain.Subject;
import com.sang.nv.education.exam.domain.command.AnswerCreateOrUpdateCmd;
import com.sang.nv.education.exam.domain.command.QuestionCreateCmd;
import com.sang.nv.education.exam.domain.repository.QuestionDomainRepository;
import com.sang.nv.education.exam.infrastructure.persistence.entity.GroupQuestionEntity;
import com.sang.nv.education.exam.infrastructure.persistence.entity.SubjectEntity;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.GroupQuestionEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.mapper.SubjectEntityMapper;
import com.sang.nv.education.exam.infrastructure.persistence.repository.GroupQuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.QuestionEntityRepository;
import com.sang.nv.education.exam.infrastructure.persistence.repository.SubjectEntityRepository;
import com.sang.nv.education.exam.infrastructure.support.enums.QuestionLevel;
import com.sang.nv.education.exam.infrastructure.support.enums.QuestionLevelList;
import com.sang.nv.education.exam.infrastructure.support.utils.Const;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ExamExcelServiceImpl implements ExamExcelService {
    static int ROW_NUMBER = 1;
    static int COL_NUMBER = 8;

    private final TemplateQuestionProperties templateProperties;
    private final QuestionDomainRepository questionDomainRepository;
    private final QuestionEntityRepository questionEntityRepository;
    private final SubjectEntityMapper subjectEntityMapper;
    private final GroupQuestionEntityMapper groupQuestionEntityMapper;
    private final GroupQuestionEntityRepository groupQuestionEntityRepository;
    private final SubjectEntityRepository subjectEntityRepository;

    @Override
    public void downloadQuestionTemplate(HttpServletResponse response) {
        List<GroupQuestion> groupQuestions = this.groupQuestionEntityMapper.toDomain(this.groupQuestionEntityRepository.findAll());
        // enrich subject
        List<Subject> subjects = this.subjectEntityMapper.toDomain(this.subjectEntityRepository.findAll());
        groupQuestions.forEach(groupQuestion -> {
            Optional<Subject> subject = subjects.stream().filter(s -> s.getId().equals(groupQuestion.getSubjectId())).findFirst();
            if (subject.isPresent()) {
                groupQuestion.enrichSubject(subject.get());
            }
        });
        List<QuestionLevelList> questionLevelLists = List.of(QuestionLevelList.values());
        String folder = templateProperties.getFolder();
        String fileName = templateProperties.getQuestion().getImportFileName();
        try (InputStream inputStream = new ClassPathResource((String.format("%s%s%s", folder, StringPool.FORWARD_SLASH, fileName))).getInputStream()) {
            String resultFile =
                    String.format(
                            "Mau_Danh_sach_cau_hoi_%s",
                            DateUtils.format(new Date(), Constants.DATE_TIME_FILE_RESULT))
                            + StringPool.XLSX;
            response.addHeader(
                    HttpHeaders.CONTENT_DISPOSITION, Constants.ATTACHMENT_FILE + resultFile);
            response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            OutputStream os = response.getOutputStream();
            Context context = new Context();
            context.putVar("groupQuestions", groupQuestions);
            context.putVar("questionTypes", questionLevelLists);
            JxlsHelper.getInstance().processTemplate(inputStream, os, context);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ResponseException(BadRequestError.IO_EXCEPTION);
        }
    }

    @Override
    public ImportQuestionResult importQuestions(MultipartFile file, HttpServletResponse response) {
        List<Question> createQuestions = new ArrayList<>();
        List<ImportQuestionDTO> importQuestionDTOS = readExcelFile(file, response);
        boolean checkTotalRecordSuccess = true;
        for (ImportQuestionDTO importQuestionDTO : importQuestionDTOS) {
            if (importQuestionDTO.getCheck()) {
                //create
                Question question = new Question(importQuestionDTO.getValue());
                createQuestions.add(question);
            } else {
                checkTotalRecordSuccess = false;
            }
        }
        if (!CollectionUtils.isEmpty(createQuestions) && Boolean.TRUE.equals(checkTotalRecordSuccess)) {
            this.questionDomainRepository.saveAll(createQuestions);
        }
        return ImportQuestionResult.builder().status(checkTotalRecordSuccess).data(importQuestionDTOS).build();
    }

    public List<ImportQuestionDTO> readExcelFile(MultipartFile multipartFile, HttpServletResponse httpResponse) {
        List<ImportQuestionDTO> importQuestionDTOS = new ArrayList<>();
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
                ImportQuestionDTO importQuestionDTO = new ImportQuestionDTO();
                importQuestionDTO.setRowIndex(rowIndex);
                QuestionCreateCmd cmd = new QuestionCreateCmd();
                List<AnswerCreateOrUpdateCmd> answerCreateOrUpdateCmds = new ArrayList<>();
                StringBuilder error = new StringBuilder();
                int answerIndex = 0;
                for (Cell cell : row) {
                    String value = ExcelUtils.readCellContent(cell);

                    switch (cell.getColumnIndex()) {
                        case 1:
                            if (!StrUtils.isBlank(value)) {
                                cmd.setTitle(value);
                            } else {
                                error.append(Const.COL_TITLE_QUESTION_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            break;
                        case 2:
                            if (!StrUtils.isBlank(value)) {
                                Optional<GroupQuestionEntity> groupQuestion = this.groupQuestionEntityRepository.findByCode(value);
                                if (groupQuestion.isPresent()) {
                                    cmd.setGroupId(groupQuestion.get().getId());
                                } else {
                                    error.append(Const.COL_GROUP_QUESTION_NOT_FOUND).append(StringPool.COMMA);
                                }
                            } else {
                                error.append(Const.COL_GROUP_QUESTION_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            break;
                        case 3:
                            if (!StrUtils.isBlank(value)) {
                                Optional<SubjectEntity> subjectEntity = this.subjectEntityRepository.findByCode(value);
                                if (subjectEntity.isPresent()) {
                                    cmd.setSubjectId(subjectEntity.get().getId());
                                } else {
                                    error.append(Const.COL_SUBJECT_QUESTION_NOT_FOUND).append(StringPool.COMMA);
                                }
                            } else {
                                error.append(Const.COL_GROUP_QUESTION_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            break;
                        case 4:
                            if (!StrUtils.isBlank(value)) {
                                cmd.setQuestionLevel(QuestionLevel.valueOf(value));
                            }
                            break;
                        case 5:
                            if (!StrUtils.isBlank(value)) {
                                answerIndex = (int) Float.parseFloat(value);
                            }
                            break;
                        case 6:
                            if (!StrUtils.isBlank(value)) {
                                answerCreateOrUpdateCmds.add(AnswerCreateOrUpdateCmd.builder().content(value).status(answerIndex == 1).build());
                            }
                            break;
                        case 7:
                            if (!StrUtils.isBlank(value)) {
                                answerCreateOrUpdateCmds.add(AnswerCreateOrUpdateCmd.builder().content(value).status(answerIndex == 2).build());
                            }
                            break;
                        case 8:
                            if (!StrUtils.isBlank(value)) {
                                answerCreateOrUpdateCmds.add(AnswerCreateOrUpdateCmd.builder().content(value).status(answerIndex == 3).build());
                            }
                            break;
                        case 9:
                            if (!StrUtils.isBlank(value)) {
                                answerCreateOrUpdateCmds.add(AnswerCreateOrUpdateCmd.builder().content(value).status(answerIndex == 4).build());
                            }
                            if (CollectionUtils.isEmpty(answerCreateOrUpdateCmds)) {
                                error.append(Const.COL_ANSWER_QUESTION_NOT_EMPTY).append(StringPool.COMMA);
                            }
                            break;

                        default:
                            break;
                    }
                }
                cmd.setAnswerCreateOrUpdateCmdList(answerCreateOrUpdateCmds);
                if (error.length() <= 0) {
                    importQuestionDTO.setCheck(true);
                    importQuestionDTO.setValue(cmd);

                } else {
                    importQuestionDTO.setCheck(false);
                    importQuestionDTO.setErrors(error.toString());
                }
                importQuestionDTOS.add(importQuestionDTO);
                rowIndex++;
            }
        }
        catch (Exception e) {
            throw new ResponseException(BadRequestError.QUESTION_INVALID);
        }
        return importQuestionDTOS;
    }

}
