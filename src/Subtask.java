public class Subtask extends TaskClass {
    private int  epicId;

    public Subtask(String nameTask, String descriptionTask, String statusTask, int epicId) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;
        this.epicId =epicId;

    }

    public Subtask(int idTask, String nameTask, String descriptionTask, String statusTask, int epicId) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask = statusTask;
        this.epicId = epicId;

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                ", idTask=" + idTask +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                "epicId= " + epicId +
                '}';
    }
}
