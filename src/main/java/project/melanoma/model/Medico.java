package project.melanoma.model;

import java.util.List;

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
//	private String telefone;
//	private String endereco;
//	private String status; //ativo ou inativo
//	private int excluido;
	private List<Paciente> pacientes;

	public void cadastrarPaciente(Paciente paciente) {
		pacientes.add(paciente);
	}
}
