package br.com.brasilprevteste.repository.Filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoFilter {
    private Double preco;
    private Integer estoque;
    private String descricao;
}
