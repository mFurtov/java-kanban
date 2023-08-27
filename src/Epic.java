import java.util.ArrayList;


public class Epic extends TaskClass {
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String nameTask, String descriptionTask, ArrayList<Integer> subtasksId) {
        super(nameTask, descriptionTask);
        this.statusTask ="NEW";
        for (Integer integer : subtasksId) {
            this.subtasksId.add(integer);
        }

    }

    public Epic(int idTask, String nameTask, String descriptionTask, ArrayList<Integer> subtasksId) {
        super(idTask, nameTask, descriptionTask);
        this.statusTask ="NEW";
        for (Integer integer : subtasksId) {
            this.subtasksId.add(integer);
        }
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        for (Integer integer : subtasksId) {
            this.subtasksId.add(integer);
        }
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
