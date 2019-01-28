package com.trackray.base.service;

import com.trackray.base.bean.ResultCode;
import com.trackray.base.bean.Task;

public interface DomainService {
    ResultCode scan(Task task);
}
