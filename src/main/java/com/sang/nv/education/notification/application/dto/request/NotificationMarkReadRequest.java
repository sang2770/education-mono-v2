package com.sang.nv.education.notification.application.dto.request;

import com.sang.commonmodel.dto.request.Request;
import com.sang.commonmodel.validator.anotations.ValidateUUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMarkReadRequest extends Request {

    @Valid
    @NotEmpty(message = "ID_REQUIRED")
    private List<@ValidateUUID String> ids;
}
