import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends TaskClass {
    protected ArrayList<Integer> subtaskID;

    public Epic( String nameTask, String descriptionTask, ArrayList<Integer> subtaskID) {
        super( nameTask, descriptionTask);
        this.subtaskID = subtaskID;
    }
    public Epic(int idTask,String nameTask, String descriptionTask, ArrayList<Integer> subtaskID) {
        super( idTask ,nameTask, descriptionTask);
        this.subtaskID = subtaskID;
    }

    public ArrayList<Integer> getSubtaskID() {
        return subtaskID;
    }

    public void setSubtaskID(ArrayList<Integer> subtaskID) {
        this.subtaskID = subtaskID;
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
