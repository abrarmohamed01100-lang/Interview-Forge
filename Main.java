import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

enum Difficulty {
	EASY, MEDIUM, HARD
}

abstract class Question {
	private String questionText;
	private String topic;
	private Difficulty difficulty;

	public Question(String questionText, String topic, Difficulty difficulty) {
		this.questionText = questionText;
		this.topic = topic;
		this.difficulty = difficulty;
	}

	public String getQuestionText() {
		return questionText;
	}

	public String getTopic() {
		return topic;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public abstract void displayQuestion();

	public abstract boolean checkAnswer(String answer);
}

class MCQQuestion extends Question {
	private String[] options;
	private String correctAnswer;

	public MCQQuestion(
		String questionText,
		String topic,
		Difficulty difficulty,
		String[] options,
		String correctAnswer) {

		super(questionText, topic, difficulty);
		this.options = options;
		this.correctAnswer = correctAnswer;
	}

	@Override
	public void displayQuestion() {
		System.out.println(getQuestionText());

		for (int i = 0; i < options.length; i++) {
			System.out.println((i + 1) + ") " + options[i]);
		}
	}

	@Override
	public boolean checkAnswer(String answer) {
		return correctAnswer.equals(answer.trim());
	}
}

class TrueFalseQuestion extends Question {
	private String correctAnswer;

	public TrueFalseQuestion(
		String questionText,
		String topic,
		Difficulty difficulty,
		String correctAnswer) {

		super(questionText, topic, difficulty);
		this.correctAnswer = correctAnswer.toLowerCase();
	}

	@Override
	public void displayQuestion() {
		System.out.println(getQuestionText());
		System.out.println("1) True");
		System.out.println("2) False");
	}

	@Override
	public boolean checkAnswer(String answer) {
		String userAnswer = answer.trim().toLowerCase();

		if (userAnswer.equals("1")) {
			userAnswer = "true";
		} else if (userAnswer.equals("2")) {
			userAnswer = "false";
		}

		return correctAnswer.equals(userAnswer);
	}
}

class OpenQuestion extends Question {
	private String correctAnswer;

	public OpenQuestion(
		String questionText,
		String topic,
		Difficulty difficulty,
		String correctAnswer) {

		super(questionText, topic, difficulty);
		this.correctAnswer = correctAnswer.toLowerCase();
	}

	@Override
	public void displayQuestion() {
		System.out.println(getQuestionText());
	}

	@Override
	public boolean checkAnswer(String answer) {
		return correctAnswer.equals(answer.trim().toLowerCase());
	}
}

class QuestionBank {
	private ArrayList<Question> questions;

	public QuestionBank() {
		questions = new ArrayList<>();
	}

	public void addQuestion(Question question) {
		questions.add(question);
	}

	public Question getQuestion(int index) {
		return questions.get(index);
	}

	public int getNumberOfQuestions() {
		return questions.size();
	}
}

class AnswerResult {
	private Question question;
	private boolean correct;

	public AnswerResult(Question question, boolean correct) {
		this.question = question;
		this.correct = correct;
	}

	public Question getQuestion() {
		return question;
	}

	public boolean isCorrect() {
		return correct;
	}
}

class ScoreCalculator {

	public double calculatePercentage(int correct, int total) {
		if (total == 0) {
			return 0;
		}

		return ((double) correct / total) * 100;
	}

	public String getLevel(double percentage) {
		if (percentage >= 90) {
			return "EXCELLENT";
		} else if (percentage >= 75) {
			return "VERY GOOD";
		} else if (percentage >= 60) {
			return "GOOD";
		} else {
			return "NEEDS IMPROVEMENT";
		}
	}
}

class PerformanceReport {
	private String candidateName;
	private int totalQuestions;
	private int correctAnswers;
	private double percentage;
	private String level;

	public PerformanceReport(
		String candidateName,
		int totalQuestions,
		int correctAnswers,
		double percentage,
		String level) {

		this.candidateName = candidateName;
		this.totalQuestions = totalQuestions;
		this.correctAnswers = correctAnswers;
		this.percentage = percentage;
		this.level = level;
	}

	public void displayReport() {
		int wrongAnswers = totalQuestions - correctAnswers;

		System.out.println("\n================================");
		System.out.println("         INTERVIEW RESULT");
		System.out.println("================================");

		System.out.println("Candidate: " + candidateName);
		System.out.println("Questions: " + totalQuestions);
		System.out.println("Correct: " + correctAnswers);
		System.out.println("Wrong: " + wrongAnswers);
		System.out.printf("Overall Score: %.2f%%\n", percentage);
		System.out.println("Performance Level: " + level);

		System.out.println("================================");
	}
}


class PerformanceAnalyzer {
	private ArrayList<AnswerResult> results;

	public PerformanceAnalyzer(ArrayList<AnswerResult> results) {
		this.results = results;
	}

