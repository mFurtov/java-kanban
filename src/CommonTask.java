public class CommonTask extends TaskClass{

    public CommonTask(String nameTask, String descriptionTask, String statusTask) {
        super(nameTask, descriptionTask);
        this.statusTask = statusTask;
    }

    @Override
    public String toString() {
        return "CommonTask{" +
                "idTask=" + idTask +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}
