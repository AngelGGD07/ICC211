public class PrintJob {

    private String user;
    private int pages;
    private String priority;

    public PrintJob(String user, int pages, String priority) {
        this.user = user;
        this.pages = pages;

        if (priority.equals("H") || priority.equals("M") || priority.equals("L")) {
            this.priority = priority;
        } else {
            this.priority = "M";
        }
    }

    public String getPriority() { return priority; }
    public String getUser() { return user; }
    public int getPages() { return pages; }

    @Override
    public String toString() {
        return user + " (" + pages + " paginas, prioridad " + priority + ")";
    }
}