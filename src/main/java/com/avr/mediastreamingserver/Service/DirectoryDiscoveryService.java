package com.avr.mediastreamingserver.Service;

import java.io.File;

import org.springframework.stereotype.Service;

import com.avr.mediastreamingserver.Model.DirectoryDiscoveryModel;
import com.avr.mediastreamingserver.Utils.Utils;

import lombok.NoArgsConstructor;

@Service
public class DirectoryDiscoveryService {

    DirectoryDiscoveryModel directoryDiscoveryModel;

    public DirectoryDiscoveryService() {}
    
    public DirectoryDiscoveryModel discover(String filePath) {
        this.directoryDiscoveryModel = new DirectoryDiscoveryModel();
        this.directoryDiscoveryModel.setIsDirectory(true);
        File dir = new File(filePath);
        addDiscoveredItems(directoryDiscoveryModel, dir.listFiles());
        return this.directoryDiscoveryModel;
    }

    private void addDiscoveredItems(DirectoryDiscoveryModel directoryDiscoveryModel, File[] files) {
        for(File file : files) {
            if(file.isDirectory()){
                DirectoryDiscoveryModel childDirectoryDiscoveryModel = new DirectoryDiscoveryModel();
                childDirectoryDiscoveryModel.setIsDirectory(Boolean.TRUE);
                addDiscoveredItems(childDirectoryDiscoveryModel, file.listFiles());
                directoryDiscoveryModel.getDirectoryDiscoveryModel().add(childDirectoryDiscoveryModel);
            }
            else {
                if(!Utils.isValidVideoFile(file.getPath())) {
                    continue;
                }
                directoryDiscoveryModel.getFilePath().add(file.getPath());
            }
        }
    }

}
