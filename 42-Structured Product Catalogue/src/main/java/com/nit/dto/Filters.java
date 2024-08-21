package com.nit.dto;

import com.nit.entity.Attribute;
import lombok.Data;

@Data
public class Filters {
    private String name;
    private String  categoryName;
    private Attribute attributes;
}
