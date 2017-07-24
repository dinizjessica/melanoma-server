package project.melanoma.repositorio;

import org.springframework.stereotype.Repository;

import project.melanoma.model.Paciente;

import java.util.Optional;

@Repository
public class PacienteRepositorio extends Repositorio<Paciente> {

    public Optional<Paciente> getById(long id) {
        return super.repositorio.stream().filter(pac -> pac.getId() == id).findAny();
    }
}