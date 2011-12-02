# Funktionaaliset ohjelmointitekniikat imperatiivisella kielellä
### (eli kuinka luovuin käskyistä, ja opin rakastamaan lambdaa)

Funktionaalisessa ohjelmoinnissa rakennetaan ohjelmisto siten, että se koostuu tilattomista funktioista, joiden tulos riippuu pelkästään syötteestä. Sen pohjana on lambda-kalkyyli ja matematiikasta tuttu funktion käsite. Erona imperatiiviseen ohjelmointiin on se, että funktionaalisessa ohjelmoinnissa arvioidaan lausekkeita käskyjen antamisen sijaan.

## Miksi koodata funktionaalisesti?

Imperatiivisessa ohjelmoinnissa mikä tahansa käsky voi muokata ohjelman tilaa. Tätä kutsutaan sivuvaikutukseksi (side-effect). Ohjelmiston ymmärtämisen kannalta rajoittamattomat sivuvaikutukset ovat haitallisia. Esimerkiksi säikeisessä (multi-threaded) ohjelmistossa käsiteltävä tieto voi muuttua kesken metodien suoritusten, mikä voi johtaa vaikeasti selvitettäviin ongelmiin. Koska funktionaalisessa ohjelmoinnissa funktion paluuarvo riippuu vain syötteestä ja tieto on oletusarvoisesti muuttumatonta, tämäntyyppisiä ongelmia ei esiinny.

Soveltamalla muutamia funktionaalisen ohjelmoinnin käsitteitä voidaan imperatiivisellä kielellä kirjoitettujen ohjelmistojen rakennetta selkiyttää ja vähentää virheitä merkittävästi. Lisäksi ohjelmiston rinnakkaistaminen helpottuu, koska tietoa ei tarvitse lukita säikeiden välillä.

## Esittelemme funktionaalisia ohjelmointitekniikoita

Lähes kaikki funktionaaliset ohjelmointitekniikat tähtäävät ohjelman ylläpitämän tilan vähentämiseen. Mitä vähemmän ylläpidettyä tilaa on, sitä vähemmän on odottamattomia sivuvaikutuksia. Täydelliseen tilattomuuteen ei yleensä ole mahdollisuutta imperatiivisella ohjelmointikielellä kirjoitetussa ohjelmassa, eikä sen saavuttamiseksi kannata nähdä liikaa vaivaa.

Seuraavissa kappaleissa esittelemme joitakin funktionaalisten kielien käsitteitä ja esimerkkejä Javalla. Esimerkit on kirjoitettu siten, että ne kuvaavat esiteltyä tekniikkaa. Ne eivät pohjaudu suoraan tosimaailman tilanteisiin. Esimerkit on pyritty tekemään luettaviksi ja toimivat samalla dokumenttina esitellystä tekniikasta.

Koska funktionaalisten ohjelmointikielien ominaisuuksia ei ole suoraan rakennettu näihin kieliin, monet tekniikoista saattavat vaikuttaa oudoilta tai jopa tarkoituksettomilta. Niiden hyödyntäminen johtaa kuitenkin moniin samoihin etuihin, joista funktionaalisen ohjelmointikielten ohjelmoijat nauttivat.

Odottamattomien sivuvaikutuksien syntymistä voi välttää esimerkiksi kirjoittamalla tilattomia funktioita.

### Tilattomat funktiot (Stateless functions)

Imperatiivisessa ohjelmointikielissä metodit voidaan kirjoittaa siten, että ne eivät muokkaa omaa syötettään tai ohjelman tilaa. Funktiota käytettäessä on tärkeää välttää nolla-arvojen (null) palauttamista, sillä tällöin funktioketjun suorittaminen päättyy poikkeukseen.

#### Javalla

Edelläolevassa esimerkissä on suodin, jossa syötettä ei suoraan muokata, vaan palautetaan uusi lista, joka täyttää ehdon. Suotimen ehto on rajapinta, joka usein toteutetaan nimettömänä (anonymous) luokkana.

*suodin*

```java
	import java.util.ArrayList;
	import java.util.List;
	
	public class Filter {
		public List<String> apply(List<String> values, Condition<String> predicate) {
			ArrayList<String> output = new ArrayList<String>();
			for (String string : values) {
				if (predicate.apply(string)) {
					output.add(string);
				}
			}
			return output;
		}
	
		public interface Condition<T> { public boolean apply(T input); }
	}
```

