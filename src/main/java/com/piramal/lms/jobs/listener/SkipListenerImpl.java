package com.piramal.lms.jobs.listener;

import com.mongodb.MongoException;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

import static java.nio.file.Files.createFile;

@Component
public class SkipListenerImpl implements SkipListener<LoanDataRead, LoanDataWrite> {
    @Override
    public void onSkipInRead(Throwable th) {
        if(th instanceof MongoException){
            createFile("C:\\Users\\racloop\\Documents\\MyProject\\SpringBatch\\lms-jobs\\loanInterestAccuralJob\\getloanInterestAccuralStep\\reader\\skipInRead.txt",
                    ((MongoException) th).getMessage());
        }
    }

    @Override
    public void onSkipInProcess(LoanDataRead loanDataRead, Throwable t) {
        createFile("C:\\Users\\racloop\\Documents\\MyProject\\SpringBatch\\lms-jobs\\loanInterestAccuralJob\\getloanInterestAccuralStep\\processor\\skipInProcessor.txt",
                loanDataRead.toString());
    }

    @Override
    public void onSkipInWrite(LoanDataWrite loanDataWrite, Throwable t) {
        createFile("C:\\Users\\racloop\\Documents\\MyProject\\SpringBatch\\lms-jobs\\loanInterestAccuralJob\\getloanInterestAccuralStep\\writer\\skipInWrite.txt",
                loanDataWrite.toString());
    }

    public void createFile(String filePath, String data){
        try (FileWriter fileWriter = new FileWriter(new File(filePath),true)){
            fileWriter.write(data + new Date() +"\n");
        }
        catch (Exception e){

        }
    }
}
