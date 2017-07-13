package project.melanoma.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode(of = {"crm"})
@Setter
@Getter
public class Medico {
	private String nome;
	private String crm;
	private String telefone;
	private String endereco;
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
