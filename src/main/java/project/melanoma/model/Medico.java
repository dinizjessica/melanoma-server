package project.melanoma.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(of = {"crm"})
@Setter
@Getter
@ToString
public class Medico {
	private String nome;
	private String crm;
	private String telefone;
	private Endereco endereco;
	private String email;
	private String dataNascimento;
	private String CPF;
	private String RG;

	@JsonIgnore
	private String status; //ativo ou inativo
	@JsonIgnore
	private int excluido;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private List<Paciente> pacientes;

	public void cadastrarPaciente(Paciente paciente) {
		pacientes.add(paciente);
	}
}
