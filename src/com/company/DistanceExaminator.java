package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class DistanceExaminator implements LoggingIn, StudentRegistration, Exam, AddingQuestions {
    private ArrayList<User> users;
    private ArrayList<Question> questions;
    private User currentUser;
    private ArrayList<String> subjects;
    private HashMap<String, ArrayList<String>> result;

    DistanceExaminator() {
        users = new ArrayList<>();
        try(FileReader reader = new FileReader("C:\\Users\\bonni\\IdeaProjects\\threelab\\users.txt")) {
            int c, b = 0;
            StringBuilder name = new StringBuilder(), login = new StringBuilder(), password = new StringBuilder(),
                    typeUser = new StringBuilder();
            while ((c = reader.read()) != -1) {
                switch ((char) c) {
                    case '/':
                        if (b == 3) {
                            User u;
                            if ("a".equals(typeUser.toString())) {
                                u = new Admin(name.toString().replace("\r", ""),
                                        login.toString().replace("\r", ""),
                                        password.toString().replace("\r", ""));
                            } else {
                                u = new Student(name.toString().replace("\r", ""),
                                        login.toString().replace("\r", ""),
                                        password.toString().replace("\r", ""));
                            }
                            users.add(u);
                            name = new StringBuilder();
                            login = new StringBuilder();
                            password = new StringBuilder();
                            typeUser = new StringBuilder();
                            b = 0;
                        } else {
                            b++;
                        }
                        break;
                    case '\n':
                        break;
                    default:
                        switch (b) {
                            case 0:
                                name.append((char) c);
                                break;
                            case 1:
                                login.append((char) c);
                                break;
                            case 2:
                                password.append((char) c);
                                break;
                            case 3:
                                typeUser.append((char) c);
                                break;
                        }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        questions = new ArrayList<>();

        currentUser = new User();

        subjects = new ArrayList<>(Arrays.asList("Английский язык", "Естествознание", "Информатика", "История",
                "Математика", "Правоведение", "Русский язык", "Физика", "Экономика"));

        result = new HashMap<>();
        result.put("Имя", new ArrayList<>());
        for (String subject: subjects) { result.put(subject, new ArrayList<>()); }
        try(FileReader reader = new FileReader("C:\\Users\\bonni\\IdeaProjects\\threelab\\students.txt")) {
            int c, b = 0;
            StringBuilder word = new StringBuilder();
            while ((c = reader.read()) != -1) {
                switch ((char) c) {
                    case '/':
                        String key = null;
                        switch (b) {
                            case 0: key = "Имя"; break;
                            case 1: key = "Английский язык"; break;
                            case 2: key = "Естествознание"; break;
                            case 3: key = "Информатика"; break;
                            case 4: key = "История"; break;
                            case 5: key = "Математика"; break;
                            case 6: key = "Правоведение"; break;
                            case 7: key = "Русский язык"; break;
                            case 8: key = "Физика"; break;
                            case 9: key = "Экономика"; break;
                            default: break;
                        }
                        ArrayList<String> get = result.get(key); get.add(word.toString()); result.put(key, get);
                        word = new StringBuilder();
                        if (b == 9) { b = 0; } else { b++; }
                        break;
                    case '\n': break;
                    default: word.append((char) c); break;
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void main(String[] args) {
        String point = "";
        try {
            while (!point.equals("Закрыть")) {
                point = menu();
                switch (point) {
                    case "Добавить нового студента":
                    case "Зарегистрироваться как студент":
                        studentRegistration();
                        break;
                    case "Войти в акаунт":
                        loggingIn();
                        break;
                    case "Пройти экзамен":
                        exam();
                        break;
                    case "Добавить вопрос":
                        addQuestion();
                        break;
                    case "Вывести результаты студентов":
                        getResult();
                        break;
                    case "Выйти из пользователя":
                        currentUser = new User();
                        break;
                    case "Закрыть":
                        return;
                    default:
                        System.out.println("Нету такого объекта");
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void addUser(String name, String login, String password, String repetition) throws Exception {
        if (password.equals(repetition)) {
            users.add(new Student(name, login, password));
        } else throw new Exception("Пароли не совпадают");
    }

    private User findUser(String login, String password) throws Exception {
        for (User user : users) {
            if (user.enter(login, password)) return user;
        }
        throw new Exception("Ошибка входа!");
    }

    private void saveResult(int choice) {
        HashMap<String, ArrayList<String>> newResult = new HashMap<>();
        newResult.put("Имя", new ArrayList<>());
        for (String subject: subjects) { newResult.put(subject, new ArrayList<>()); }
        FileWriter writer;
        try {
            writer = new FileWriter("students.txt", false);
            for (int i = 0; i < result.get("Имя").size(); i++) {
                if (!result.get("Имя").get(i).equals(currentUser.getName())) {
                    writer.write(result.get("Имя").get(i) + "/");
                    ArrayList<String> get = newResult.get("Имя");
                    get.add(result.get("Имя").get(i));
                    newResult.put("Имя", get);
                    for (String subject : subjects) {
                        writer.write(result.get(subject).get(i) + "/");
                        get = newResult.get(subject);
                        get.add(result.get(subject).get(i));
                        newResult.put(subject, get);
                    }
                    writer.write("\n");
                    writer.flush();
                } else {
                    writer.write(result.get("Имя").get(i) + "/");
                    ArrayList<String> get = newResult.get("Имя");
                    get.add(result.get("Имя").get(i));
                    newResult.put("Имя", get);
                    for (String subject : subjects) {
                        if (subject.equals(subjects.get(choice - 1))) {
                            writer.write(((Student)currentUser).getRightAnswers() + "/");
                            get = newResult.get(subject);
                            get.add(((Student)currentUser).getRightAnswers().toString());
                            newResult.put(subject, get);
                        } else{
                            writer.write(result.get(subject).get(i) + "/");
                            get = newResult.get(subject);
                            get.add(result.get(subject).get(i));
                            newResult.put(subject, get);
                        }
                    }
                    writer.write("\n");
                    writer.flush();
                }
            }
            result = newResult;
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQuestions(String file) {
        questions = new ArrayList<>();
        try(FileReader reader = new FileReader("C:\\Users\\bonni\\IdeaProjects\\threelab\\" + file)) {
            int c, b = 0;
            StringBuilder text = new StringBuilder(), answer = new StringBuilder();
            while ((c = reader.read()) != -1) {
                if ((char) c == '~') {
                    if (b == 1) {
                        questions.add(new Question(text.toString(),
                                answer.toString().replace("\n", "").replace("\r", "")));
                        text = new StringBuilder();
                        answer = new StringBuilder();
                        b = 0;
                    } else {
                        b = 1;
                    }
                } else {
                    if (b == 0) {
                        text.append((char) c);
                    } else {
                        answer.append((char) c);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getResult() {
        for (int i = 0; i < result.get("Имя").size(); i++) {
            System.out.println(result.get("Имя").get(i) + ":");
            for (String subject : subjects) {
                System.out.println("\t" + subject + " - " + result.get(subject).get(i));
            }
        }
    }

    private String subjectSelection() {
        ArrayList<String> subjectsExam = new ArrayList<>();
        Scanner inSubject = new Scanner(System.in);
        int choice = 0;
        for (int i = 0; i < result.get("Имя").size(); i++) {
            if (result.get("Имя").get(i).equals(currentUser.getName())) {
                for (String subject: subjects) {
                    if (Integer.parseInt(result.get(subject).get(i)) < 3) {
                        subjectsExam.add(subject);
                    }
                }
                break;
            }
        }
        System.out.println("Список предметов:");
        for (int i = 0; i < subjectsExam.size(); i++) {
            System.out.println(((Integer)(i + 1)).toString() + " - " + subjectsExam.get(i));
        }
        boolean check = true;
        while (check) {
            System.out.println("Какой экзамен вы хотите пройти? ");
            if (inSubject.hasNextInt()) { choice = inSubject.nextInt(); } else {
                System.out.println("Допускается только int.");
                inSubject.next();
                continue;
            }
            check = false;
        }
        return subjectsExam.get(choice - 1);
    }

    private String menu() {
        ArrayList<String> pointsMenuAdmin = new ArrayList<>(Arrays.asList("Добавить нового студента",
                "Добавить вопрос", "Вывести результаты студентов", "Выйти из пользователя", "Закрыть"));
        ArrayList<String> pointsMenuStudent = new ArrayList<>(Arrays.asList("Пройти экзамен",
                "Вывести результаты студентов", "Выйти из пользователя", "Закрыть"));
        ArrayList<String> pointMenuUnknown = new ArrayList<>(Arrays.asList("Зарегистрироваться как студент",
                "Войти в акаунт", "Вывести результаты студентов", "Закрыть"));
        Scanner in = new Scanner(System.in);
        int choice = 0;
        try {
            System.out.println("\nМеню:");
            switch (currentUser.getClass().toString()) {
                case "class com.company.User":
                    for (int i = 0; i < pointMenuUnknown.size(); i++) {
                        System.out.println(((Integer) (i + 1)).toString() + ". " + pointMenuUnknown.get(i));
                    }
                    break;
                case "class com.company.Student":
                    for (int i = 0; i < pointsMenuStudent.size(); i++) {
                        System.out.println(((Integer) (i + 1)).toString() + ". " + pointsMenuStudent.get(i));
                    }
                    break;
                case "class com.company.Admin":
                    for (int i = 0; i < pointsMenuAdmin.size(); i++) {
                        System.out.println(((Integer) (i + 1)).toString() + ". " + pointsMenuAdmin.get(i));
                    }
                    break;
            }
            boolean check = true;
            while (check) {
                System.out.println("Что Вы хотите сделать? ");
                if (in.hasNextInt()){
                    choice = in.nextInt();
                }
                else{
                    System.out.println("Допускается только int.");
                    in.next();
                    continue;
                }
                check = false;
            }
            switch (currentUser.getClass().toString()) {
                case "class com.company.User":
                    return pointMenuUnknown.get(choice - 1);
                case "class com.company.Student":
                    return pointsMenuStudent.get(choice - 1);
                case "class com.company.Admin":
                    return pointsMenuAdmin.get(choice - 1);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public void loggingIn() {
        Scanner inLogin = new Scanner(System.in), inPassword = new Scanner(System.in);
        String login, password;
        try {
            System.out.print("Логин: ");
            login = inLogin.next();
            System.out.print("Пароль: ");
            password = inPassword.next();
            currentUser = findUser(login, password);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @Override
    public void studentRegistration() {
        BufferedReader inName = new BufferedReader(new InputStreamReader(System.in));
        Scanner inLogin = new Scanner(System.in), inPassword = new Scanner(System.in),
                inRepetition = new Scanner(System.in);
        String name, login, password, repetition;
        FileWriter writer;
        try {
            System.out.print("Имя: "); name = inName.readLine();
            System.out.print("Логин: "); login = inLogin.next();
            System.out.print("Пароль: "); password = inPassword.next();
            System.out.print("Повторите пароль: "); repetition = inRepetition.next();
            addUser(name, login, password, repetition);

            writer = new FileWriter("users.txt", true);
            writer.write(name + "/" + login + "/" + password + "/s/\n");
            writer.flush();

            writer = new FileWriter("students.txt", true);
            writer.write(name + "/0/0/0/0/0/0/0/0/0/\n");
            writer.flush();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exam() throws IOException {
        String file = "None";
        String subject = "";
        while (file.equals("None")) {
            subject = subjectSelection();
            switch (subject) {
                case "Английский язык": file = "english.txt"; break;
                case "Естествознание": file = "natural.txt"; break;
                case "Информатика": file = "informatics.txt"; break;
                case "История": file = "history.txt"; break;
                case "Математика": file = "mathematics.txt"; break;
                case "Правоведение": file = "jurisprudence.txt"; break;
                case "Русский язык": file = "russian.txt"; break;
                case "Физика": file = "physics.txt"; break;
                case "Экономика": file = "economics.txt"; break;
                default:
                    System.out.println("Нету такого объекта");
                    file = "None";
                    break;
            }
        }
        boolean check;
        loadQuestions(file);
        ArrayList<Integer> numberCurrentQuestions = new ArrayList<Integer>();
        Question question;
        for (int i = 0; i < 5; i++) {
            check = true;
            int rnd = i;
            while (check) {
                rnd = (int) (Math.random() * questions.size());
                check = false;
                for (int j = 0; j < i; j++) {
                    if (rnd == numberCurrentQuestions.get(j)) {
                        check = true;
                        break;
                    }
                }
            }
            numberCurrentQuestions.add(rnd);
        }
        for (int number : numberCurrentQuestions) {
            BufferedReader inAnswer = new BufferedReader(new InputStreamReader(System.in));
            question = questions.get(number);
            System.out.println(question.getText());
            String answer = inAnswer.readLine();
            ((Student)currentUser).getAnswer(question, answer);
        }
        System.out.println("Вы ответили на " + ((Student)currentUser).getRightAnswers() + " из "  + ((Student)currentUser).getQuestionsCount() + " вопросов");
        for (int i = 0; i < subjects.size(); i++) {
            if (subject.equals(subjects.get(i))) saveResult(i);
        }
    }

    @Override
    public void addQuestion() {
        Scanner inSubject = new Scanner(System.in);
        BufferedReader inQuestion = new BufferedReader(new InputStreamReader(System.in)),
                inAnswer = new BufferedReader(new InputStreamReader(System.in));
        String file = "None", question, answer;
        int choice = 0;
        System.out.print("Список предметов:\n" +
                "1 - Английский язык\n" +
                "2 - Естествознание\n" +
                "3 - Информатика\n" +
                "4 - История\n" +
                "5 - Математика\n" +
                "6 - Правоведение\n" +
                "7 - Русский язык\n" +
                "8 - Физика\n" +
                "9 - Экономика\n");
        boolean check = true;
        while (check) {
            System.out.println("Вопрос по какому предмету вы хотите добавить? ");
            if (inSubject.hasNextInt()){
                choice = inSubject.nextInt();
            }
            else{
                System.out.println("Допускается только int.");
                inSubject.next();
                continue;
            }
            check = false;
        }
        switch (choice) {
            case 1: file = "english.txt"; break;
            case 2: file = "natural.txt"; break;
            case 3: file = "informatics.txt"; break;
            case 4: file = "history.txt"; break;
            case 5: file = "mathematics.txt"; break;
            case 6: file = "jurisprudence.txt"; break;
            case 7: file = "russian.txt"; break;
            case 8: file = "physics.txt"; break;
            case 9: file = "economics.txt"; break;
            default:
                System.out.println("Нету такого объекта");
                break;
        }
        if (!file.equals("None")) try (FileWriter writer = new FileWriter(file, true)){
            System.out.println("Вопрос: ");
            question = inQuestion.readLine();
            System.out.print("Ответ: ");
            answer = inAnswer.readLine();
            questions.add(new Question(question, answer));
            writer.write(question + "\n~\n" + answer + "\n~\n");
            writer.flush();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
