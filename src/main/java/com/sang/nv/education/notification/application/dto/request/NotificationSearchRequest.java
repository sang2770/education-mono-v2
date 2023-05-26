package com.sang.nv.education.notification.application.dto.request;

import com.sang.commonmodel.dto.request.PagingRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSearchRequest extends PagingRequest {

    @ApiModelProperty(hidden = true)
    private String userId;
}
