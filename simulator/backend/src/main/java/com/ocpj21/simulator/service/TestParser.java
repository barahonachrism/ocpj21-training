// package com.ocpj21.simulator.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class TestParser {
    public static void main(String[] args) {
        String fullText = "**9. Which of the following code snippets correctly serializes an object to a file?**\n" +
                "\n" +
                "**A)**\n" +
                "\n" +
                "```java\n" +
                "class Animal implements Serializable {\n" +
                "    private static final long serialVersionUID = 1L;\n" +
                "    private String species;\n" +
                "    private int age;\n" +
                "\n" +
                "    public Animal(String species, int age) {\n" +
                "        this.species = species;\n" +
                "        this.age = age;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "Animal animal = new Animal(\"Lion\", 5);\n" +
                "try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(\"animal.ser\"))) {\n" +
                "    ois.writeObject(animal);\n" +
                "} catch (IOException e) {\n" +
                "    e.printStackTrace();\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "**B)**\n" +
                "\n" +
                "```java\n" +
                "class Animal implements Serializable {\n" +
                "    // ...\n" +
                "}\n" +
                "```\n";

        // 1. Extract Code Snippet
        Pattern codePattern = Pattern.compile("```java(.*?)```", Pattern.DOTALL);
        Matcher codeMatcher = codePattern.matcher(fullText);
        if (codeMatcher.find()) {
            System.out.println("Found code snippet: " + codeMatcher.group(1).substring(0, 20) + "...");
            fullText = fullText.replace(codeMatcher.group(0), "").trim();
        }

        System.out.println("Full Text after code extraction:\n" + fullText);

        // Debugging
        System.out.println("DEBUG: Trying to match '**A)'");
        Matcher m1 = Pattern.compile("\\*\\*A\\)").matcher(fullText);
        if (m1.find()) {
            System.out.println("Found '**A)' at " + m1.start());
        } else {
            System.out.println("Did NOT find '**A)'");
        }

        System.out.println("DEBUG: Trying to match '**B)'");
        Matcher m2 = Pattern.compile("\\*\\*B\\)").matcher(fullText);
        if (m2.find()) {
            System.out.println("Found '**B)' at " + m2.start());
        } else {
            System.out.println("Did NOT find '**B)'");
        }

        // Simpler Regex without \\s+
        System.out.println("DEBUG: Testing regex without \\s+");
        Pattern simplePattern = Pattern.compile("\\*\\*([A-Z])\\)(.*?)(?=\\*\\*[A-Z]\\)|\\Z)", Pattern.DOTALL);
        Matcher simpleMatcher = simplePattern.matcher(fullText);

        if (simpleMatcher.find()) {
            System.out.println("Found match with simpler regex!");
            System.out.println("Group 1: " + simpleMatcher.group(1));
            System.out.println("Group 2: [" + simpleMatcher.group(2) + "]");
        } else {
            System.out.println("No match with simpler regex.");
        }
    }
}
