import java.util.ArrayList;


public class Epic extends TaskClass {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask) {
        super(nameTask, descriptionTask);
        this.statusTask = "NEW";

    }

    public Epic(int idTask, String nameTask, String descriptionTask) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask = "NEW";
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }


    @Override
    public String toString() {
        return "Epic{" +
                ", idTask=" + idTask +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}
