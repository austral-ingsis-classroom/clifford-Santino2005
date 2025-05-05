package edu.austral.ingsis.clifford;
public class pwd implements Command{

    @Override
    public Result execute(FileManager dir, String[] content) {
        return new Result(dir,dir.getCurrent().getPath());
    }
}
