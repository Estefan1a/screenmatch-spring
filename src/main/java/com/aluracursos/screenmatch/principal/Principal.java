package com.aluracursos.screenmatch.principal;
import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3e33ea23";
    private ConvierteDatos conversor = new ConvierteDatos();


    public void muestraElMenu(){
        System.out.println("Por favor escribe el nombre de la serie que deseas buscar");
        //BUSCA LOS DATOS GENERALES DE LAS SERIES
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);

        //Busca los datos de todas las temporadas
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i < datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE +nombreSerie.replace(" ","+")+
                    "&Season="+ i + API_KEY);
            var datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporada);
        }
        //temporadas.forEach(System.out::println);

        //MOSTRAR SÓLO EL TÍTULO DE LOS EPISODIOS PARA LAS TEMPORADAS
//        for (int i = 0; i < datos.totalDeTemporadas(); i++) {
//            List<DatosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        //temporadas.forEach(t-> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //Convertir todas las informaciones en una lista del tipo DatosEpisodio

        List<DatosEpisodio> datosEpisodios=temporadas.stream()
                .flatMap(t -> t.episodios().stream()).collect(Collectors.toList());

        //TOP 5 EPISODIOS
//        System.out.println("TOP 5 Episodios");
//        datosEpisodios.stream().filter(e->!e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primer filtro (N/A)"+ e))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(e -> System.out.println("Ordenación (M>m)"+ e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Tercer filtro Mayuscula (m>M)"+ e))
//                .limit(5)
//                .forEach(System.out::println);

        //Convirtiendo los datos en una lista del tipo Episodio
        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.episodios().stream()
                .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

        //episodios.forEach(System.out::println);

        //Busqueda de episodios a partir de x año
        //System.out.println("Indica el año en que salió el episodio que deseas ver");
        //var fecha = teclado.nextInt();
        //teclado.nextInt();

        //LocalDate fechaBusqueda = LocalDate.of(fecha, 1,1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(e -> e.getFechaDeLanzazmiento()!=null && e.getFechaDeLanzazmiento().isAfter(fechaBusqueda))
//                .forEach(e-> System.out.println(
//                        "Temporada "+e.getTemporada()+
//                                "Episodio "+e.getTitulo()+
//                                "Fecha e lanzamiento "+ e.getFechaDeLanzazmiento().format(dtf)
//                ));

        //Busca episodios por un pedazo de titulo
//        System.out.println("Escriba el titulo del episodio que deseas ver");
//        var pedazoTitulo = teclado.nextLine();
//
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(pedazoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episodio encontrado");
//            System.out.println("Los datos son: "+episodioBuscado.get());
//        }else{
//            System.out.println("Episodio no encontrado");
//        }

        //
        Map<Integer, Double> evaluacionesPorTemporada = episodios.stream()
                .filter(e->e.getEvaluacion()>0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));

        System.out.println(evaluacionesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e->e.getEvaluacion()>0.0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println("Media de las evaluaciones: "+est.getAverage());
        System.out.println("Episodio mejor evaluado: "+ est.getMax());
        System.out.println("Episodio peor evaluado: "+ est.getMin());





    }
}
