package dev.onyxstudios.projecti.api.block;

public interface IBellowsTickable {

    void addProgress(int amount);
    int getProgress();
    int getTotalProgress();
}
