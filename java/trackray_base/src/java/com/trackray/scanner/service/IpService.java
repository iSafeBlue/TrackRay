package com.trackray.scanner.service;

import com.trackray.scanner.bean.ResultCode;
import com.trackray.scanner.bean.Task;

public interface IpService {
    ResultCode scan(Task task);
}
