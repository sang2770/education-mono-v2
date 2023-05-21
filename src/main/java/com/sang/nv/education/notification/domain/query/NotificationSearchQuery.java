package com.sang.nv.education.notification.domain.query;

import com.sang.commonmodel.query.PagingQuery;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class NotificationSearchQuery extends PagingQuery {
    private String userId;
}
