package eu.ebdit.sqleasy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.portable.ApplicationException;

import com.sun.xml.internal.ws.util.StringUtils;


/**
 * FIXME: nezvladam komentare
 * jedine komentare, ktere zvladam jsou blokove, ktere neobsahuji strednik :( 
 */
public class SkriptParser {
	
	private static final Logger log = Logger.getLogger(SkriptParser.class);
	
	private static final String DELIMITER_STRING = "DELIMITER";
	//private static final String ZACATEK_KOMENTARE = "/*";
	//private static final String KONEC_KOMENTARE = "*/";
	private static final Pattern DELIMITER_DEFINITION_PATTERN = Pattern.compile(DELIMITER_STRING + "\\s+(.+)\\s*");
	private static final String ZAKLADNI_ODDELOVAC = ";";

	private SkriptParser() {}
	
	public static List<String> rozdelNaPrikazy(File skript)
			throws ApplicationException {
		
		List<String> radky = StringUtils.nactiRadky(skript);
		List<String> prikazy = nactiPrikazy(radky);
		
		return prikazy;

	}

	private static List<String> nactiPrikazy(List<String> radky)
			throws ApplicationException {
		List<String> prikazy = new ArrayList<String>();

		String oddelovac = ZAKLADNI_ODDELOVAC;
		String praveNacitanyPrikaz = "";

		String lineSeparator=System.getProperty("line.separator");
		
		for (String s : radky) {
			
			if (s.trim().startsWith("--")) {
				log.info("Vynechavam radek komentare: " + s);
				continue;
			}

			Matcher m = DELIMITER_DEFINITION_PATTERN.matcher(s);
			if (m.matches()) {
				oddelovac = m.group(1);
				if (oddelovac == null) {
					throw new ApplicationException(String.format("Neni mozne nacist novy oddelovac z radky: %s", s));
				}
			} else if (!s.contains(oddelovac)) {
				praveNacitanyPrikaz += " " + s + lineSeparator;
			} else {
				List<String> split = new ArrayList<String>(Arrays.asList(s
						.split(Pattern.quote(oddelovac))));

				// prvni cast patri jeste k poslednimu prikazu
				praveNacitanyPrikaz += " " + split.get(0);
				prikazy.add(praveNacitanyPrikaz.trim());
				praveNacitanyPrikaz = "";

				boolean konciOddelovacem = s.lastIndexOf(oddelovac) == s.length()
						- oddelovac.length();

				// od druheho do predposledniho jsou to cele prikazy
				// posledni je cely prikaz pouze pokud konci radek oddelovacem
				for (int i = 1; i < split.size() - (konciOddelovacem ? 0 : 1); i++) {
					prikazy.add(split.get(i).trim());
				}

				if (!konciOddelovacem) {
					praveNacitanyPrikaz = split.get(split.size() - 1);
				}
			}
		}
		return prikazy;
	}

	
	public static void main(String[] args) {
		String[] radky = {
				"Zacneme jednim radkem",
				"rozdelenym do dvou;",
				"Druhy radek bude taky",
				"rozdelenej, ale bude koncit v pulce; Treti bude zacinat v pulce a skonci hned;",
				"Ctvrtej bude pres cast;Patej bude pres cast; Sestej bude pres cast a bude prasacky ukoncej na zacatku dalsiho",
				";Sedmej bude jen tak;",
				DELIMITER_STRING + "   " + "$$",
				"Ted ; by ; meli ; byt ; prepnuty ; oddelovace $$",
				"Nasleduje to same s novymy oddelovaci$$",
				"Zacneme jednim radkem",
				"rozdelenym do dvou$$",
				"Druhy radek bude taky",
				"rozdelenej, ale bude koncit v pulce$$ Treti bude zacinat v pulce a skonci hned$$",
				"Ctvrtej bude pres cast$$Patej bude pres cast$$ Sestej bude pres cast a bude prasacky ukoncej na zacatku dalsiho",
				"$$Sedmej bude jen tak$$",
				"A jeste jednou puvodni se strednikama (bez prepnuti)$$",
				"Zacneme jednim radkem",
				"rozdelenym do dvou;",
				"Druhy radek bude taky",
				"rozdelenej, ale bude koncit v pulce; Treti bude zacinat v pulce a skonci hned;",
				"Ctvrtej bude pres cast;Patej bude pres cast; Sestej bude pres cast a bude prasacky ukoncej na zacatku dalsiho",
				";Sedmej bude jen tak;",
				DELIMITER_STRING + "   " + ";",
				"Ted by mel byt zpatky puvodni",
				"Zacneme jednim radkem",
				"rozdelenym do dvou;",
				"Druhy radek bude taky",
				"rozdelenej, ale bude koncit v pulce; Treti bude zacinat v pulce a skonci hned;",
				"Ctvrtej bude pres cast;Patej bude pres cast; Sestej bude pres cast a bude prasacky ukoncej na zacatku dalsiho",
				";Sedmej bude jen tak;",
				"A jeste jednou s dolarama (bez prepnuti);",
				"Zacneme jednim radkem",
				"rozdelenym do dvou$$",
				"Druhy radek bude taky",
				"rozdelenej, ale bude koncit v pulce$$ Treti bude zacinat v pulce a skonci hned$$",
				"Ctvrtej bude pres cast$$Patej bude pres cast$$ Sestej bude pres cast a bude prasacky ukoncej na zacatku dalsiho",
				"$$Sedmej bude jen tak$$",
				";Test s komentari;;",
				"Zacneme komentarem /**/, ktery vlastne komentar neni;",
				"Normalni /* komentar */ uvnitr radku;",
				"Dalsi komentar bude s falsenym konecem /* bla bla;",
				"bla bla */ kecy kecy;",
				"Ctvrtej bude pres /*cast;Patej bude pres cast; /*Sestej*/ bude pres cast a bude prasacky */ukoncej na zacatku dalsiho",
				";Sedmej bude jen tak;",
				";;;A to je vse pratele;;;"
		};
		
		try {
			List<String> prikazy = nactiPrikazy(Arrays.asList(radky));
			for (String prikaz : prikazy) {
				System.out.println(prikaz);
			}
			
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
