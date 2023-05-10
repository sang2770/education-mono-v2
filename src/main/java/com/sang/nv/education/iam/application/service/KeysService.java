package com.sang.nv.education.iam.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.BaseSearchRequest;
import com.sang.nv.education.iam.application.dto.request.KeyCreateOrUpdateRequest;
import com.sang.nv.education.iam.domain.Key;

public interface KeysService {
    Key create(KeyCreateOrUpdateRequest request);

    Key update(String id, KeyCreateOrUpdateRequest request);

    PageDTO<Key> search(BaseSearchRequest request);

    Key getById(String id);
}
