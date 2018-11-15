package ventanas;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Fechas
{

   //Pasa una fecha en SQL (yyyy-MM-dd) a String (dd/MM/yyyy)
   public static String convertirSQLAString(String fecha)
   {
      String retorno = null;
      if (fecha != null)
      {
    	  String anio="";
    	  String mes="";;
    	  String dia="";
    	  int j=0;
    	  for(int i=0;i<fecha.length();i++) {
    		  if(fecha.charAt(i)!='-') {
    			  if(j==0)
    				  anio=anio+fecha.charAt(i);
    			  else
    				  if(j==1)
    					  mes=mes+fecha.charAt(i);
    				  else
    					  dia=dia+fecha.charAt(i);
    		  }
    		  else
    			  j++;
    	  }
         retorno = dia + "/" + mes + "/" + anio;
      }
      return retorno;
   }
   
   //Pasa una fecha (dd/MM/yyyy) a SQL (yyyy-MM-dd)
   public static String convertirStringASQL(String fecha) {
	   String retorno=null;
	   if(fecha != null) {
		   String anio="";
		   String mes="";;
		   String dia="";
		   int j=0;
	    	  for(int i=0;i<fecha.length();i++) {
	    		  if(fecha.charAt(i)!='/') {
	    			  if(j==0)
	    				  dia=dia+fecha.charAt(i);
	    			  else
	    				  if(j==1)
	    					  mes=mes+fecha.charAt(i);
	    				  else
	    					  anio=anio+fecha.charAt(i);
	    		  }
	    		  else
	    			  j++;
	    	  }
	    	  retorno = anio + "-" + mes + "-" + dia;
	   }
	   return retorno;
   }

   //Valida que una fecha sea de tipo (dd/MM/yyyy)
   public static boolean validar(String p_fecha)
   {
      if (p_fecha != null)
      {
         try
         {
        	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        	sdf.setLenient(false);
        	sdf.parse(p_fecha);
            return true;
         }
         catch (ParseException ex) {}
      }
      return false;
   }
   
   //Devuelve verdadero si la fecha1 es menor o igual a la fecha2
   public static boolean fechaMenor(String fecha1, String fecha2) {
	   boolean retorno=true;
	   String dia1="";
	   String dia2="";
	   String mes1="";
	   String mes2="";
	   String anio1="";
	   String anio2="";
	   int j=0;
 	   for(int i=0;i<fecha1.length();i++) {
 		  if(fecha1.charAt(i)!='/') {
 				  if(j==0)
 					  dia1=dia1+fecha1.charAt(i);
 				  else
 					  if(j==1)
 						  mes1=mes1+fecha1.charAt(i);
 					  else
 						  anio1=anio1+fecha1.charAt(i);
 		  }
 		  else
 			  j++;
 	   }
 	   j=0;
 	   for(int i=0;i<fecha2.length();i++) {
 		   if(fecha2.charAt(i)!='/') {
 			   if(j==0)
	 			  dia2=dia2+fecha2.charAt(i);
 			   else
	 			  if(j==1)
	 				  mes2=mes2+fecha2.charAt(i);
	 			  else
	 				  anio2=anio2+fecha2.charAt(i);
 		   }
 		   else
 			   j++;
 	   }
 	   
 	   if(Integer.parseInt(anio1)>Integer.parseInt(anio2))
 		   retorno=false;
 	   else
 		   if(Integer.parseInt(anio1)==Integer.parseInt(anio2)) {
 			   if(Integer.parseInt(mes1)>Integer.parseInt(mes2))
 				   retorno=false;
 			   else
 				   if(Integer.parseInt(mes1)==Integer.parseInt(mes2)) {
 					   if(Integer.parseInt(dia1)>Integer.parseInt(dia2))
 						   retorno=false;
 				   }
 		   }
 	   return retorno;
   	}
   
   //Devuelve la fecha actual en SQL (yyyy-MM-dd)
   public static String actualDiaSQL() {
	   	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);
   }
   
}
