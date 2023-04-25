package com.zfsoft.superwindow.feign.settings;

import com.zfsoft.platform.common.data.ApiResultSet;
import com.zfsoft.superwindow.data.web.ReviewDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value ="${zfsoft.feign.middleManager}",contextId = "middleManagerEerduosi")
public interface ReviewsDockingService {

    @RequestMapping(value = "/middleManager/middle/reviewsDocking/projectDataCollection", method = RequestMethod.POST)
    ApiResultSet<String> projectDataCollection(@RequestBody ReviewDto reviewDto);
}
