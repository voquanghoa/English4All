package voon.truongvan.english_for_all_level.controller;

/**
 * Created by Vo Quang Hoa on 12/27/2015.
 */
public class FileCachingController {
    private class FileContent{
        private String filePath;
        private String fileContent;

        public FileContent(String path, String content){
            filePath=path;
            fileContent=content;
        }
    }

    private FileContent[] cache;
    private int currentIndex = 0;
    private static final int CACHE_SIZE = 70;

    private static FileCachingController instance;
    public synchronized static FileCachingController getInstance(){
        if(instance==null){
            instance = new FileCachingController();
        }
        return instance;
    }

    private FileCachingController(){
        cache = new FileContent[CACHE_SIZE];
    }

    public String get(String filePath){
        for(int i=0;i<cache.length;i++){
            if(cache[i] != null && cache[i].filePath.equals(filePath)){
                return cache[i].fileContent;
            }
        }
        return "";
    }

    public void add(String filePath, String content){
        cache[currentIndex] = new FileContent(filePath, content);

        if(++currentIndex>=CACHE_SIZE){
            currentIndex = 0;
        }
    }

}
