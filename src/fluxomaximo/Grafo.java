package fluxomaximo;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Grafo {

    /**
     * Função de Busca em Largura
     * @param grafo
     * @param s Vértice inicial
     * @param t Vértice final
     * @param anterior Vetor que guarda o antecessor de cada vértice, no caminho de "s" até ele
     * @return Retorna "true" se existe caminho entre "s" e "t" na rede residual e falso caso não exista.
     */
    private boolean buscaEmLargura(int[][] grafo, int s, int t, int[] anterior) {

        boolean[] visitado = new boolean[grafo.length];

        Queue<Integer> fila = new LinkedList<Integer>();
        fila.add(s);
        visitado[s] = true;
        anterior[s] = -1;

        while (!fila.isEmpty()) {
            int v = fila.poll();
            for (int i = 0; i < grafo.length; i++) {
                if (grafo[v][i] > 0 && !visitado[i]) {
                    fila.offer(i);
                    visitado[i] = true;
                    anterior[i] = v;
                }
            }
        }
        return (visitado[t] == true);
    }
    
    /**
     * Função para achar todos os vértices alcaçáveis a partir de "s"
     * @param grafo
     * @param s Vértice inicial
     * @param visitado Vetor que diz quais vértices foram visitados
     */
    private void acharVizinhos(int[][] grafo, int s, boolean[] visitado) {
        visitado[s] = true;
        for (int i = 0; i < grafo.length; i++) {
            if (grafo[s][i] > 0 && !visitado[i]) {
                acharVizinhos(grafo, i, visitado);
            }
        }
    }

    /**
     * Função que imprime o corte mínimo
     * @param grafo
     * @param s Vértice inicial
     * @param t Vértice final
     */
    public int[][] corteMinimo(int[][] grafo, int s, int t) {
        int u, v;

        /**
         * Cria a rede residual
         */
        int[][] redeResidual = new int[grafo.length][grafo.length];
        /**
         * Preenche o grafo residual com as capacidades do grafo original
         */
        for (int i = 0; i < grafo.length; i++) {
            for (int j = 0; j < grafo.length; j++) {
                redeResidual[i][j] = grafo[i][j];
            }
        }

        int[] anterior = new int[grafo.length];
        
        int fluxoMaximo = 0;

        /**
         * Caso haja caminho de "s" para "t", aumenta-se o fluxo
         */
        while (buscaEmLargura(redeResidual, s, t, anterior)) {

            /**
             * Encontra o fluxo máximo através do caminho encontrado
             */
            int fluxo = Integer.MAX_VALUE;
            for (v = t; v != s; v = anterior[v]) {
                u = anterior[v];
                fluxo = Math.min(fluxo, redeResidual[u][v]);
            }

            /**
             * Atualiza as capacidades residuais das arestas e as inverte ao longo do caminho 
             */
            for (v = t; v != s; v = anterior[v]) {
                u = anterior[v];
                redeResidual[u][v] = redeResidual[u][v] - fluxo;
                redeResidual[v][u] = redeResidual[v][u] + fluxo;
            }
            /**
             * Adiciona o fluxo ao fluxo máximo
             */
            fluxoMaximo += fluxo;
        }
        
        boolean[] foiVisitado = new boolean[grafo.length];
        /**
         * Acha os alcançáveis de "s"
         */
        acharVizinhos(redeResidual, s, foiVisitado);
        /**
         * Cria os conjuntos "S" e "T"
         */
        ArrayList<Integer> conjunto_S = new ArrayList();
        ArrayList<Integer> conjunto_T = new ArrayList();
        ArrayList<Integer> entrada = new ArrayList();
        ArrayList<Integer> saida = new ArrayList();
        /**
         * Imprime na tela o fluxo máximo, os conjuntos "S" e "T", e o corte mínimo
         */
        System.out.println("\nO fluxo máximo é: " + fluxoMaximo);
        for (int i = 0; i < grafo.length; i++) {
            for (int j = 0; j < grafo.length; j++) {
                if (grafo[i][j] > 0 && ((foiVisitado[i] && !foiVisitado[j]) || (foiVisitado[j] && !foiVisitado[i]))) {
                    saida.add(i + 1);
                    entrada.add(j + 1);
                }
                if ((foiVisitado[i] && !foiVisitado[j])) {
                    if (!conjunto_S.contains(i+1)) {
                        conjunto_S.add(i+1);
                    }
                    if (!conjunto_T.contains(j+1)) {
                        conjunto_T.add(j+1);
                    }
                }
            }
        }
        System.out.print("\nS = {");
        for (int i = 0; i < conjunto_S.size(); i++) {
            System.out.print(conjunto_S.get(i));
            if (i != conjunto_S.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("}, T = {");
        for (int i = 0; i < conjunto_T.size(); i++) {
            System.out.print(conjunto_T.get(i));
            if (i != conjunto_T.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
        System.out.println("\nO corte mínimo é: ");
        System.out.print("(S,T) = {");
        for (int i = 0; i < saida.size(); i++) {
            System.out.print("(" + saida.get(i) + ", " + entrada.get(i) + ")");
            if (i != saida.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
        
        return redeResidual;
    }
    /**
     * Exporta o grafo para um arquivo do tipo ".graphml"
     * @param grafo Grafo a ser exportado
     * @param numeroVertices Número de vértices do grafo
     * @param nomeArquivo Nome do arquivo que será salvo com a estrutura do grafo
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException 
     */
    public void exportarGrafo (int [][] grafo, int numeroVertices, String nomeArquivo) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(nomeArquivo + ".graphml", "UTF-8");
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns  http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
        writer.println("\t" + "<key id=\"d0\" for=\"edge\" attr.name=\"capacity\" attr.type=\"int\" />");
        writer.println("\t" + "<graph id=\"G\" edgedefault=\"directed\">");
        for (int i = 0; i < numeroVertices; i++) {
            writer.println("\t\t" + "<node id=\"n" + (i + 1) + "\" />");
        }
        int contador = 0;
        for (int i = 0; i < numeroVertices; i++) {
            for (int j = 0; j < numeroVertices; j++) {
                if (grafo[i][j] != 0) {
                    writer.println("\t\t" + "<edge id=\"e" + (++contador) + "\" source=\"n" + (i + 1) + "\" target=\"n" + (j + 1) + "\">");
                    writer.println("\t\t\t" + "<data key=\"d0\">" + grafo[i][j] + "</data>");
                    writer.println("\t\t" + "</edge>");
                }
            }
        }
        writer.println("\t" + "</graph>");
        writer.println("</graphml>");
        writer.close();
    } 
}