Esimerkki suotimen käytöstä kirjoitettuna testin muotoon. Aiemmin esitelty Condition-rajapinta vastaa täysin guava-kirjaston monikäyttöistä [Predicate-rajapintaa](http://guava-libraries.googlecode.com/svn/trunk/javadoc/com/google/common/base/Predicate.html).

*suotimen testi*

```java
	...
	public class FilterTest {
		private static final List<String> PETS = asList("cat", "dog", "bunny", "tiger");
		private static final List<String> BEASTS = asList("tiger", "lion", "rhino", "bear");

		@Test
		public void separatesBeastsFromPets() {
			List<String> petsWithoutBeasts = new Filter().apply(PETS, new Condition<String>() {
				@Override
				public boolean apply(String input) {
					return isNoBeast(input);
				}
			});
			assertNoBeasts(petsWithoutBeasts);
		}

		private static boolean isNoBeast(String input) {
			for (String beast : BEASTS) {
				if(input.equals(beast)) { return false; }
			}
			return true;
		}

		private void assertNoBeasts(Iterable<String> petsWithoutBeasts) {
			for (String pet : petsWithoutBeasts) {
				for (String beast : BEASTS) {
					assertFalse(pet.equals(beast));
				}
			}
		}
	}
```

Nolla-arvojen palauttaminen voidaan välttää rakentamalla dataluokkien rajapintoihin vakioita, jotka edustavat dataluokan nolla-arvoa. Toinen mahdollisuus on palauttaa esimerkiksi Collections.emptyList, jonka käsitteleminen ei tuota poikkeuksia. Artikkelissa esitellyt myöhemmät esimerkit dataluokista käyttävät seuraavaa rajapintaa.

*dataluokan rajapinta*

```java
	public interface ContactInformation {
		public static final ContactInformation NO_CONTACT_INFORMATION = new NoContactInformation();

		String streetAddress();
		String postCode();
		String postOffice();

		public static final class NoContactInformation implements ContactInformation {
			@Override
			public String streetAddress() { return ""; }

			@Override
			public String postCode() { return ""; }

			@Override
			public String postOffice() { return ""; }
		}
	}
```

Huomaa, että ContactInformation-rajapinnalla on oma tyhjä vakio NO_CONTACT_INFORMATION, jota voidaan käyttää sen sijaan, että palautetaan nolla-arvo. Tällöin nolla-arvon tarkistuksien sijaan arvoja verrataan suoraan NO_CONTACT_INFORMATION-vakioon.

Seuraavassa kappaleessa toteutamme ContactInformation-rajapinnan siten, että siinä oleva data ei voi muuttua.

### Muuttumaton data (Immutable data)

Yksi helpoimpia tapoja vähentää sivuvaikutuksien syntymistä on estää ohjelmakoodin muuttujien suora muokkaaminen. Imperatiivisissa kielissä tämä tarkoittaa sitä, että muuttujat alustetaan arvoilla vain kerran, eikä niille anneta myöhemmin uusia arvoja.

#### Javalla

Tyypillinen Java-bean-rakenne ohjaa väärään suuntaan. Sen sijaan kannattaakin suosia final-avainsanaa, joka estää muuttujien muokkauksen. Tietorakenteiden sisällä olevia muuttujia final-avainsana ei koske. Mikäli on tarve saada muuttumattomia tietorakenteita kuten listoja (List) tai taulukoita (Map), siihen kannattaa käyttää esimerkiksi [Googlen guava-kirjastoa](http://code.google.com/p/guava-libraries/), josta löytyy mm. ImmutableList- ja ImmutableMap-luokat.

Tässä esimerkkinä edellisen kappaleen rajapinnan mukainen muuttumaton dataluokka.

*muuttumaton dataluokka*

```java
	public class Address implements ContactInformation {
		private final String streetAddress;
		private final String postCode;
		private final String postOffice;
		
		public Address(String streetAddress, String postCode, String postOffice) {
			this.streetAddress = streetAddress;
			this.postCode = postCode;
			this.postOffice = postOffice;
		}

		@Override
		public String streetAddress() { return streetAddress; }

		@Override
		public String postCode() { return postCode; }

		@Override
		public String postOffice() { return postOffice; }
	}
```

Muuttumattomat oliot vaativat avukseen apuluokkia, jotta niiden muodostaminen onnistuu kivuttomasti. Mikäli muuttumattomien olioiden muodostimen (constructor) parametreja on paljon, apuna voidaan käyttää rakentajaolioa. Rakentajaolio pitää rakentamiseen tarvittavat arvot tallessa ja palauttaa rakennettavaan olioon arvoja asetettaessa itsensä. Täten rakentajan metodit voidaan ketjuttaa toistensa perään. 

*rakentaja*

```java
	public class AddressBuilder {
		private String buildStreetAddress;
		private String buildPostCode;
		private String buildPostOffice;

		public AddressBuilder withStreetAddress(String streetAddress) {
			buildStreetAddress = streetAddress;
			return this;
		}

		public AddressBuilder withPostCode(String postCode) {
			buildPostCode = postCode;
			return this;
		}

		public AddressBuilder withPostOffice(String postOffice) {
			buildPostOffice = postOffice;
			return this;
		}

		public Address build() {
			return new Address(buildStreetAddress, buildPostCode, buildPostOffice);
		}
	}
```

Seuraavaksi esimerkki rakentajan käytöstä testin muodossa. Rakentajaa käytettäessä kutsutaan ensin tietoa kartuttavia metodeja ja lopuksi build-metodia, joka palauttaa rakennettavan olion. Mikäli kaikki tarvittava tieto on heti saatavissa, voidaan uusi olio rakentaa yhdellä rivillä. Muissa tapauksessa rakentajaa voidaan antaa tietoa hakeville objekteille.

*rakentajan testi*

```java
	import static junit.framework.Assert.assertEquals;

	import org.junit.Test;

	public class AddressBuilderTest {
		private final static String STREET = "Testitie 5";
		private final static String POST_CODE = "00999";
		private final static String POST_OFFICE = "OLEMATON";

		@Test
		public void buildAddressWithTestData() {
			ContactInformation address = new AddressBuilder().withStreetAddress(STREET)
								.withPostCode(POST_CODE)
								.withPostOffice(POST_OFFICE)
								.build();
			assertEquals("Testitie 5", address.streetAddress());
			assertEquals("00999", address.postCode());
			assertEquals("OLEMATON", address.postOffice());
		}
	}
```

Kuten edellisessä testissä rakentajan metodeja, myös funktioiden kutsuja voidaan ketjuttaa toistensa perään. Tätä kutsutaan koostamiseksi.

### Koostaminen (Composition)

Koostamisessa funktion palautusarvot sopivat suoraan seuraavan funktion syötteeksi. Tällä tavalla funktioita voidaan helposti ketjuttaa toisiinsa, ja niistä tulee lyhyitä ja helposti uudelleenkäytettäviä.

Javassa koostaminen tehdään funktio-olioilla, jotka alustetaan syötteellä ja tuottavat saman tuloksen. Funktio-olioita voidaan antaa syötteeksi toisille funktio-olioille, jolloin saadaan aikaan korkeamman asteen funktioita.

#### Javalla

Funktion rajapinta on  yksinkertainen, ja se löytyy mm. [guava-kirjastosta](http://code.google.com/p/guava-libraries/).

```java
	public interface Function<F, T> {
		public T apply(F input);
	}
```

Alla olevassa esimerkissä on käytetty funktioita ja staattisia metodeita siten, että niistä muodostuu oma kielensä. Funktioiden käyttö on siirretty staattisten metodien taakse, jotta vältyttäisiin "new"-sanan toistamiselta. Guava-kirjastossa on monia apuluokkia funktioiden käyttämiseen, kuten [Functions-luokka](http://google-collections.googlecode.com/svn/trunk/javadoc/index.html?com/google/common/base/Functions.html), jota alla oleva esimerkki käyttää.

Functions.compose-metodilla muodostettu koostefuktio arvioidaan vasta kun sen apply-metodia kutsutaan. Jos apply-metodia kutsutaan vasta kun arvoa tarvitaan, tulee koodista laiskaa (lazy). Laiskuuden avulla voidaan välttää turhaa laskentaa esimerkiksi virhetapauksissa, joissa laskettua arvoa ei välttämättä tarvita ollenkaan. 

Tässä muutamia koostefunktioita, joilla voidaan hakea merkkijonosta ensimmäinen ja toinen sana.

*funktioista koostuva apuluokka*

```java
	...
	public class StringList {
		public static String firstWord(String input) {
			return Functions.compose(new First(), new Words()).apply(input);
		}
	
		public static String secondWord(String input) {
			return Functions.compose(new Second(), new Words()).apply(input);
		}
	
		public static class Lines implements Function<String, List<String>> {
			@Override
			public List<String> apply(String input) { return asList(input.split("\n")); }
		
			public static List<String> from(String input) { return new Lines().apply(input); }
		}

		public static class Words implements Function<String, List<String>> {
			@Override
			public List<String> apply(String input) { return Arrays.asList(input.split(" ")); }
		}

		public static class First implements Function<List<String>, String> {
			@Override
			public String apply(List<String> input) { return input.get(0); }
		
			public static String of(List<String> input) { return new First().apply(input); }
		}
	
		public static class Second implements Function<List<String>, String> {
			@Override
			public String apply(List<String> input) { return input.get(1); }
		
			public static String of(List<String> input) { return new Second().apply(input); }
		}
	}
```

Edellisiä koostefunktioita voidaan käyttää hyödyksi, kun halutaan muuttaa merkkijono osoitteeksi. Apuluokassa määritelty kieli näkyy hyvin toAddress-metodissa.

*muuntajaluokka*

```java
	package functional.java.examples;

	import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
	import static functional.java.examples.StringList.*;
	import java.util.List;
	import com.google.common.base.Function;

	public class AddressTransformer implements Function<String, ContactInformation> {
		@Override
		public ContactInformation apply(String input) {
			try {
				return toAddress(Lines.from(input));
			} catch (ArrayIndexOutOfBoundsException e) {
				return NO_CONTACT_INFORMATION;
			}
		}

		private Address toAddress(final List<String> addressLines) {
			AddressBuilder addressBuilder = new AddressBuilder().withStreetAddress(First.of(addressLines));
			addressBuilder.withPostCode(firstWord(Second.of(addressLines)));
			addressBuilder.withPostOffice(secondWord(Second.of(addressLines)));
			return addressBuilder.build();
		}
	}
```

Muuntajaluokkaa käytetään juuri samaan tapaan kuin muitakin funktioita, ja siitäkin voisi tehdä osan koostettua funktiota. Seuraavassa on muuntajaluokan testi, jossa tarkistetaan mahdolliset virhetilanteet.

*muuntajaluokan testi*

```java
	import static functional.java.examples.ContactInformation.NO_CONTACT_INFORMATION;
	import static junit.framework.Assert.assertEquals;

	import org.junit.Test;

	public class AddressTransformerTest {
		private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
		private final static String MISSING_CITY = "Testitie 5\n00999OLEMATON";
		private final static String MISSING_SECOND_LINE = "FUBAR";

		@Test
		public void hasAddressFields() {
			final ContactInformation address = new AddressTransformer().apply(ADDRESS);
			assertEquals("Testitie 5", address.streetAddress());
			assertEquals("00999", address.postCode());
			assertEquals("OLEMATON", address.postOffice());
		}

		@Test
		public void hasNoContactInformationWithMissingCity() {
			final ContactInformation address = new AddressTransformer().apply(MISSING_CITY);
			assertEquals(NO_CONTACT_INFORMATION, address);
		}

		@Test
		public void hasNoContactInformationWithMissingSecondLine() {
			final ContactInformation address = new AddressTransformer().apply(MISSING_SECOND_LINE);
			assertEquals(NO_CONTACT_INFORMATION, address);
		}
	}
```

Muuntajaluokan testi tarkistaa, että virheellisen syötteen tuloksena on aiemmin esitelty NO_CONTACT_INFORMATION-vakio. Tällöin se soveltuu muiden funktioiden kanssa käytettäväksi.

Sen sijaan, että dataa muutetaan käskystä eri muotoon, sen voi tehdä myös tyyppimuunnoksena, jolloin se on oikeassa muodossa silloin kun sitä tarvitaan.

### Tyyppimuunnokset (Type-transformation)

Tyyppimuunnoksessa data muutetaan seuraavan funktion tarvitsemaan muotoon muuttamatta alkuperäistä dataa. Imperatiivisissa kielissä tyyppimuunnoksien tekeminen ilman käskyjä on hankalaa. Voidaan ajatella, että koko datarakenne on jatkuvassa muutoksen tilassa syötteestä palautteeseen. Käskytystä voidaan välttää ketjuttamalla esitysmuodon muunnoksia.

#### Javalla

Yksi tapa tehdä tyyppimuunnoksia (ja samalla muuttumatonta dataa) on edustaja (proxy). Sen sijaan että oliolla on omia muuttujia, se toimii näkymänä toisten olioiden tietosisältöön. Myös edustajia voidaan ketjuttaa toisiinsa siten, että syntyy kutsuketju alkuperäiseen syötteeseen saakka.

*edustaja*

```java
	import static functional.java.examples.StringList.*;

	public class PostalAddress implements ContactInformation {
		private final String address;

		public PostalAddress(String address) { this.address = address; }

		@Override
		public String streetAddress() { return First.of(Lines.from(address)); }

		@Override
		public String postCode() { return firstWord(Second.of(Lines.from(address))); }

		@Override
		public String postOffice() { return secondWord(Second.of(Lines.from(address))); }
	}
```

Edustaja käyttää hyödykseen aiemmin esitellyn apuluokan funktioita. Lopputulos on sama kuin aiemmin, mutta nyt alkuperäinen syöte säilyy aina muuttumattomana.

Tyyppimuunnetun luokan käyttäminen on äärimmäisen yksinkertaista, kuten seuraava testi osoittaa.

*tyyppimuunnoksen testi*

```java
	import static junit.framework.Assert.assertEquals;

	import org.junit.Test;

	public class PostalAddressTest {
		private final static String ADDRESS = "Testitie 5\n00999 OLEMATON";
		private final static ContactInformation postalAddress = new PostalAddress(ADDRESS);
	
		@Test
		public void firstLineIsStreetAddress() {
			assertEquals("Testitie 5", postalAddress.streetAddress());
		}

		@Test
		public void firstWordOfSecondLineIsPostCode() {
			assertEquals("00999", postalAddress.postCode());
		}
	
		@Test
		public void restOfWordsOfSecondLineIsPostOffice() {
			assertEquals("OLEMATON", postalAddress.postOffice());
		}
	}
```

## Lopuksi

Monet funktionaaliset tekniikat käytettynä imperatiivisissa kielissä tuottavat lisää laskentaa paikkoihin, joissa perinteisesti on ylläpidetty tilaa. Tästä ei kannata huolestua, sillä mikäli suorituskykyongelmia löytyy, ne voidaan ratkaista esimerkiksi oikein sijoitetun välimuistin avulla tai säikeistämällä. Mitä muuta välimuisti onkaan kuin funktio, joka muistaa syötteellä saadun arvon!

Funktionaalisten ohjelmointitekniikoiden käyttö vaatii ohjelmoijalta kurinalaisuutta. Erityisesti ulkoisia kirjastoja käytettäessä voi olla hankalaa ohjelmoida funktionaalisella tavalla. Halu lipsua muuttumattoman datan käytöstä tai tyyppimuunnoksien tekemisestä saattaa kasvaa aikataulupaineiden kerääntyessä. Funktionaalisella tiellä pysyminen kuitenkin kannattaa, sillä funktionaalisesti kirjoitettu koodi on helposti luettavampaa ja muokattavampaa kuin imperatiiviseen tapaan kirjoitettu. Tämän lisäksi se on helpompaa testata myös suurempina yksikköinä kuin luokkatasolla.

Artikkelissa esitetyt koodiesimerkit löytyvät [githubista](https://github.com/hsalokor/funktionaalisempi-ohjelmointi).

Me allekirjoittaneet toivomme kaikille lukijoille mukavia funktionaalisia koodaushetkiä!

*Tuomas Hakkarainen ja Harri Salokorpi*
