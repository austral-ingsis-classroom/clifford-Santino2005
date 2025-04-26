package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ls implements Command {

    @Override
    public Result execute(FileManager dirState, String[] parent) {
       if(parent[0].equals("--ord=")){
            String[] split = parent[0].split("=");
            if(split[1].equals("asc")){
                return new Result(dirState,formatListContent(dirState.getCurrent().listContent()));
            } else if(split[1].equals("desc")) {
                String desc = reverseList(dirState);
                return new Result(dirState, desc);
            }else{
                return new Result(dirState,"use asc or desc");
            }
        }
        else{
            return new Result(dirState, formatListContent(dirState.getCurrent().listContent()));
        }
    }
    private String reverseList(FileManager dirState){
        List<FileSystem> reversed = new ArrayList<>();
        for(int lastItemIndex = dirState.getCurrent().listContent().size() - 1; lastItemIndex >= 0; lastItemIndex--){
            reversed.add(dirState.getCurrent().listContent().get(lastItemIndex));
        }
        return formatListContent(reversed);
    }
    private String formatListContent(List<FileSystem> content) {
        if (content.isEmpty()) {
            return "";
        }

        return content.stream()
                .map(FileSystem::getName)
                .collect(Collectors.joining(" "));
    }
}
