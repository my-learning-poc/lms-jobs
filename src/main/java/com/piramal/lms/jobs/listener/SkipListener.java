package com.piramal.lms.jobs.listener;

import com.mongodb.MongoException;
import com.piramal.lms.jobs.model.LoanDataRead;
import com.piramal.lms.jobs.model.LoanDataWrite;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Component
public class SkipListener {

    @OnSkipInRead
    public void skipInRead(Throwable th){
        if(th instanceof MongoException){
            createFile("C:\\Users\\racloop\\Documents\\MyProject\\SpringBatch\\lms-jobs\\loanInterestAccuralJob\\getloanInterestAccuralStep\\reader\\skipInRead.txt",
                    ((MongoException) th).getMessage());
        }
    }

    @OnSkipInProcess
    public void skipInProcessor(LoanDataRead loanDataRead, Throwable throwable){

        createFile("C:\\Users\\racloop\\Documents\\MyProject\\SpringBatch\\lms-jobs\\loanInterestAccuralJob\\getloanInterestAccuralStep\\processor\\skipInProcessor.txt",
                loanDataRead.toString());
    }

    @OnSkipInWrite
    public void skipInWriter(LoanDataWrite loanDataWrite, Throwable throwable){
        if(throwable instanceof MongoException) {
            createFile("C:\\Users\\racloop\\Documents\\MyProject\\SpringBatch\\lms-jobs\\loanInterestAccuralJob\\getloanInterestAccuralStep\\writer\\skipInWrite.txt",
                    loanDataWrite.toString());
        }
    }

    public void createFile(String filePath, String data){
        try (FileWriter fileWriter = new FileWriter(new File(filePath),true)){
            fileWriter.write(data + new Date() +"\n");
        }
        catch (Exception e){
        }
    }
}
