package com.fanduel.abstractr;

import com.fanduel.abstractr.jp.FileUtils;
import com.fanduel.abstractr.jp.parser.JParser;
import com.fanduel.abstractr.jp.parser.JType;
import com.fanduel.abstractr.jp.parser.enums.Modifier;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class MainController {

    @FXML
    private TextField packageInput;

    @FXML
    private ListView<String> fileNameListView;

    @FXML
    private TextField fileNameFilter;

    @FXML
    private Text codeSection;

    private Map<String, List<File>> fileNameMap = new HashMap<>();

    @FXML
    protected void onStartButtonClick() {
        File rootDirectory = new File(packageInput.getText());
        if (rootDirectory.listFiles() == null) {
            return;
        }
        fileNameMap = new HashMap<>();
        FileUtils.getAllFilesFromRootDirectory(rootDirectory)
                .stream()
                .filter(file -> file.getName().endsWith(".java"))
                .forEach(file -> {
                    if (!fileNameMap.containsKey(file.getName())) {
                        fileNameMap.put(file.getName(), new LinkedList<>());
                    }
                    fileNameMap.get(file.getName()).add(file);
                });
//        fileNameMap = fileNameMap.entrySet().stream()
//                        .filter(entry -> entry.getValue().size() > 1)
//                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        updateListView(null);
    }

    @FXML
    protected void onFileNameListViewItemClicked() throws IOException {

        if (fileNameListView.getSelectionModel().getSelectedItem() == null ||
                !fileNameListView.getSelectionModel().getSelectedItem().contains(" ")) {
            return;
        }

        List<File> files = fileNameMap.get(fileNameListView.getSelectionModel().getSelectedItem().split("\\s+")[0]);
        List<JType> jTypes = files.stream().map(JParser::parseFile).collect(Collectors.toList());

        JType first = jTypes.get(0);
        final JType parent = new JType(
                first.getPackageName(),
                first.getTypeName(),
                first.getParentClass(),
                first.getAccessLevel(),
                new LinkedList<>(first.getImports()),
                new LinkedList<>(first.getAnnotations()),
                new LinkedList<>(first.getModifiers()),
                new LinkedList<>(first.getFields()),
                new LinkedList<>(first.getMethods()),
                new LinkedList<>(first.getInnerClasses())
        );
        jTypes.forEach(jType -> {
            parent.getImports().removeIf(i -> !jType.getImports().contains(i));
            parent.getAnnotations().removeIf(a -> !jType.getAnnotations().contains(a));
            parent.getModifiers().removeIf(m -> !jType.getModifiers().contains(m));
            parent.getFields().removeIf(parentField -> jType.getFields()
                    .stream()
                    .noneMatch(field -> {
                        return field.getName().equals(parentField.getName());
                    }));
            parent.getMethods().removeIf(parentMethod -> jType.getMethods()
                    .stream()
                    .noneMatch(method -> {
                        return method.getMethodName().equals(parentMethod.getMethodName()) &&
                                method.getInputParameters().equals(parentMethod.getInputParameters());
                    }));
            parent.getInnerClasses().removeIf(i -> !jType.getInnerClasses().contains(i));
        });
        if (!parent.getModifiers().contains(Modifier.ABSTRACT)) {
            parent.getModifiers().add(Modifier.ABSTRACT);
        }
        codeSection.setText(parent.toString());
//        System.out.println(parent.toString());
//        TypeSpec.Builder typeSpecBuilder = TypeSpec
//                .classBuilder(baseClassName)
//                .addModifiers(Modifier.PUBLIC)
//                .addAnnotation(Data.class)
//                .addAnnotation(
//                        AnnotationSpec
//                                .builder(Generated.class)
//                                .addMember("value", "\"fanduel-model-generator\"")
//                                .build());
//        Map<String, Field> fieldMap = new HashMap<>();
//        classes.stream()
//                .flatMap(clazz -> Arrays.stream(clazz.getFields()))
//                .forEach(field -> {
//                    String fieldName = field.getName();
//                    fieldMap.put(fieldName, field);
//                });
//        fieldMap.forEach((fieldName, field) -> {
//            FieldSpec.Builder fieldSpecBuilder = FieldSpec
//                    .builder(getBaseTypeForField(classMap, field), fieldName)
//                    .addModifiers(Modifier.PRIVATE);
//            Arrays.stream(field.getDeclaredAnnotations()).forEach(annotation ->
//                    fieldSpecBuilder.addAnnotation(
//                            AnnotationSpec
//                                    .builder(annotation.annotationType())
//                                    .addMember("value", '"' + ((JsonProperty) annotation).value() + '"')
//                                    .build()
//                    ));
//            typeSpecBuilder.addField(fieldSpecBuilder.build());
//        });
//        JavaFile javaFile = JavaFile
//                .builder("com.fanduel.modelgenerator.generated.sportradar." + targetPackage, typeSpecBuilder.build())
//                .indent("    ")
//                .build();
//
//        try {
//            if (log.isTraceEnabled()) {
//                javaFile.writeTo(System.out);
//            }
//            Path path = Paths.get("src/main/java/");
//            javaFile.writeTo(path);
//            for (File file : filesToDelete) {
//                file.delete();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    @FXML
    protected void onFileNameFilterChanged() {
        updateListView(fileNameFilter.getText());
    }

    private void updateListView(String filter) {
        fileNameListView.getItems().clear();
        fileNameListView.getItems().addAll(
                fileNameMap
                        .entrySet()
                        .stream()
                        .filter(entry -> filter == null || filter.length() == 0 || entry.getKey().toLowerCase().contains(filter.toLowerCase()))
                        .map(entry -> entry.getKey() + " (" + entry.getValue().size() + ")")
                        .sorted()
                        .collect(Collectors.toList()));
    }

}