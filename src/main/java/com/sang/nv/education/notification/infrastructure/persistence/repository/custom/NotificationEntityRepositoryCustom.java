package com.sang.nv.education.notification.infrastructure.persistence.repository.custom;


import com.sang.nv.education.notification.domain.query.NotificationSearchQuery;
import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationEntity;

import java.util.List;

public interface NotificationEntityRepositoryCustom {
    List<NotificationEntity> search(NotificationSearchQuery querySearch);

    Long count(NotificationSearchQuery querySearch);

}
