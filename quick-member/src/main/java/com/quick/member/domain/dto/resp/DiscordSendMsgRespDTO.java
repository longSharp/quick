package com.quick.member.domain.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class DiscordSendMsgRespDTO {
    private List<DiscordSendMsg> attachments;
}
