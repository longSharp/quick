package com.quick.gpt.domain.dto;

import lombok.Data;

@Data
public class Billing {

    private String dueDate;

    private String total;

    private String usage;

    private String balance;
}
