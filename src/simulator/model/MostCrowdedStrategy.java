package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchStrategy {

	int timeS;
	
	public MostCrowdedStrategy(int timeSlot) {
		this.timeS = timeSlot;
	}
	
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, 
			int currGreen, int lastSwitchingTime, int currTime) {
		if (roads.isEmpty())
			return -1;
		if (currGreen == -1) {
			int max = qs.get(0).size();
			int iterador = 0;
			//busca la cola de vehiculos (que estan esperando en un semaforo en rojo) mas larga
			for (int i = 1; i < qs.size(); i++) {
				if(qs.get(i).size() > max) {
					max = qs.get(i).size();
					iterador = i;
				}
			}
			return iterador;
		}
		
		if (currTime-lastSwitchingTime <timeS) 
			return currGreen;
		
		/*pone a verde el sem�foro de la carretera entrante con la cola m�s larga, realizando
		una b�squeda circular (en qs) desde la posici�n currGreen+1 modulo el n�mero de
		carreteras entrantes al cruce. Si hay m�s de una carretera cuyas colas tengan el 
		mismo tama�o maximal, entonces coge la primera que encuentra durante la b�squeda.
		Observa que podr�a devolver currGreen.
		*/
		
		int max = qs.get(currGreen + 1).size();
		int iter = currGreen + 1; //mirar
		for (int i =(currGreen + 1)%roads.size(); i%roads.size() != currGreen; i++) {
			
			if (qs.get(i%roads.size()).size() > max) {
				max = qs.get(i%roads.size()).size();
				iter = i%roads.size();
			}
		}
		return iter;
		
	}

}