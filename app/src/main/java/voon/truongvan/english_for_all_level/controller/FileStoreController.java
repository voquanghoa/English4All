package voon.truongvan.english_for_all_level.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import voon.truongvan.english_for_all_level.constant.AppConstant;

/**
 * Created by Vo Quang Hoa on 12/26/2015.
 */
public class FileStoreController implements AppConstant{
    private static FileStoreController instance;

    public static synchronized FileStoreController getInstance(){
        if(instance==null){
            instance=new FileStoreController();
        }
        return instance;
    }

    private String baseDir;

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getBaseDir() {
        return baseDir ;
    }

    public String getFullPath(String path){
        return baseDir+"/"+path;
    }

    private File getFile(String path){
        return new File(getFullPath(path));
    }

    public boolean isFileExist(String path){
        return getFile(path).exists();
    }

    public void writeFile(String path, String content) throws Exception {
        FileOutputStream fos = new FileOutputStream(getFile(path));
        fos.write(content.getBytes(CHARSET));
        fos.close();
    }

    public String readFile(String fileName) throws Exception {
        File file = getFile(fileName);
        FileInputStream fis = new FileInputStream(file);

        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();

        return new String(data, CHARSET);
    }
}
