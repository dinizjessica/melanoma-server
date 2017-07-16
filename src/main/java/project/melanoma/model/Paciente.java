package project.melanoma.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Setter
@Getter
public class Paciente {
	private long id;
	private String nome;
	private String cpf;
//	private String sexo;
//	private int idade;
	private String telefone;
	private String endereco;
	private String caminhoFoto;
//	private String diagnostico;
//	private String status;
//	private int excluido;
//	private LocalDate dataCadastro;
}
