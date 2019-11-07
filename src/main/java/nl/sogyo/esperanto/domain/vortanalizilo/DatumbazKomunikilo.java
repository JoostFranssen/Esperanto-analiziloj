package nl.sogyo.esperanto.domain.vortanalizilo;

import java.util.ArrayList;
import java.util.List;

import nl.sogyo.esperanto.API.IVortero;
import nl.sogyo.esperanto.persistence.Datumbazo;

public class DatumbazKomunikilo {
	private static Datumbazo datumbazo = Datumbazo.getReVoDatumbazo();
	
	public static List<Vortero> preniElDatumbazo(String vortero) {
		List<Vortero> vorteroj = new ArrayList<>();
		for(IVortero v : datumbazo.preniElDatumbazo(vortero)) {
			vorteroj.add(new Vortero(v));
		}
		return vorteroj;
	}
	
	public static void main(String[] args) {
		System.out.println(preniElDatumbazo("ili"));
	}
}
