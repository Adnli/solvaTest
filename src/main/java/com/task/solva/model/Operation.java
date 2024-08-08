package com.task.solva.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
@Entity
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Operation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currency;
    private Double value;
    private Date date;
    private Long categoryId;
    private Long userId;
    private boolean limitExceeded;
    @ManyToOne
    private ExchangeRate exchangeRate;
}
