package co.com.bbva.app.notas.contables.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CausalDeRechazoDTO {

    private String cuenta;
    private String divisa;
    private String destino;
    private String error;
}
