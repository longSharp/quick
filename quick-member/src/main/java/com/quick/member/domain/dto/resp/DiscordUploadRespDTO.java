package com.quick.member.domain.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class DiscordUploadRespDTO {
    private List<DiscordUploadAtta> attachments;
}
