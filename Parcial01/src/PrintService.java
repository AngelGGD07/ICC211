public class PrintService {

    private PrintQueue mainQueue;

    public PrintService() {
        mainQueue = new PrintQueue();
    }

    public void submitJob(String user, int pages, String priority) {
        PrintJob newJob = new PrintJob(user, pages, priority);
        mainQueue.enqueue(newJob);
    }

    public void processNext() {
        PrintJob currentJob = mainQueue.dequeue();
        if (currentJob != null) {
            System.out.println("Imprimiendo: " + currentJob.toString());
        }
    }

    public void processAll() {
        System.out.println("\n Iniciando Impresion \n");
        while (!mainQueue.isEmpty()) {
            processNext();
        }
        System.out.println("\n Fin del proceso ");
    }
}