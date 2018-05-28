package fluxomaximo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
        int[][] grafo = g.corteMinimo(matrizAdjacencia, verticeOrigem - 1, verticeDestino - 1);
        /**
         * Exporta o último grafo para um arquivo .graphml
         */
        PrintWriter writer = new PrintWriter("grafo.graphml", "UTF-8");
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"  \n" +
        " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
        " xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns \n" +
        " http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
        writer.println("<key id=\"d0\" for=\"edge\" attr.name=\"capacity\" attr.type=\"int\" />");
        writer.println("<graph id=\"G\" edgedefault=\"directed\">");
        for (int i = 0; i < numeroVertices; i++) {
            writer.println("<node id=\"n" + (i + 1) + "\" />");
        }
        int contador = 0;
        for (int i = 0; i < numeroVertices; i++) {
            for (int j = 0; j < numeroVertices; j++) {
                if (grafo[i][j] != 0) {
                    writer.println("<edge id=\"e" + (++contador) + "\" source=\"n" + (i + 1) + "\" target=\"n" + (j + 1) + "\">");
                    writer.println("<data key=\"d0\">" + grafo[i][j] + "</data>");
                    writer.println("</edge>");
                }
            }
        }
        writer.println("</graph>");
        writer.println("</graphml>");
        writer.close();
        
        System.out.println("O arquivo grafo.graphml foi criado na pasta do projeto Netbeans!");
    }
}