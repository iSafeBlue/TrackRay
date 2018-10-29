package com.trackray.scanner.service;

import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;

public interface SystemService {
    ResultCode scan(Task task);
}
