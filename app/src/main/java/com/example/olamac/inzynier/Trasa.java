package com.example.olamac.inzynier;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Trasa extends Fragment implements View.OnClickListener {


    private DatePickerDialog fromDatePickerDialog;
    private EditText g;
    private TextView textView4;
    private AutoCompleteTextView pktA;
    private AutoCompleteTextView pktB;
    private Button button5;

    public static Trasa newInstance() {
        Trasa fragment = new Trasa();
        return fragment;
    }

    public Trasa() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_trasa, container, false);
        AutoCompleteTextView pktA = (AutoCompleteTextView)
                rootView.findViewById(R.id.pktA);
        AutoCompleteTextView pktB = (AutoCompleteTextView)
                rootView.findViewById(R.id.pktB);
        EditText g = (EditText) rootView.findViewById(R.id.godz);
        Button button5 = (Button) rootView.findViewById(R.id.button5);
        TextView textView4 = (TextView) rootView.findViewById(R.id.textView4);
        this.pktA=pktA;
        this.pktB=pktB;
        this.g=g;
        this.button5=button5;
        this.textView4=textView4;


        Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        g.setText(String.valueOf(mHour + ":" + mMinute));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, PRZYSTANKI);

        pktA.setAdapter(adapter);
        pktB.setAdapter(adapter);

        button5.setOnClickListener(this);
        g.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button5:

                final String pktAA = String.valueOf(pktA.getText());
                final String pktBB = String.valueOf(pktB.getText());
                final String godz = String.valueOf(g.getText());

                new WebServiceHandler()
                        .execute("http://10.0.2.2:8080/JAXRSJsonExample/rest/przyjazd/"+pktAA+"/"+pktBB+"/"+godz);
                Toast.makeText(getActivity(), "http://10.0.2.2:8080/JAXRSJsonExample/rest/przyjazd/"+pktAA+"/"+pktBB+"/"+godz, Toast.LENGTH_SHORT).show();
                break;

            case R.id.godz:

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedMinute2;
                        if (selectedMinute==0){
                            selectedMinute2="0"+Integer.toString(selectedMinute);
                            g.setText(selectedHour + ":" + selectedMinute2);
                            selectedMinute=0;
                        }else if(selectedMinute>0 && selectedMinute<10){
                            selectedMinute2="0"+Integer.toString(selectedMinute);
                            g.setText(selectedHour + ":" + selectedMinute2);
                        }else {
                            g.setText(selectedHour + ":" + selectedMinute);
                        }
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


        }





        }





    private class WebServiceHandler extends AsyncTask<String, Void, String> {

        // okienko dialogowe, które każe użytkownikowi czekać
        private ProgressDialog dialog = new ProgressDialog(getActivity());

        // metoda wykonywana jest zaraz przed główną operacją (doInBackground())
        // mamy w niej dostęp do elementów UI
        @Override
        protected void onPreExecute() {
            // wyświetlamy okienko dialogowe każące czekać
            dialog.setMessage("Czekaj...");
            dialog.show();
        }

        // główna operacja, która wykona się w osobnym wątku
        // nie ma w niej dostępu do elementów UI
        @Override
        protected String doInBackground(String... urls) {

            try {
                // zakładamy, że jest tylko jeden URL
                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();

                // pobranie danych do InputStream
                InputStream in = new BufferedInputStream(
                        connection.getInputStream());

                // konwersja InputStream na String
                // wynik będzie przekazany do metody onPostExecute()
                return streamToString(in);

            } catch (Exception e) {
                // obsłuż wyjątek
                Log.d(MainActivity.class.getSimpleName(), e.toString());
                return null;
            }

        }

        // metoda wykonuje się po zakończeniu metody głównej,
        // której wynik będzie przekazany;
        // w tej metodzie mamy dostęp do UI
        @Override
        protected void onPostExecute(String result) {

            // chowamy okno dialogowe
            dialog.dismiss();

            try {
                // reprezentacja obiektu JSON w Javie
                JSONObject json = new JSONObject(result);


                // pobranie pól obiektu JSON i wyświetlenie ich na ekranie
                textView4.setText("tekst: "
                        + json.optString("pktA") + json.optString("pktB")+ json.optString("godz"));


            } catch (Exception e) {
                // obsłuż wyjątek
                Log.d(MainActivity.class.getSimpleName(), e.toString());
            }
        }
    }

    // konwersja z InputStream do String
    public static String streamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        try {

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }

            reader.close();

        } catch (IOException e) {
            // obsłuż wyjątek
            Log.d(MainActivity.class.getSimpleName(), e.toString());
        }

        return stringBuilder.toString();
    }





    private static final String[] PRZYSTANKI = new String[] {
            "9 Maja", "Adamczewskich", "Adamieckiego", "Aleja Pracy", "Aleja Wędrowców", "Al. Piastów", "Arabska", "Arena", "Arkady (Capitol)", "Armii Krajowej", "Auchan", "Autostrada", "Azaliowa", "Bacciarellego", "Bagatela", "Bajana", "BAJANA - pętla", "Bałtycka", "BARDZKA", "BARDZKA (Cmentarz)", "BARTOSZOWICE", "Bąków", "Berenta","Bezpieczna",
            "Białych Goździków", "BIELANY WROCŁAWSKIE", "Bielany - Kościół", "Bielany - Kwiatowa", "Bielany - MAKRO", "Bielany - PKP", "Bielany - PKS", "Bielany - skrzyżowanie", "Bielawa", "BIEŃKOWICE", "Bierdzany", "Bierutowska", "Bierutowska 75", "BIERUTOWSKA (Ośrodek zdrowia)", "Bierutowska - wiadukt", "Bierzyce", "Bierzyce - wieś", "Biestrzyków - skrzyżowanie",
            "Biskupice Podgórne Dong Seo/Dong Yang", "Biskupice Podgórne LG Chem", "Biskupice Podgórne LG Display", "Biskupice Podgórne LG Electronics", "Biskupice Podgórne LG Innotek", "BISKUPIN", "BLACHARSKA", "BLIZANOWICE", "Blizanowicka", "Bociania", "Bogusławice", "Bojanowska", "Borowa - skrzyżowanie", "Borowa - Stacja PKP", "Borowa - szkoła", "Borowska", "Borowska",
            "Braterska", "Brochowska", "BROCHÓW", "BROCHÓW (Stacja kolejowa)", "Brodnicka", "Brodzka", "Brodzka - łącznik", "Broniewskiego", "Brücknera", "Brzezia Łąka - cmentarz", "Brzezia Łąka - Główna", "Brzezia Łąka - Lipowa", "Brzezina", "Brzezina - Osiedle", "BRZEZINA - pętla", "Brzezinka Średzka", "Brzezińska", "Budziwojowice", "Buforowa", "Bujwida", "Bukowina",
            "Bukowina - skrzyżowanie", "Bukowskiego", "Byków", "Byków - Bar", "Byków - Lange", "Bystrzycka", "Bzowa", "Ceglana", "Centralna", "Centrostal", "CENTRUM HURTU UL. GIEŁDOWA", "Centrum Zarządzania Kryzysowego", "Chełmońskiego", "Chińska", "Chłodna", "Chłopska", "Chociebuska", "Chopina", "Chwałkowska", "C.H. Korona", "Ciechocińska", "Czajkowskiego", "Damrota",
            "Daszyńskiego", "Dawida", "Dąbrowica", "DH Astra", "Długa", "DŁUGA (Politechnika)", "Długołęka Nowy Urząd", "Długołęka - Kościół", "Długołęka - SELGROS", "Długołęka - skrzyżowanie", "Długołęka - Volvo", "Długołęka - Wrocławska", "Długosza", "Dmowskiego", "Dobroszów", "Dobroszycka", "Dokerska", "Dolmed", "Dolnobrzeska", "Dolnośląska Szkoła Wyższa",
            "DOMASZCZYN", "Domaszczyn - Dębowa", "Domaszczyn - sklep", "Domaszczyn - skrzyżowanie", "Domaszczyn - Trzebnicka", "Drukarska", "Drzewieckiego", "Dubois", "DWORCOWA", "Dworska", "DWORZEC AUTOBUSOWY", "DWORZEC GŁ. PKP", "DWORZEC NADODRZE", "DW. ŚWIEBODZKI", "Dyrekcyjna", "Działdowska", "Dzielna", "EPI", "Eureka", "Fadroma", "FAT", "Fiołkowa", "Forum Muzyki",
            "GAJ", "GAJ", "Gajowa", "Gajowicka", "Gajowicka (Gimnazjum nr 23)", "GAJ - pętla", "GALERIA DOMINIKAŃSKA", "Gałczyńskiego", "Gałowska", "GAŁÓW", "Gałów - ferma", "Gałów - PGR", "Gazowa", "Gądowianka", "Gąsiorowskiego", "Gęsia", "Gimnazjum nr 23", "Glinianki", "Głogowska", "Główna", "Głubczycka", "Godebskiego", "Godzieszowa", "Godzieszowa - stajnia", "Gorlicka",
            "Góra Kapliczna", "Górnickiego", "Górnicza", "GRABISZYNEK", "Grabiszyńska", "GRABISZYŃSKA (Cmentarz)", "GRABISZYŃSKA (Cmentarz II)", "Grabowa", "Graniczna", "Graniczna (Strachowicka)", "Grawerska", "Groblice II-Kotowicka", "Groblice - Kolejowa", "Grochowa", "Gromadzka", "Grudziądzka", "Grunwaldzka", "Gwiaździsta", "Hala Orbita", "Hala Stulecia", "Hala Targowa",
            "HALLERA", "Hartmana", "Hippiczna", "Hotel Wrocław", "Hubska", "Husarska", "Hutmen", "Hutnicza", "Hynka", "Ibn Siny Avicenny", "Inflancka", "Inowrocławska", "Inżynierska", "Irysowa", "Iwiny (Rondo)", "Iwiny - Brochowska", "Iwiny - Kolejowa", "IWINY - pętla", "Iwiny - Polna", "Iwiny - Słoneczna", "Jabłeczna", "Jagodno", "JAKSONOWICE", "Janówek", "JANÓWEK (WOŚ)",
            "Januszkowice", "Januszkowice - Wierzbowa", "Jarnołtowska", "JARNOŁTÓW", "Jarocińska", "Jarzębinowa", "Jastrzębia", "Jaworowa", "Jedności Narodowej", "Jeleniogórska", "Jerzmanowo", "JERZMANOWO (Cmentarz I)", "JERZMANOWO (Cmentarz II)", "JERZMANOWO (Pętla)", "Jerzmanowska", "JERZMANOWSKA (Pętla)", "Jędrzejowska", "Joannitów", "Jordanowska", "Jutrosińska", "Kadłubka",
            "Kamienna", "Kamiennogórska", "Kamieńskiego", "KAMIEŃSKIEGO (Pętla)", "Kamieńskiego szpital", "Kamień Brylantowa", "Kamień I", "Kamień II", "Kamień Jantarowa", "Karłowicza", "Karwińska", "Kasprowicza", "Katedra", "Kątna", "Kątowa", "Kąty Wilczyckie", "Kępa", "Kępa - Skrzyżowanie", "Kępińska", "Kętrzyńska", "Kiełczowska", "Kiełczowska (LZN)", "KIEŁCZOWSKA (LZN)",
            "Kiełczowska (cmentarz)", "KIEŁCZÓW", "Kiełczówek", "Kiełczówek - pętla", "Kiełczów Południowa", "Kiełczów Wschodnia", "Kiełczów - Brzozowa", "Kiełczów - Lipowa", "Kiełczów - Mleczna", "Kiełczów - osiedle", "Kiełczów - Rzeczna", "Kiełczów - Sielska Dolina", "Kiełczów - WODROL", "Kiełczów - Wrocławska", "KLECINA", "KLECINA (Stacja kolejowa)", "Kleczkowska",
            "Klimasa", "Kliniki", "KŁOKOCZYCE", "Kmieca", "Kobierzycka", "Kochanowskiego", "KOLBUSZOWSKA (Stadion)", "Kolejowa", "Kolista", "Kolista", "Kolonia Kiełczów", "Kołobrzeska", "Komandorska", "Kominiarska", "Kominiarska-plac sportowy", "Komuny Paryskiej", "Końcowa", "Kopańskiego", "Kopernika", "Kosmonautów (Nowe Żerniki)", "Kosmonautów (Szpital)", "Kossak-Szczuckiej",
            "Koszarowa", "KOSZAROWA (Szpital)", "KOSZAROWA (Uniwersytet)", "Kościelna", "Kościuszki", "Kośnego", "Kośnego1", "Kotowice II-Spacerowa/Leśna", "Kotowice - Cmentarna", "Kotowice - Główna", "Kotowice - Ogrodowa", "KOWALE", "Kowale (Stacja kolejowa)", "Kowalska", "Kowalska I", "KOWALSKA (Urząd Pocztowy)", "Kowieńska", "Kozanowska", "KOZANÓW", "KOZANÓW (Dokerska)",
            "Kozia", "Krajobrazowa", "Krakowiany", "Krakowska", "KRAKOWSKA (Centrum handlowe)", "Krasińskiego", "Krępicka", "Krępicka - szkoła", "KROMERA", "Królewiecka", "Królewska", "Krucza", "Krynicka", "Krzelowska", "Krzemieniecka", "KRZYKI", "KRZYŻANOWICE", "Księska", "KSIĘŻE MAŁE", "Księże Wielkie", "Kurpiów", "Kutrzeby", "KUŹNIKI", "KUŹNIKI (Stacja kolejowa)",
            "Kwiatkowskiego", "Kwiatkowskiego (Rondo)", "Kwidzyńska", "Kwiska", "Lamowice", "Las Mokrzański", "Las Osobowicki", "Las Ratyński", "Lekarska", "LEŚNICA", "Leśnica (stacja kolejowa)", "LEŚNICA - Hermes", "Libelta", "LIPA PIOTROWSKA", "Lipska", "LITEWSKA (Pętla)", "LITOMSKA (ZUS)", "Lubelska", "Łosice", "Łososiowicka", "ŁOZINA", "Łozina - Milicka",
            "Łozina - Nowego Osiedla", "Łozina - Ośrodek zdrowia", "Łozina - Wrocławska", "Ługowa", "Łukaszowice", "Łużycka", "Malinowa", "Malwowa", "Małomicka", "Małopanewska", "Małopolska", "Marcepanowa", "Marchewkowa", "MARINO", "Marszowice", "Marszowicka", "Maślice Małe", "Maślicka", "Maślicka - Osiedle", "Metalowców", "METALOWCÓW (Ośrodek sportu)", "Michałowice",
            "Mickiewicza", "Mielecka", "Międzyrzecka", "Mikołowska", "Miłoszycka", "Miłoszyn", "MIŃSKA (Rondo Rotm. Pileckiego)", "Miodowa", "Mirków - stadion", "Mirków - Wolności", "Młodych Techników", "Mochnackiego", "Modra", "Mokra", "Mokronoska", "MOKRY DWÓR", "Mokrzańska", "Moniuszki", "Monopolowa", "Monte Cassino", "Morelowskiego", "Morwowa", "Mostostal",
            "mosty Mieszczańskie", "Mosty Warszawskie", "most Grunwaldzki", "Most Milenijny", "most Osobowicki", "Most Rakowiecki", "Mroźna", "MUCHOBÓR MAŁY (stacja kolejowa)", "Muchobór Wielki", "Muchobór Wielki (pętla)", "Mydlana", "Mydlice I", "Mydlice II", "Na Grobli", "Na Grobli 22", "Na Niskich Łąkach", "Na Ostatnim Groszu", "Na Szańcach", "Niedziałkowskiego",
            "Niedźwiedzia", "Norwida", "Nowodworska", "Nowowiejska", "Nowy Dom", "Nowy Dwór", "NOWY DWÓR (Pętla)", "Nowy Dwór (Rogowska)", "Nowy Dwór (Stacja kolejowa)", "Nyska", "NYSKA", "Obornicka (Obwodnica)", "Oboźna", "Odolanowska", "Odrodzenia Polski", "Ogród Botaniczny", "Ojca Beyzyma", "Okrzei", "Oleśniczka", "Oleśniczka - skrzyżowanie", "Olsztyńska", "Oławska",
            "Ołtaszyn", "OPATOWICE", "Opatowicka nr 127", "Opatowicka nr 69", "Opatowicka nr 85", "Opera", "OPORÓW", "Orla", "Orląt Lwowskich", "Orlińskiego", "Orzechowa", "OSIEDLE SOBIESKIEGO (Pętla)", "OSIEDLE SOBIESKIEGO (Pętla)", "OSOBOWICE", "OSOBOWICE (Stacja kolejowa)", "OSOBOWICKA (Cmentarz)", "OSOBOWICKA (Cmentarz II)", "Ostowa", "Ostrowskiego", "Os. Przyjaźni",
            "Owczarska", "Ozorzyce-Krasińskiego", "Ożynowa", "Palacha", "Paprotna", "Parafialna", "Park Brochowski", "PARK POŁUDNIOWY", "Park Stabłowicki", "Park Staromiejski", "Park Szczytnicki", "Park Tysiąclecia", "Park Wschodni", "Park Zachodni", "Partynice", "Partynicka", "Partyzantów", "Pasikurowice-cmentarz", "Pasikurowice-Energetyczna", "Pasikurowice-Malinowa",
            "Pasikurowice - n/ż", "Paulińska", "Pawłowice", "Pawłowice", "PAWŁOWICE (Widawska)", "Pawłowicka", "Pereca", "PETRUSEWICZA", "Piastowska", "PIECOWICE", "Pietrzykowice", "PILCZYCE", "Pilczycka (Anima)", "Piłsudskiego", "Piramowicza", "Pisarzowice", "Pisarzowice - Baza", "Pisarzowice - Kolejowa", "Pisarzowice - Szkolna", "Pisarzowice - ul. Kolejowa",
            "Pisarzowice - Wrocławska", "Piwnika - Ponurego", "pl. Bema", "PL. DANIŁOWSKIEGO", "pl. Grunwaldzki", "PL. GRUNWALDZKI", "Pl. Hirszfelda", "PL. JANA PAWŁA II", "pl. Legionów", "pl. Nowy Targ", "pl. Orląt Lwowskich", "Pl. SOLIDARNOŚCI", "PL. STASZICA", "pl. Strzegomski (Muzeum Współczesne)", "pl. Wróblewskiego", "pl. Zgody", "Płońskiego", "Poczta Główna",
            "POCZTA POLSKA", "Podwale", "Pola", "POLANOWICE", "Polanowicka", "Poleska", "Politechnika Wrocławska", "Polkowicka", "Połabian", "Pomorska", "Popowice", "POPOWICE (Stacja kolejowa)", "Poprzeczna", "PORT LOTNICZY", "Poświęcka", "POŚWIĘCKA (Ośrodek zdrowia)", "Poznańska", "Północna", "PRACZE ODRZAŃSKIE", "PRACZE ODRZAŃSKIE (Stacja kolejowa)", "Pracze Widawskie",
            "Pretficza", "Prudnicka", "Prusa", "Pruszowice - las", "Pruszowice - wieś", "Przebiśniegowa", "Przedwiośnie (Stacja kolejowa)", "Przedwiośnie (Stacja kolejowa)", "Przybyły", "Przybyszewskiego", "Przyjaźni", "Przystankowa", "PSARY", "Psary - Parkowa", "Psie Pole", "Psie Pole (Gorlicka)", "Psie Pole (Rondo)", "Psie Pole (Rondo)", "Psie Pole (Rondo)",
            "Psie Pole (stacja kolejowa)", "PSIE POLE (Stacja kolejowa)", "Psie Pole (Sycowska)", "Pułaskiego", "PUŁTUSKA", "Pustecka", "Racławicka", "Racławicka (Gimnazjum nr 23)", "Radio i Telewizja", "Radomierzyce", "Radwanice - Dębowa", "Radwanice - Mickiewicza", "Radwanice - Poprzeczna", "Radwanice - Skrajna", "Radwanice - szkoła", "Rakowiecka", "Raków",
            "Raków - skrzyżowanie", "Raków - zakład", "Ramiszów-kolonia", "RATYŃ", "Ratyń - skrzyżowanie", "Rdestowa", "Redycka", "Reja", "Renoma", "RĘDZIN", "RĘDZIŃSKA", "Rękodzielnicza", "Robotnicza (Cenrozłom)", "Robotnicza (Centrozłom)", "ROD Bajki", "ROD Bielany", "ROD Mieczyk", "ROD Oświata", "ROD Pod Dębem", "ROD Pod Morwami", "ROD Storczyk", "ROD Tysiąclecia",
            "ROD Zgoda", "ROD Źródło Zdrowia", "Rogowska", "Rondo", "Roweckiego", "Rozbrat", "Różanka", "Rubczaka", "RUBCZAKA (Stacja kolejowa)", "Rynek", "Rysia", "Rzemieślnicza", "Samotworska", "Samotwór - Dębowa", "SAMOTWÓR - PAŁAC", "Sanocka", "Sąsiedzka", "Semaforowa", "Serbska", "SĘPOLNO", "Siechnice", "Siechnice - CPN", "Siechnice - CPN n ż",
            "Siechnice - Elektrociepłownia", "Siechnice - Gimnazjum", "Siechnice - ogrodnictwo", "Siechnice - Opolska", "SIECHNICE - OSIEDLE", "Siechnice - Staszica", "Siechnice - Szkolna", "SIECHNICE - TIM", "Siechnice - wiadukt", "Siechnice - Zacisze", "Siechnice - ZOZ", "Siedlec-skrzyżowanie", "Siedlec-szkoła", "Siostrzana", "Skała", "Skarbowców",
            "SKARBOWCÓW (Pętla)", "Skarżyńskiego", "Skierniewicka", "Słoneczna", "Słowiańska", "Smardzów", "SMARDZÓW (FERMA)", "Smardzów - Reja", "Smocza", "Solskiego", "SOŁTYSOWICE", "Sołtysowicka", "Sosnowiecka", "Sowia", "SPISKA (Ośrodek sportu)", "Spółdzielcza", "Stabłowice", "STABŁOWICKA (Ośrodek zdrowia)", "Stabłowicka (Szkoła Podstawowa nr 22)",
            "Stabłowicka (Szkoła Podstawowa nr 22)", "Stadion Olimpijski", "STADION WROCŁAW (Królewiecka)", "STADION WROCŁAW (Królewiecka)", "STADION WROCŁAW (Królewiecka)", "Stadion Wrocław (Lotnicza)", "Stadion Wrocław (Lotnicza)", "Stalowa", "Stanisławowska", "Stanki", "Starodębowa", "Starodworska", "STARODWORSKA (ZUW)", "Starogajowa", "Stępin", "STĘPIN - pętla",
            "Stoczniowa", "Strachowice", "Strachowice General Aviation", "Strachowicka", "Strachowskiego", "Strumykowa", "Strzegomska 56", "Sulimów", "Sułowska (Rondo)", "Suwalska", "Swobodna", "Swojczyce", "Swojczycka", "Szczecińska", "Szczepin", "SZCZODRE", "Szczodre - stawy", "Szczodre - Szkoła", "Szczodre - Trzebnicka", "Szewczenki", "Szkocka", "Szkolna", "Szkoła Żerniki",
            "Szp. Kolejowy", "Sztabowa", "Szybka", "Szybowcowa", "Szymanowskiego", "SZYMANÓW", "Szymanów - skrzyż.", "Ślazowa (pętla)", "Ślęza", "Śliczna", "Śliwice", "Śliwowa", "Ślusarska", "Śniadeckich", "Śnieżna", "Średzka", "Śrubowa", "Świdnicka", "ŚWIDNICKA (Dom Europy)", "Świeradowska", "Świętokrzyska", "ŚWINIARY", "Świstackiego", "Świt", "Św. Katarzyna", "Św. Katarzyna1",
            "Św. Katarzyna", "Św. Katarzyna n.ż", "Św. Katarzyna - Główna", "Św. Katarzyna - Główna n ż", "Św. Katarzyna - Parkowa", "Św. Katarzyna - przedszkole", "Św. Katarzyna - skrzyżowanie", "ŚW. Katarzyna - stacja", "Św. Katarzyna - szkoła", "Św. Katarzyna - szpital", "Św. Katarzyna - Żernicka", "TARNOGAJ", "Tczewska", "Tokary", "Tokary - osiedle", "Topolowa", "Tramwajowa",
            "Transbud", "Trawowa", "Trestno", "TRESTNO (Pętla)", "Trzebnicka", "Trzmielowicka", "TRZMIELOWICKA (Stacja kolejowa)", "Tunelowa", "Turoszowska", "Twardogórska", "Tymiankowa", "Tyniecka", "Tyrmanda", "Uniwersytecka", "Uniwersytet", "Uniwersytet Ekonomiczny", "Uniwersytet Medyczny", "Unruga", "Urząd Wojewódzki", "Volvo", "Volvo - brama II", "Wallenroda", "WAŁBRZYSKA",
            "Warmińska", "Wawrzyniaka", "Weigla", "Wejherowska", "Wełniana", "Węglowa", "Węgrów", "Wiaduktowa", "Widawa", "Wiejska", "Wielka", "Wielkopolska", "Wilczyce", "Wilczyce - Dębowa", "Wilczyce - Sosnowa", "Wilczyce - stary młyn", "Wilczyce - Szkoła", "Wilczyce - Wilczycka", "Wilkszyn", "Wilkszyn - Graniczna", "Wilkszyn - Polna", "Wilkszyńska", "Wiślańska", "Wiśniowa",
            "Wita Stwosza", "Witkowska", "Wojanowska", "WOJANOWSKA (pętla)", "WOJNÓW", "WOJNÓW (pętla)", "Wojszyce", "Wojszycka", "Wolska", "Wrocławski Park Przemysłowy", "Wrocławski Park Technologiczny", "Wrocław Brochów", "Wrocław Główny", "Wrocław Grabiszyn", "Wrocław Kuźniki", "Wrocław Leśnica", "Wrocław Mikołajów", "Wrocław Muchobór", "Wrocław Nadodrze", "Wrocław Nowy Dwór",
            "Wrocław Osobowice", "Wrocław Pawłowice", "Wrocław Popowice", "Wrocław Pracze", "Wrocław Psie Pole", "Wrocław Sołtysowice", "Wrocław Stadion", "Wrocław Świniary", "Wrocław Zachodni", "Wrocław Zakrzów", "Wrocław Żerniki", "Wrozamet", "Wschowska", "Wysoka", "Wysoka-Chabrowa", "Wysoka-Radosna", "Wysoka Osiedle", "Wyszyńskiego", "Wyścigowa", "Wyższa Szkoła Bankowa",
            "ZABRODZIE", "ZACHODNIA (Stacja kolejowa)", "Zacisze", "Zagłębiowska", "Zagłoby", "Zagony", "Zagrodnicza", "Zajazdowa", "Zajączkowska", "Zajezdnia BOREK", "Zajezdnia DĄBIE", "Zajezdnia GAJ", "Zajezdnia GĄDÓW", "Zajezdnia Grabiszyńska", "Zajezdnia IX", "Zajezdnia Obornicka", "Zajezdnia OŁBIN", "Zajezdnia VII", "Zakrzowska", "ZAKRZÓW", "Zalewowa", "Zamkowa",
            "Zaporoska", "Zaprężyn", "Zaprężyn - skrzyżowanie", "Zarembowicza", "ZAWALNA", "Zbożowa", "Zemska", "ZĘBICE - pętla", "Zębice - Prusa", "Zębice - skrzyżowanie", "Zębice - Trzech Lip/Prusa", "Zgorzelisko", "Zielińskiego", "Zielna", "Ziemniaczana", "Zimowa", "Złotnicka", "Złotniki", "Złotostocka", "ZOO", "Zwycięska", "ŻAR", "Żelazna", "Żernicka",
            "Żernicka (stacja kolejowa)", "Żerniki", "ŻERNIKI WROCŁAWSKIE", "Żerniki Wr. - Kolejowa", "Żerniki Wr. - Orla", "Żerniki Wr. - skrzyżowanie", "Żeromskiego", "Żmigrodzka (Obwodnica)", "Żmudzka"
    };
    }
