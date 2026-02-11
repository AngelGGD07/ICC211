public class main {

    public static void main(String[] args) {

        System.out.println("******************************************");
        System.out.println(" SISTEMA DE IMPRESION - PARCIAL PRACTICO ");
        System.out.println("******************************************\n");

        PrintService service = new PrintService();

        service.submitJob("Asdruval", 5, "M");
        service.submitJob("Angel", 2, "");
        service.submitJob("Marcos", 10, "L");
        service.submitJob("Wilmary", 3, "H");
        service.submitJob("Belliard", 4, "M");

        service.processAll();
    }
}

/*
 =========================================================================
   PARTE TEÓRICA
 =========================================================================

   1. COMPLEJIDAD ASINTÓTICA DE LAS OPERACIONES:

      PARA LA OPCIÓN A (3 Colas Simples - La que usamos aquí):
      ------------------------------------------------
      a. enqueue (Meter a la cola):
         - c. Notación O (Cota superior / Peor caso): O(1).
           Justificación: Siempre agregamos al final de la cola correspondiente usando un puntero tail o cola. No hay bucles.
         - d. Notación Ω (Cota inferior / Mejor caso): Ω(1).
           Justificación: El tiempo es constante sin importar los datos.
         - e. Notación Θ (Cota ajustada): Θ(1).
           Justificación: El comportamiento es siempre constante.

      b. dequeue (Sacar de la cola):
         - c. Notación O (Cota superior / Peor caso): O(1).
           Justificación: En el peor caso verificamos 3 referencias (H, M, L) y sacamos del frente. Es constante.
         - d. Notación Ω (Cota inferior / Mejor caso): Ω(1).
           Justificación: Si hay datos en la cola H, sale inmediatamente.
         - e. Notación Θ (Cota ajustada): Θ(1).
           Justificación: Siempre tarda un tiempo constante.

      OPCIÓN B (1 Lista Enlazada Ordenada por Prioridad):
      ---------------------------------------------------
      a. enqueue (Meter a la cola):
         - c. Notación O (Cota superior / Peor caso): O(N).
           Justificación: Si el nuevo trabajo tiene la menor prioridad o llegó de último, debemos recorrer toda la lista N nodos para insertarlo al final.
         - d. Notación Ω (Cota inferior / Mejor caso): Ω(1).
           Justificación: Si la lista está vacía o el trabajo tiene mayor prioridad que todos, se inserta al inicio cabeza o frente instantáneamente.
         - e. Notación Θ (Cota ajustada): No aplica estrictamente una constante única global, pero se asume comportamiento lineal Θ(N) en promedio.

      b. dequeue (Sacar de la cola):
         - c. Notación O (Cota superior / Peor caso): O(1).
           Justificación: Siempre sacamos el elemento de la cabeza o frente, que ya es el de mayor prioridad.
         - d. Notación Ω (Cota inferior / Mejor caso): Ω(1).
         - e. Notación Θ (Cota ajustada): Θ(1).

   2. COMPLEJIDAD TOTAL DEL PROCESO (Para N trabajos):

      OPCIÓN A (Nuestra implementación):
      - Insertar N trabajos: N * O(1) = O(N)
      - Procesar N trabajos: N * O(1) = O(N)
      -> TOTAL: O(N) (Lineal, muy eficiente).

      OPCIÓN B (Lista única ordenada):
      - Insertar N trabajos: La suma de 1+2+3...+N pasos da una complejidad
        cuadrática en el peor caso -> O(N^2).
      - Procesar N trabajos: N * O(1) = O(N)
      -> TOTAL: O(N^2) (Cuadrática, se pone lenta si son muchos trabajos).

   3. ANÁLISIS FINAL:

      a. ¿Cuál implementación es más eficiente en el peor caso?
         La Opción A (3 Colas). O(N) gana por mucho a O(N^2) cuando N crece.

      b. ¿Cuál sería más simple de mantener?
         La Opción A. Es mucho más fácil programar y mantener 3 colas tontas
         FIFO= Primero en Entrar, Primero en Salir donde solo metes al final y sacas del principio, que programar
         una sola lista inteligente donde tienes que romper y reconectar
         enlaces en el medio de la lista sin perder datos.

      c. Justificación de Diseño:
         Separar las prioridades en estructuras independientes H, M y L
         elimina la necesidad de ordenar. El orden viene implícito en la
         lógica de negocio primero vaciar H, luego M... Esto reduce la
         carga computacional drásticamente.
*/