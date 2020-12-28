package com.hello.inject_comiler;

import com.google.auto.service.AutoService;
import com.hello.inject_annotation.Json;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

//这个注解是谷歌提供了，快速实现注解处理器，会帮你生成配置文件啥的 。直接用就好
@AutoService(Processor.class)
public class JsonProcessor extends AbstractProcessor {

    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> set = new HashSet<>();
        set.add(Json.class.getCanonicalName());
        return set;
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment
            roundEnv) {
// 准备在gradle的控制台打印信息
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "start: --------------");

        for (Element element : roundEnv.getElementsAnnotatedWith(Json.class)) {
            if (!(element.getKind() == ElementKind.CLASS)) return false;
// 在gradle的控制台打印信息
            messager.printMessage(Diagnostic.Kind.NOTE, "className: " +
                    element.getSimpleName().toString());
// 创建main⽅法
            MethodSpec main = MethodSpec.methodBuilder("main")//
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)//
                    .returns(void.class)//
                    .addParameter(String[].class, "args")//
                    .addStatement("$T.out.println($S)", System.class, "⾃动创建的2")//
                    .build();
// 创建HelloWorld类
            TypeSpec helloWorld = TypeSpec.classBuilder(element.getSimpleName().toString()+"Bind")//
                    .addModifiers(Modifier.PUBLIC)//
                    .addMethod(main)//
                    .build();

            String packageName =
                    processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
            try {
                JavaFile javaFile = JavaFile.builder(packageName, helloWorld)//
                        .addFileComment(" This codes are generated automatically. Do not modify!")//
                        .build();
                javaFile.writeTo(filer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }
}
