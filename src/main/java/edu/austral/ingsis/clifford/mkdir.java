package edu.austral.ingsis.clifford;

import java.util.List;
import java.util.NoSuchElementException;

public class mkdir implements Command{

    @Override
    public String execute(FileManager dirState, String[] dir) {
        if(dir.length > 1){
            return "only one parameter";
        }
        else if(dir[0].equals("/")){
            return dir[0] + "cant have /";
        }
        else if(dir[0].contains(" ")){
            return dir[0] + "have spaces between";
        }
        else{
            for(FileSystem data: dirState.getCurrent().listContent()){
                if(!data.getName().equals(dir[0])){
                    dirState.getCurrent().add(new Directory(dir[0], dirState.getCurrent()));
                    return dir[0] + "created";
                }
            }
            return dir[0] + "already created";
        }
    }
}
