package shr.training_camp.dev.excel;

import shr.training_camp.core.model.database.Resources;
import shr.training_camp.core.model.database.ResourcesCraft;

import javax.validation.constraints.Null;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReadResourcesFromExcel {
    // ToDo пока задача прочитать и сохранить в объекты ресурсы и создание ресурсов
    private final static String pathToResources = "src/main/resources/storage/resources.xlsx";
    List<Resources> resourcesList;
    List<ResourcesCraft> resourcesCraftList;

    public static void main(String[] args) {
        // parseMapAndCreateResource();
    }

    public void parseMapAndCreateResource() {
        Map<Integer, List<String>> dataFromExcel = null;
        try {
            dataFromExcel = ReadExcelFile.readExcelFile(pathToResources, 0);
            // dataFromExcel contains header in the first row. In the next, the resource name in the first column and other information in the next (if present)
            List<List<String>> checkList = dataFromExcel.entrySet().stream().skip(1).map(Map.Entry::getValue).collect(Collectors.toList());
            for (List<String> list: checkList) {
                String resourceName = list.get(0);
                Resources resources = new Resources(resourceName);
                if (Objects.isNull(resourcesList)) {
                    resourcesList = new ArrayList<>();
                }
                resourcesList.add(resources);
                /*if (checkList.size()>1) {
                    // ToDo придумать наилучший способ создавать обхект у которого может быть разное число полей
                    switch (checkList.size()) {
                        case 4:

                            break;
                    }

                }*/
            }
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
