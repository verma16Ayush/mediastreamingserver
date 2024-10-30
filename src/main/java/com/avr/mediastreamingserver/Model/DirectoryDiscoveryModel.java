package com.avr.mediastreamingserver.Model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectoryDiscoveryModel {
    public record FileLocAndHashRecord(String filePath, String hash) {}
    Boolean isDirectory;
    String directoyPath;
    List<DirectoryDiscoveryModel> directoryDiscoveryModel;
    List<FileLocAndHashRecord> fileLocAndHashRecords;

    public DirectoryDiscoveryModel() {
        this.isDirectory = false;
        this.directoyPath = "";
        this.directoryDiscoveryModel = new ArrayList<>();
        this.fileLocAndHashRecords = new ArrayList<>();
    }
}
