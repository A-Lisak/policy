package com.lisak.controllers;

//import com.ku.apidocs.BeanJsonDocumentor;
//import com.ku.apidocs.ControllerDocumentor;
//import com.ku.apidocs.InterfaceDocumentor;
//import com.ku.apidocs.TableWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DocumentorUtils {

//    static void createControllerDocs(final JUnitRestDocumentation restDocumentation) throws IOException {
//        new ControllerDocumentor(restDocumentation).document();
//    }
//
//    static void createInterfaceDocs(final JUnitRestDocumentation restDocumentation) throws IOException {
//        new InterfaceDocumentor(restDocumentation).document();
//    }
//
//    static void createErrorCodesDocumentation(final JUnitRestDocumentation restDocumentation) throws Exception {
//        String[] tableHeaders = {"Code", "Description Template", "Notes"};
//
//        String[][] tableData = Arrays.stream(MotorPolicyError.values())
//                .map(error -> new String[]{error.getCode(), error.getMessage(), ""}).toArray(String[][]::new);
//
//        new TableWriter(restDocumentation, "errors", "errors", null, tableHeaders, tableData).write();
//    }
//
//    static <T> void addBeanDocumentation(final JUnitRestDocumentation restDocumentation, final String title, final String name,
//                                                final T bean, final String snippetPath) throws IOException {
//        new BeanJsonDocumentor<>(restDocumentation, title, snippetPath, name, bean).document();
//    }
}