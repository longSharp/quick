package com.quick.gpt.feign;

import com.quick.common.dto.resp.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "member-service",path = "/member-service/member")
public interface UserMemberService {
    @GetMapping("queryMemberByUserId")
    R<String> queryMemberByUserId(@RequestParam Long userId);
}
