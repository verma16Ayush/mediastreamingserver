package com.avr.mediastreamingserver.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.avr.mediastreamingserver.Model.DirectoryDiscoveryModel;
import com.avr.mediastreamingserver.Model.FileLocAndHashRecord;
import com.avr.mediastreamingserver.Utils.Utils;


@Service
public class DirectoryDiscoveryService {

    DirectoryDiscoveryModel directoryDiscoveryModel;

    @Autowired
    MediaStore mediaStore;
    
    public DirectoryDiscoveryService() {}
    
    public DirectoryDiscoveryModel discover(String filePath) throws UnsupportedEncodingException {
        this.directoryDiscoveryModel = new DirectoryDiscoveryModel();
        this.directoryDiscoveryModel.setIsDirectory(true);
        File dir = new File(filePath);
        addDiscoveredItems(directoryDiscoveryModel, dir.listFiles(), filePath.length());
        return this.directoryDiscoveryModel;
    }

    private void addDiscoveredItems(DirectoryDiscoveryModel directoryDiscoveryModel, File[] files, int rootPathEndIndex) throws UnsupportedEncodingException {
        for(File file : files) {
            if(file.isDirectory()){
                DirectoryDiscoveryModel childDirectoryDiscoveryModel = new DirectoryDiscoveryModel();
                childDirectoryDiscoveryModel.setIsDirectory(Boolean.TRUE);
                childDirectoryDiscoveryModel.setDirectoyPath(file.getPath().substring(rootPathEndIndex + 1));
                addDiscoveredItems(childDirectoryDiscoveryModel, file.listFiles(), rootPathEndIndex);
                directoryDiscoveryModel.getDirectoryDiscoveryModel().add(childDirectoryDiscoveryModel);
            }
            else {
                if(!Utils.isValidVideoFile(file.getPath())) {
                    continue;
                }
                directoryDiscoveryModel.getFileLocAndHashRecords().add(new FileLocAndHashRecord(file.getPath().substring(rootPathEndIndex + 1), mediaStore.insertInMapAndRecordList(file.getPath())));
            }
        }
    }

}
