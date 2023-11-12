package model;

public class AudioBook extends Book{
    private int runTime;

    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString(){
        return String.format("Book author: %s | title: %s | Published Date: %s | Run Time: %s.", super.getAuthor(), super.getTitle(), super.getPublishedDate(), runTime);
    }
}
