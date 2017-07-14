package project.melanoma.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class Endereco {
	private String rua;
	private String complemento;
	private String cidade;
	private String bairro;
	private String estado;
	private String CEP;

	public Endereco() {}
}
