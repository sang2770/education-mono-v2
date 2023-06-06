package com.sang.nv.education.exam.application.dto.request;

import com.sang.commonmodel.dto.request.Request;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@Data
public class UserExamReviewDoneRequest extends Request {
    @NotBlank
    String feedBack;
    List<String> reviewFileIds;
}