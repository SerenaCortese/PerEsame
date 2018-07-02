package ricorsione;

public class AltreRicorsioni {
	
	/*
	 * public List<ArtObject> camminoMassimo(int startId, int LUN){
		//trovare vertice di partenza
		ArtObject start = trovaVertice(startId);
		
		List<ArtObject> parziale = new ArrayList<>();
		parziale.add(start);
		
		this.best = new ArrayList<>();
		best.add(start);//tanto il peso di questa funzione =0 e vien subito rimpiazzato
		
		cerca(parziale, 1, LUN);
		
		return best;
	}
	
	public void cerca(List<ArtObject> parziale, int livello, int LUN) {
		//condizione terminale
		if(livello == LUN) {
			if(peso(parziale) > peso(best)) {
				best= new ArrayList(parziale);
				System.out.println(parziale);
			}
			return;
		}
		
		//trova vertici adiacenti all'ultimo della sequenza
		ArtObject ultimo = parziale.get(parziale.size()-1);
		
		List<ArtObject> adiacenti = Graphs.neighborListOf(this.graph, ultimo);
		
		for(ArtObject prova : adiacenti) {
			if(!parziale.contains(prova) && prova.getClassification().equals(parziale.get(0).getClassification())) {
				parziale.add(prova);
				cerca(parziale,livello+1,LUN);
				parziale.remove(parziale.size()-1);
			}
		}
	}
	 */

}
