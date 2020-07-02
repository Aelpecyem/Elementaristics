package de.aelpecyem.elementaristics.common.feature.stats;

public interface IElemStats {
    //other stats: ascension route (String), ascension stage (Byte):
    //Max magan and magan regen can be extrapolated from these.
    int getMagan();
    void setMagan(int magan);

    void setAscensionStage(byte stage);
    byte getAscensionStage();

    String getAscensionPath();
    void setAscensionPath(String path);
}
