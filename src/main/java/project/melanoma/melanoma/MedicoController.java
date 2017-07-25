package project.melanoma.melanoma;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import project.melanoma.repositorio.PacienteRepositorio;
import project.melanoma.segmentation.ImagemUtil;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    @Autowired
    private MedicoRepositorio medicoRepositorio;

    @Autowired
    private PacienteRepositorio pacienteRepositorio;

    @RequestMapping(value = "/cadastrar", method = RequestMethod.POST)
    public ResponseEntity cadastrar(@RequestBody Medico medico) {
        System.out.println(MedicoController.class.toString() + "/cadastrar" + medico.toString());

        if (medico.getCrm() != null) {
            Optional<Medico> medicoDaBase = medicoRepositorio.getByCRM(medico.getCrm());
            Medico result;
            if (medicoDaBase.isPresent()) {
                result = medicoRepositorio.update(medicoDaBase.get(), medico);
            } else {
                medico.setPacientes(new ArrayList<>());
                result = medicoRepositorio.add(medico);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>("CRM e obrigatorio", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @RequestMapping(value = "/login/{crm}", method = RequestMethod.POST)
    public ResponseEntity cadastrar(@PathVariable String crm, @RequestBody String password) {
        System.out.println(MedicoController.class.toString() + "/login/" + crm);

        Optional<Medico> medico = medicoRepositorio.login(crm, password.replaceAll("\"", ""));
        if (!medico.isPresent()) {
            return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity<>(medico.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<Medico>> getAll() {
        System.out.println(MedicoController.class.toString() + "/getAll");
        return new ResponseEntity<List<Medico>>(medicoRepositorio.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/getPacientes/{crm}", method = RequestMethod.GET)
    public ResponseEntity getPacientes(@PathVariable String crm) {
        System.out.println(MedicoController.class.toString() + "/getPacientes");
        Optional<Medico> medico = medicoRepositorio.getByCRM(crm);
        if (!medico.isPresent()) {
            return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<List<Paciente>>(medico.get().getPacientes(), HttpStatus.OK);
    }

    @RequestMapping(value = "/cadastrarPaciente/{crm}", method = RequestMethod.POST)
    public ResponseEntity cadastrarPaciente(@PathVariable(value = "crm") String crm, @RequestBody Paciente paciente) {
        System.out.println(MedicoController.class.toString() + "/cadastrarPaciente");
        Optional<Medico> medico = medicoRepositorio.getByCRM(crm);
        if (!medico.isPresent()) {
            return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        paciente.setId(System.currentTimeMillis());
        pacienteRepositorio.add(paciente);
        medico.get().cadastrarPaciente(paciente);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/getMedico/{crm}", method = RequestMethod.GET)
    public ResponseEntity getMedico(@PathVariable String crm) throws IOException {
        System.out.println(MedicoController.class.toString() + "/getMedico/" + crm);
        Optional<Medico> medico = medicoRepositorio.getByCRM(crm);
        if (!medico.isPresent()) {
            return new ResponseEntity<String>("CRM nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<Medico>(medico.get(), HttpStatus.OK);
    }

    @RequestMapping(value = "/processarImagem/{pacient_id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity processarImagem(@RequestParam(value = "file") MultipartFile file,
                                          @PathVariable(value = "pacient_id") long pacientId) throws IOException {
        System.out.println(MedicoController.class.toString() + "/processarImagem/" + pacientId);

        Optional<Paciente> paciente = pacienteRepositorio.getById(pacientId);

        if (paciente.isPresent()) {
            paciente.get().setCaminhoFoto(file.getOriginalFilename());
            ImagemUtil.saveOriginalPhoto(file, paciente.get().getId());

            processarImagem(file, paciente);
            return new ResponseEntity(paciente.get(), HttpStatus.OK);
        }
        return new ResponseEntity<String>("Paciente nao encontrado", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private void processarImagem(@RequestParam(value = "file") MultipartFile file, Optional<Paciente> paciente) {
        // processando em uma nova thread
        Runnable processarESalvarImg = () -> {
            BufferedImage result = null;
            try {
                result = ImagemUtil.processarImagem(file.getBytes());
                ImagemUtil.saveResultedImage(result, paciente.get());
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(processarESalvarImg).start();
    }

    @RequestMapping(value = "/getResultado/{pacient_id}", method = RequestMethod.GET)
    public ResponseEntity getResultado(@PathVariable(value = "pacient_id") long pacientId) throws IOException {
        System.out.println(MedicoController.class.toString() + "/getResultado/"+pacientId);

        Optional<Paciente> paciente = pacienteRepositorio.getById(pacientId);

        if (paciente.isPresent() && !paciente.get().getCaminhoFoto().isEmpty()) {
            byte[] array = ImagemUtil.getResultedImage(paciente.get());

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<byte[]>(array, headers, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Paciente ou imagem nao encontrada", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}