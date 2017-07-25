package project.melanoma.repositorio;

import org.springframework.stereotype.Repository;
import project.melanoma.model.Medico;

import java.util.Optional;

@Repository
public class MedicoRepositorio extends Repositorio<Medico> {
	
	public Optional<Medico> getByCRM(String crm) {
		return super.repositorio.stream().filter(med -> med.getCrm().equals(crm)).findAny();
	}

	public Optional<Medico> login(String crm, String password) {
		return super.repositorio.stream().filter(
				med -> med.getCrm().equalsIgnoreCase(crm)
						&& med.getPassword().equals(password)).findAny();
	}
}