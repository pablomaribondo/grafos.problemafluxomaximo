package fluxomaximo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class FluxoMaximo {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        System.out.println("Informe o caminho do arquivo: ");
        Scanner lerArquivo = new Scanner(System.in);
        String arquivo = lerArquivo.nextLine();
        /**
         * Java IO
         */
        InputStream inputStream = new FileInputStream(arquivo);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        /**
         * Lê a primeira linha do arquivo(vértice origem)
         */
        String primeiraLinha = bufferedReader.readLine();
        int verticeOrigem = Integer.parseInt(primeiraLinha);
        /**
         * Lê a segunda linha do arquivo(vértice destino e número de arestas)
         */
        String segundaLinha = bufferedReader.readLine();
        Scanner input = new Scanner(segundaLinha);
        int numeroVertices = Integer.parseInt(input.next());
        int verticeDestino = numeroVertices;
        int numeroArestas = Integer.parseInt(input.next());
        int[][] matrizAdjacencia = new int[numeroVertices][numeroVertices];
        /**
         * Popula a matriz com os valores do arquivo
         */
        for (int i = 0; i < numeroArestas; i++) {
            String linha = bufferedReader.readLine();
            Scanner entrada = new Scanner(linha);
            int u = Integer.parseInt(entrada.next());
            int v = Integer.parseInt(entrada.next());
            int capacidade = Integer.parseInt(entrada.next());
            matrizAdjacencia[u-1][v-1] = capacidade;
        }
        bufferedReader.close();
        
        Grafo g = new Grafo();
        g.exportarGrafo(matrizAdjacencia, numeroVertices, "grafoInicial");
        int[][] grafo = g.corteMinimo(matrizAdjacencia, verticeOrigem - 1, verticeDestino - 1);
        g.exportarGrafo(grafo, numeroVertices, "redeResidual");
        
        
    }
}