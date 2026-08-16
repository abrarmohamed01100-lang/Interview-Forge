// Interview-Forge
// Created by: Abrar
// 2nd Year CS - 2026
// This is my project for interview practice
// I built this because I kept forgetting OOP in interviews :(

import java.util.ArrayList;
import java.util.Scanner;

// I made this enum so I can set difficulty. Learned it from youtube
enum Level {
	EASY, MEDIUM, HARD
}

// This is the parent class for all questions
abstract class Question {
	private String text;
	private String topic;
	private Level level;

	public Question(String text, String topic, Level level) {
		this.text = text;
		this.topic = topic;
		this.level = level;
	}

	public String getText() { return text; }
	public String getTopic() { return topic; }
	public Level getLevel() { return level; }

	// every question must have show and check. my teacher told me this
	public abstract void show();
	public abstract boolean check(String answer);
}

// MCQ questions
class MCQ extends Question {
	private String[] options;
	private String correctAnswer;

	public MCQ(String text, String topic, Level level, String[] options, String correctAnswer) {
		super(text, topic, level);
		this.options = options;
		this.correctAnswer = correctAnswer;
	}

	@Override
	public void show() {
		System.out.println(getText());
		for (int i = 0; i < options.length; i++) {
			System.out.println((i + 1) + ") " + options[i]);
		}
	}

	@Override
	public boolean check(String answer) {
	// trim because I had a bug with spaces
		return correctAnswer.equals(answer.trim());
	}
}

// True or False questions
class TF extends Question {
	private String correctAnswer;

	public TF(String text, String topic, Level level, String correctAnswer) {
		super(text, topic, level);
		this.correctAnswer = correctAnswer.toLowerCase();
	}

	@Override
	public void show() {
		System.out.println(getText());
		System.out.println("1) True");
		System.out.println("2) False");
	}

	@Override
	public boolean check(String answer) {
		String ans = answer.trim().toLowerCase();
		if (ans.equals("1")) ans = "true"; // let user type 1 or 2
		if (ans.equals("2")) ans = "false";
		return correctAnswer.equals(ans);
	}
}

// Essay questions - for short answers
class Essay extends Question {
	private String correctAnswer;

	public Essay(String text, String topic, Level level, String correctAnswer) {
		super(text, topic, level);
		this.correctAnswer = correctAnswer.toLowerCase();
	}

	@Override
	public void show() { System.out.println(getText()); }

	@Override
	public boolean check(String answer) {
		return correctAnswer.equals(answer.trim().toLowerCase());
	}
}

// I called it QuestionBank to store all questions
class QuestionBank {
	private ArrayList<Question> questions = new ArrayList<>();
	public void addQuestion(Question q) { questions.add(q); }
	public Question getQuestion(int index) { return questions.get(index); }
	public int getSize() { return questions.size(); }
}

// to save each result
class Result {
	Question question;
	boolean isCorrect;
	public Result(Question question, boolean isCorrect) {
		this.question = question;
		this.isCorrect = isCorrect;
	}
}

// This class calculates everything
class Calculator {
	public double getPercentage(int correct, int total) {
		if (total == 0) return 0;
		// * 100.0 is important! I forgot this and got 0% always
		return (correct * 100.0) / total;
	}
	public String getGrade(double percent) {
		if (percent >= 85) return "EXCELLENT";
		if (percent >= 70) return "GOOD";
		if (percent >= 50) return "PASS";
		return "FAIL"; // need to study more
	}
}

// Final report
class Report {
	String studentName; int total; int correct; double percent; String grade;
	public Report(String studentName, int total, int correct, double percent, String grade) {
		this.studentName = studentName; this.total = total; this.correct = correct; this.percent = percent; this.grade = grade;
	}
	public void display() {
		System.out.println("\n==== INTERVIEW RESULT ====");
		System.out.println("Student: " + studentName);
		System.out.println("Score: " + correct + "/" + total);
		System.out.printf("Percentage: %.2f%%\n", percent);
		System.out.println("Grade: " + grade);
		System.out.println("==========================");
	}
}

