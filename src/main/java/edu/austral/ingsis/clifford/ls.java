package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ls implements Command {

    @Override
    public Result execute(FileManager dirState, String[] parent) {
        String[] split = parent[0].split("=");
       if(split[0].equals("--ord")){
            if(split[1].equals("asc")){
                return new Result(dirState,formatListContent(getSortedContent(dirState.getCurrent().listContent(),true)));
            } else if(split[1].equals("desc")) {
                return new Result(dirState, formatListContent(getSortedContent(dirState.getCurrent().listContent(), false)));
            }else{
                return new Result(dirState,"use asc or desc");
            }
        }
        else{
            return new Result(dirState, formatListContent(dirState.getCurrent().listContent()));
        }
    }
    private List<FileSystem> getSortedContent(List<FileSystem> content, boolean ascending) {
        List<FileSystem> sorted = new ArrayList<>(content);
        if (ascending) {
            sorted.sort(Comparator.comparing(FileSystem::getName));
        } else {
            sorted.sort(Comparator.comparing(FileSystem::getName).reversed());
        }

        return sorted;
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
