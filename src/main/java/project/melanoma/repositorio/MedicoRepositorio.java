package project.melanoma.repositorio;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import project.melanoma.model.Medico;

@Repository
public class MedicoRepositorio extends Repositorio<Medico> {
	
	public Optional<Medico> getByCRM(String crm) {
		return super.repositorio.stream().filter(med -> med.getCrm().equals(crm)).findAny();
	}	
}