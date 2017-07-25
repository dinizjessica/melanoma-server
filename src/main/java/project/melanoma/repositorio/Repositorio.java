package project.melanoma.repositorio;

import org.springframework.stereotype.Repository;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public abstract class Repositorio<T> {
	protected List<T> repositorio;
	
	public Repositorio() {
		repositorio = new ArrayList<>();
	}
	
	public List<T> getAll() {
		return repositorio;
	}
	
	public T add(T input) {
		repositorio.add(input);
		return input; // lista ja atualiza a referencia
	}

	public T update(T antigo, T novo) {
		T updatedObject = merge(antigo, novo);
		return updatedObject;
		// lista ja atualiza a referencia
	}

	private T merge(T antigo, T novo) {
		List<String> excludedMethods = new ArrayList<String>(
				Arrays.asList("getCrm", "getStatus", "getExcluido", "getPacientes"));
 		Method[] methods = antigo.getClass().getMethods();
		for(Method fromMethod : methods){
			if(fromMethod.getDeclaringClass().equals(antigo.getClass())
					&& fromMethod.getName().startsWith("get")
					&& !excludedMethods.contains(fromMethod.getName())) {
				try {
					String fromName = fromMethod.getName();
					String toName = fromName.replace("get", "set");
					Method toMetod = antigo.getClass().getMethod(toName, fromMethod.getReturnType());

					Object valueNovo = fromMethod.invoke(novo, (Object[])null);
					Object valueAntigo = fromMethod.invoke(antigo, (Object[])null);

					if(valueNovo != null && !valueNovo.equals(valueAntigo)){
						if (!fromMethod.getName().equals("getPassword")) {
							toMetod.invoke(antigo, valueNovo);
						} else {
							if (!String.valueOf(valueNovo).isEmpty()) {
								toMetod.invoke(antigo, valueNovo);
							}
						}

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return antigo;
	}
}
