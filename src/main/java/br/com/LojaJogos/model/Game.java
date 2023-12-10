package br.com.LojaJogos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Game {
    private long id;
    private String name;
    private BigDecimal price;
    private int amount;
    private String producer;
    private String platform;
    private List<String> staffNameList;
}
