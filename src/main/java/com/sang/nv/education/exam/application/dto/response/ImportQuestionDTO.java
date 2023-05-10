package com.sang.nv.education.exam.application.dto.response;

import com.sang.nv.education.exam.domain.command.QuestionCreateCmd;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
public class ImportQuestionDTO {
    private int rowIndex;
    private QuestionCreateCmd value;
    private Boolean check;
    private String errors;
}
