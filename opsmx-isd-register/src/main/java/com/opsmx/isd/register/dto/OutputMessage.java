package com.opsmx.isd.register.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutputMessage {
    private String from;
    private String text;
    private String time;
}