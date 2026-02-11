public class PrintQueue {

    private class Nodo {
        PrintJob dato;
        Nodo siguiente;

        Nodo(PrintJob dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private Nodo frenteH;
    private Nodo finalH;

    private Nodo frenteM;
    private Nodo finalM;

    private Nodo frenteL;
    private Nodo finalL;

    public PrintQueue() {
        frenteH = null; finalH = null;
        frenteM = null; finalM = null;
        frenteL = null; finalL = null;
    }

    public void enqueue(PrintJob job) {
        Nodo nuevo = new Nodo(job);
        String p = job.getPriority();

        if (p.equals("H")) {
            if (frenteH == null) {
                frenteH = nuevo;
                finalH = nuevo;
            } else {
                finalH.siguiente = nuevo;
                finalH = nuevo;
            }
        }
        else if (p.equals("M")) {
            if (frenteM == null) {
                frenteM = nuevo;
                finalM = nuevo;
            } else {
                finalM.siguiente = nuevo;
                finalM = nuevo;
            }
        }
        else {
            if (frenteL == null) {
                frenteL = nuevo;
                finalL = nuevo;
            } else {
                finalL.siguiente = nuevo;
                finalL = nuevo;
            }
        }
    }

    public PrintJob dequeue() {
        if (frenteH != null) {
            PrintJob trabajo = frenteH.dato;
            frenteH = frenteH.siguiente;

            if (frenteH == null) {
                finalH = null;
            }
            return trabajo;
        }

        if (frenteM != null) {
            PrintJob trabajo = frenteM.dato;
            frenteM = frenteM.siguiente;

            if (frenteM == null) {
                finalM = null;
            }
            return trabajo;
        }

        if (frenteL != null) {
            PrintJob trabajo = frenteL.dato;
            frenteL = frenteL.siguiente;

            if (frenteL == null) {
                finalL = null;
            }
            return trabajo;
        }

        return null;
    }

    public boolean isEmpty() {
        if (frenteH == null && frenteM == null && frenteL == null) {
            return true;
        } else {
            return false;
        }
    }
}