package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.persistence.Datumbazo;

public class DatumbazKomunikilo {
	private static Datumbazo datumbazo = Datumbazo.getReVoDatumbazo();
	
	public static List<Vortero> preniElDatumbazo(String vortero) {
		return preniElDatumbazo(vortero, v -> true);
	}
	public static List<Vortero> preniElDatumbazo(String vortero, Predicate<IVortero> filtrilo) {
		List<Vortero> vorteroj = new ArrayList<>();
		for(IVortero v : datumbazo.preniElDatumbazo(vortero)) {
			if(filtrilo.test(v)) {
				vorteroj.add(new Vortero(v));
			}
		}
		return vorteroj;
	}
	
	public static void main(String[] args) {
		System.out.println(preniElDatumbazo("ili"));
	}
}
