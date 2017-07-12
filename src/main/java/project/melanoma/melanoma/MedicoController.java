package project.melanoma.melanoma;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import project.melanoma.model.Medico;
import project.melanoma.model.Paciente;
import project.melanoma.repositorio.MedicoRepositorio;

@RestController
@RequestMapping("/medico")
public class MedicoController {
	
	@Autowired
	private MedicoRepositorio repositorio;
	
	@Autowired
    private ServletContext servletContext;

	@RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
	public ResponseEntity cadastrar(@RequestBody Medico medico) {
		medico.setPacientes(new ArrayList<>());
		repositorio.add(medico);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public ResponseEntity<List<Medico>> getAll() {
		return new ResponseEntity<List<Medico>>(repositorio.getAll(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getPacientes", method = RequestMethod.GET)
    public ResponseEntity getPacientes(@RequestParam(value="crm") String crm) {
		Optional<Medico> medico = repositorio.getByCRM(crm);
		if (!medico.isPresent()) {
			return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
		}
        return new ResponseEntity<List<Paciente>>(medico.get().getPacientes(), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/cadastrarPaciente", method = RequestMethod.POST)
    public ResponseEntity cadastrarPaciente(@RequestParam(value="crm") String crm, @RequestBody Paciente paciente) {
		Optional<Medico> medico = repositorio.getByCRM(crm);
		if (!medico.isPresent()) {
			return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		medico.get().cadastrarPaciente(paciente);
        return new ResponseEntity(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/getMedico/{crm}", method = RequestMethod.GET)
    public ResponseEntity getMedico(@PathVariable String crm) throws IOException {
		Optional<Medico> medico = repositorio.getByCRM(crm);
		if (!medico.isPresent()) {
			return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return new ResponseEntity<Medico>(medico.get(), HttpStatus.OK);
    }
	
	@RequestMapping(value = "/processarImagem", method = RequestMethod.POST, consumes = "multipart/form-data", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity processarImagem(@RequestParam("file") MultipartFile file, @RequestParam("cpf") String cpf) throws IOException {
		System.out.println(cpf);
		byte[] bytes = file.getBytes();
		String UPLOADED_FOLDER = "/Users/jessicadiniz/Documents/eclipse_maven2/workspace/melanoma/resource/";
        Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
        Files.write(path, bytes);
		
		return new ResponseEntity(HttpStatus.OK);
    }
	
	@RequestMapping(value = "/getResultado", method = RequestMethod.GET)
    public ResponseEntity getResultado() throws IOException {
	    String filePath = "/Users/jessicadiniz/Documents/eclipse_maven2/workspace/melanoma/resource/ISIC_0000001.jpg";
	    byte[] array = Files.readAllBytes(new File(filePath).toPath());
	    
	    final HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.IMAGE_JPEG);
	    
	    return new ResponseEntity<byte[]>(array, headers, HttpStatus.OK);
    }
}