	public void analyzeTopics() {
		HashMap<String, int[]> topicStats = new HashMap<>();

		for (AnswerResult result : results) {
			String topic = result.getQuestion().getTopic();
			topicStats.putIfAbsent(topic, new int[2]);
			topicStats.get(topic)[0]++;
			if (result.isCorrect()) {
				topicStats.get(topic)[1]++;
			}
		}

		System.out.println("==============================");
		System.out.println(" TOPIC PERFORMANCE");
		System.out.println("==============================");

		String bestTopic = "";
		double bestScore = -1;
		ArrayList<String> weakTopics = new ArrayList<>();
		double weakScore = 101;


		for (String topic : topicStats.keySet()) {
			int[] data = topicStats.get(topic);
			double percentage = ((double) data[1] / data[0]) * 100;
			System.out.println("Topic: " + topic);
			System.out.println("Questions: " + data[0]);
			System.out.println("Correct: " + data[1]);
			System.out.println("Wrong: " + (data[0] - data[1]));
			System.out.printf("Score: %.2f%%\n", percentage);
			System.out.println();

			if (percentage > bestScore) {
				bestScore = percentage;
				bestTopic = topic;
			}
			if (percentage < weakScore) {
				weakScore = percentage;
				weakTopics.clear();
				weakTopics.add(topic);
			} else if (percentage == weakScore) {
				weakTopics.add(topic);
			}
		}

		System.out.println("==============================");
		System.out.println(" PERFORMANCE SUMMARY");
		System.out.println("==============================");
		System.out.println("Strongest Topic: " + bestTopic);
		System.out.printf("Strongest Score: %.2f%%\n", bestScore);

		System.out.print("Topics That Need More Practice: ");
		for (int i = 0; i < weakTopics.size(); i++) {
			System.out.print(weakTopics.get(i));
			if (i < weakTopics.size() - 1) System.out.print(", ");
		}
		System.out.println();
		System.out.printf("Current Score: %.2f%%\n", weakScore);
		System.out.println("Recommendation: Review these topics and practice more questions.");
		System.out.println("==============================");
	}
}
class InterviewSession {
	private QuestionBank questionBank;
	private ArrayList<AnswerResult> results;
	private ScoreCalculator scoreCalculator;
	private String candidateName;

	public InterviewSession(
		QuestionBank questionBank,
		String candidateName) {

		this.questionBank = questionBank;
		this.candidateName = candidateName;
		results = new ArrayList<>();
		scoreCalculator = new ScoreCalculator();
	}

	public void startInterview(Scanner scanner) {

		int score = 0;

		System.out.println("\n================================");
		System.out.println("          INTERVIEW FORGE");
		System.out.println("================================");
		System.out.println("Welcome, " + candidateName);
		System.out.println("Let's start your interview.\n");

		for (int i = 0;
				i < questionBank.getNumberOfQuestions();
				i++) {

			Question question =
				questionBank.getQuestion(i);

			System.out.println("--------------------------------");
			System.out.println("Question " + (i + 1));
			System.out.println("Topic: " + question.getTopic());
			System.out.println(
				"Difficulty: "
				+ question.getDifficulty()
			);
			System.out.println("--------------------------------");

			question.displayQuestion();

			System.out.print("Your answer: ");

			String answer = scanner.nextLine();

			boolean correct =
				question.checkAnswer(answer);

			results.add(
				new AnswerResult(question, correct)
			);

			if (correct) {
				System.out.println("Correct!");
				score++;
			} else {
				System.out.println("Incorrect.");
			}
		}

		int total =
			questionBank.getNumberOfQuestions();

		double percentage =
			scoreCalculator.calculatePercentage(
				score,
				total
			);

		String level =
			scoreCalculator.getLevel(percentage);

		PerformanceReport report =
			new PerformanceReport(
			candidateName,
			total,
			score,
			percentage,
			level
		);

		report.displayReport();

		PerformanceAnalyzer analyzer =
			new PerformanceAnalyzer(results);

		analyzer.analyzeTopics();
	}
}

public class Main {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter your name: ");
		String name = scanner.nextLine();

		QuestionBank bank = new QuestionBank();

		String[] oopOptions = {
			"Java",
			"HTML",
			"CSS",
			"SQL"
		};

		bank.addQuestion(
			new MCQQuestion(
				"Which language supports OOP?",
				"OOP",
				Difficulty.EASY,
				oopOptions,
				"1"
			)
		);

		String[] accessOptions = {
			"private",
			"public",
			"static",
			"final"
		};

		bank.addQuestion(
			new MCQQuestion(
				"Which modifier provides the most restricted access?",
				"Encapsulation",
				Difficulty.EASY,
				accessOptions,
				"1"
			)
		);

		bank.addQuestion(
			new TrueFalseQuestion(
				"Inheritance allows a class to reuse another class.",
				"Inheritance",
				Difficulty.MEDIUM,
				"true"
			)
		);

		bank.addQuestion(
			new TrueFalseQuestion(
				"Java does not support polymorphism.",
				"Polymorphism",
				Difficulty.MEDIUM,
				"false"
			)
		);

		bank.addQuestion(
			new OpenQuestion(
				"What keyword is used for inheritance in Java?",
				"Inheritance",
				Difficulty.EASY,
				"extends"
			)
		);

		bank.addQuestion(
			new OpenQuestion(
				"Which keyword is used to hide data?",
				"Encapsulation",
				Difficulty.EASY,
				"private"
			)
		);

		InterviewSession session =
			new InterviewSession(bank, name);

		session.startInterview(scanner);

		scanner.close();
	}
}