// This was the hardest part for me
class TopicTracker {
	ArrayList<Result> results;
	public TopicTracker(ArrayList<Result> results) { this.results = results; }
	public void showReport() {
		System.out.println("\n==== TOPIC REPORT ====");
		String[] topics = {"OOP", "Inheritance", "Encapsulation", "Polymorphism"};
		
	// I wanted to use HashMap but it was confusing. So I used simple loops instead
		for (String t : topics) {
			int total = 0; int correct = 0;
			for (Result r : results) {
				if (r.question.getTopic().equals(t)) {
					total++;
					if (r.isCorrect) correct++;
				}
			}
			if (total > 0) {
				double p = (correct * 100.0) / total;
				System.out.println(t + ": " + correct + "/" + total + " = " + p + "%");
				// My idea: give advice if student is weak
				if(p < 50){ System.out.println(" -> Advice: Focus on " + t + "! Watch videos and practice."); }
				else if(p >= 80){ System.out.println(" -> Great job in " + t + "! Keep it up."); }
			}
		}
	}
}

// This runs the whole interview
class Interview {
	QuestionBank bank; ArrayList<Result> results; Calculator calc; String studentName;
	public Interview(QuestionBank bank, String studentName) {
		this.bank = bank; this.studentName = studentName; this.results = new ArrayList<>(); this.calc = new Calculator();
	}
	public void start(Scanner scanner) {
		int correctCount = 0;
		System.out.println("Welcome " + studentName + "! Let's begin.");
		for (int i = 0; i < bank.getSize(); i++) {
			Question q = bank.getQuestion(i);
			System.out.println("\nQuestion " + (i + 1) + " [" + q.getTopic() + " - " + q.getLevel() + "]");
			q.show();
			System.out.print("Your Answer: ");
			String answer = scanner.nextLine(); // had bug here before. fixed it
			boolean isCorrect = q.check(answer);
			results.add(new Result(q, isCorrect));
			if (isCorrect) { System.out.println("Correct!"); correctCount++; }
			else { System.out.println("Wrong! Don't worry, try again."); }
	}
		double percent = calc.getPercentage(correctCount, bank.getSize());
		String grade = calc.getGrade(percent);
		Report report = new Report(studentName, bank.getSize(), correctCount, percent, grade);
		report.display();
		TopicTracker tracker = new TopicTracker(results);
		tracker.showReport();
		retryWrong(scanner); // my favorite feature
	}
	// I added this myself. If you fail you can try again
	public void retryWrong(Scanner scanner) {
		System.out.print("\nRetry wrong questions? y/n: ");
		String choice = scanner.nextLine();
		if (choice.equalsIgnoreCase("y")) {
			System.out.println("\n==== RETRY MODE ====");
			for (Result r : results) {
				if (!r.isCorrect) {
					System.out.println("\nTry this again:");
					r.question.show();
					System.out.print("Your Answer: ");
					String newAnswer = scanner.nextLine();
					if (r.question.check(newAnswer)) { System.out.println("Nice! You got it this time."); }
					else { System.out.println("Keep practicing this topic."); }
				}
			}
		}
	}
}

public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter your name: ");
		String name = scanner.nextLine();
	QuestionBank bank = new QuestionBank();
	// I wrote these questions myself from my notes
		String[] q1 = {"Java", "HTML", "CSS", "SQL"};
		bank.addQuestion(new MCQ("Which of these is a programming language?", "OOP", Level.EASY, q1, "1"));
		String[] q2 = {"private", "public", "protected", "default"};
		bank.addQuestion(new MCQ("Which access modifier is most restricted?", "Encapsulation", Level.EASY, q2, "1"));
		bank.addQuestion(new TF("Inheritance allows code reuse.", "Inheritance", Level.MEDIUM, "true"));
		bank.addQuestion(new TF("Polymorphism means one form.", "Polymorphism", Level.MEDIUM, "false"));
		bank.addQuestion(new Essay("Keyword used for inheritance in Java?", "Inheritance", Level.EASY, "extends"));
		bank.addQuestion(new Essay("What keyword makes a variable private?", "Encapsulation", Level.EASY, "private"));
	Interview interview = new Interview(bank, name);
		interview.start(scanner);
		scanner.close();
		System.out.println("Thanks for using Interview-Forge! Good luck in your interview.");
	}
