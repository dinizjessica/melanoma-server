package project.melanoma.melanoma;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import project.melanoma.model.Paciente;
import project.melanoma.repositorio.PacienteRepositorio;

@RestController
@RequestMapping("/paciente")
public class PacienteController {
	
	@Autowired
	private PacienteRepositorio repositorio;

	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public ResponseEntity<Paciente> cadastrar(@RequestBody Paciente paciente) {
		repositorio.add(paciente);
		return new ResponseEntity<Paciente>(paciente, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<Paciente>> getAll() {
		return new ResponseEntity<List<Paciente>>(repositorio.getAll(), HttpStatus.OK);
	}
}