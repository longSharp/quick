package com.quick.gpt.dto;

import lombok.Data;

@Data
public class Billing {

    private String dueDate;

    private String total;

    private String usage;

    private String balance;
}
