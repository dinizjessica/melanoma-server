package project.melanoma.repositorio;

import java.lang.reflect.Field;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import project.melanoma.model.Medico;

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