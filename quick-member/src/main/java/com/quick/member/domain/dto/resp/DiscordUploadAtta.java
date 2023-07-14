package com.quick.member.domain.dto.resp;

import lombok.Data;

@Data
public class DiscordUploadAtta {
    private Integer id;
    private String upload_url;
    private String upload_filename;
}
