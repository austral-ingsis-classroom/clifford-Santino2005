package edu.austral.ingsis.clifford;
public class touch implements Command{
    @Override
    public String execute(FileManager dirState, String[] file) {
        if(file[0].equals("/")){
            return file[0] + "cant have /";
        }
        else if(file[0].contains(" ")){
            return file[0] + "have spaces between";
        }
        dirState.getCurrent().add(new File(file[0],dirState.getCurrent()));
        return file[0] + "created";
    }
}
