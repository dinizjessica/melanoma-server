package project.melanoma.repositorio;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public abstract class Repositorio<T> {
	protected List<T> repositorio;
	
	public Repositorio() {
		repositorio = new ArrayList<>();
	}
	
	public List<T> getAll() {
		return repositorio;
	}
	
	public void add(T input) {
		repositorio.add(input);
	}
}
