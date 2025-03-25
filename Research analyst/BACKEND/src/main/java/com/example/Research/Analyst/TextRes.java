package com.example.Research.Analyst;

import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class TextRes {
    private String content;
    private String Operation;
}
