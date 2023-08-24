public class Subtask extends TaskClass{
    private int epicID;

    public Subtask( String nameTask, String descriptionTask, String statusTask, int epicID) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;
        this.epicID =epicID;

    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                ", idTask=" + idTask +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                "epicId= " + epicID+
                '}';
    }
}
