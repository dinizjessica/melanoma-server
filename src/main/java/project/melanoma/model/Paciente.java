package project.melanoma.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"cpf"})
@Setter
@Getter
public class Paciente {
//	private int id;
	private String nome;
	private String cpf;
//	private String sexo;
//	private int idade;
//	private String telefone;
//	private String endereco;
//	private String diagnostico;
//	private String status;
//	private int excluido;
//	private LocalDate dataCadastro;
}
