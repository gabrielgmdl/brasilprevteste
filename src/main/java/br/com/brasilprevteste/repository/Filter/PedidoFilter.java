package br.com.brasilprevteste.repository.Filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoFilter {
    private Double valorTotal;
    private String descricao;
}
