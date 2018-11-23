import org.jacop.core.BooleanVar;
import org.jacop.core.Store;
import org.jacop.jasat.utils.structures.IntVec;
import org.jacop.satwrapper.SatWrapper;
import org.jacop.search.DepthFirstSearch;
import org.jacop.search.IndomainMin;
import org.jacop.search.Search;
import org.jacop.search.SelectChoicePoint;
import org.jacop.search.SimpleSelect;
import org.jacop.search.SmallestDomain;

/*Esto lo pongo yo*/
public class practica2_1 {
	public static void guardarLab_Matriz(String archivo) throws FileNotFoundException, IOException {
		String cadena;
		FileReader f = new FileReader(archivo);
		BufferedReader b = new BufferedReader(f);
		while((cadena = b.readLine())!=null) {
		    System.out.println(cadena);
		}
		b.close();
	}


	public static void main(String args[]){
		Store store = new Store();
		SatWrapper satWrapper = new SatWrapper(); 
		store.impose(satWrapper);					/* Importante: sat problem */



		// 0. LEER FICHERO DE LABERINTO Y PASARLO A UNA MATRIZ
		File file = new File(args[0]);
		BufferedReader br = new BufferedReader(new FileReader(file));
		while ((st = br.readLine()) != null) 
   			System.out.println(st); 
  		} 
		


		for(int i=0; i<Al.length; i++){
			for(){
				if(posicion == " "){
					BooleanVar Al[][] = new BooleanVar(store);
				}
			}
		}


		// 1. DECLARACION DE VARIABLES
		
		BooleanVar x = new BooleanVar(store, "\n Node x");
		BooleanVar y = new BooleanVar(store, "\n Node y");
		BooleanVar z = new BooleanVar(store, "\n Node z");
		BooleanVar w = new BooleanVar(store, "\n Node w");

		// Se registran las variables
		satWrapper.register(x);
		satWrapper.register(y);
		satWrapper.register(z);		
		satWrapper.register(w);

		// Todas las variables en un unico array para despues invocar al metodo que nos
		// permite resolver el problema
		BooleanVar allVariables[] = new BooleanVar[]{x, y, z, w};
		
		// 2. DECLARACION DE LOS LITERALES

		int xLiteral = satWrapper.cpVarToBoolVar(x, 1, true);
		int yLiteral = satWrapper.cpVarToBoolVar(y, 1, true);
		int wLiteral = satWrapper.cpVarToBoolVar(w, 1, true);
		int zLiteral = satWrapper.cpVarToBoolVar(z, 1, true);


		// 3. RESTRICCIONES
		
		/* El problema se va a definir en forma CNF, por lo tanto, tenemos
		   que añadir una a una todas las clausulas del problema. Cada 
		   clausula será una disjunción de literales. Por ello, sólo
		   utilizamos los literales anteriormente obtenidos. Si fuese
		   necesario utilizar un literal negado, éste se indica con un
		   signo negativo delante. Ejemplo: -xLiteral */


		/* Aristas */
		
		/* Por cada arista una clausula de los literales involucrados */
		
		addClause(satWrapper, xLiteral, yLiteral);		/* (x v y) */
		addClause(satWrapper, xLiteral, zLiteral);		/* (x v z) */
		addClause(satWrapper, yLiteral, zLiteral);		/* (y v z) */
		addClause(satWrapper, yLiteral, wLiteral);		/* (y v w) */
		addClause(satWrapper, zLiteral, wLiteral);		/* (z v w) */


		/* Clausulas para limitar el numero de vertices seleccionados (k = 2) */
		
		addClause(satWrapper, -xLiteral, -yLiteral, -zLiteral);		/* (-x v -y v -z) */
		addClause(satWrapper, -xLiteral, -yLiteral, -wLiteral);		/* (-x v -y v -w) */
		addClause(satWrapper, -xLiteral, -zLiteral, -wLiteral);		/* (-x v -z v -w) */
		addClause(satWrapper, -yLiteral, -zLiteral, -wLiteral);		/* (-y v -z v -w) */


		// 4. INVOCAR AL SOLUCIONADOR
		
	    Search<BooleanVar> search = new DepthFirstSearch<BooleanVar>();
		SelectChoicePoint<BooleanVar> select = new SimpleSelect<BooleanVar>(allVariables,new SmallestDomain<BooleanVar>(), new IndomainMin<BooleanVar>());
		Boolean result = search.labeling(store, select);

		if (result) {
			System.out.println("Solution: ");

			System.out.println(x.id() + " " + x.value());
			System.out.println(y.id() + " " + y.value());
			System.out.println(w.id() + " " + w.value());
			System.out.println(z.id() + " " + z.value());

		} else{
			System.out.println("*** No solution");
		}

		System.out.println();
	}


	public static void addClause(SatWrapper satWrapper, int literal1, int literal2){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		satWrapper.addModelClause(clause.toArray());
	}


	public static void addClause(SatWrapper satWrapper, int literal1, int literal2, int literal3){
		IntVec clause = new IntVec(satWrapper.pool);
		clause.add(literal1);
		clause.add(literal2);
		clause.add(literal3);
		satWrapper.addModelClause(clause.toArray());
	}
}
