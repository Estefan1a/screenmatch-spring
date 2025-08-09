package com.aluracursos.screenmatch.principal;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda","Luis María","Fernanda", "Eric", "Génesis");

        nombres.stream()
                .sorted()                            //Ordena los nombres en orden alfabético
                .limit(4)                   //VA A LIMITAR LA LISTA a 4
                .filter(n->n.startsWith("B")) //Filtra los nombres que empiezan con "B"
                .map(n->n.toUpperCase())      //Convierte los nombres en mayusculas
                .forEach(System.out::println);      //Imprime los nombres 
    }
}
