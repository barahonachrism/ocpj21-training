/** Represents an answer option for a question. */
export interface Option {
    /** Unique identifier for the option. */
    id: string;
    /** Label of the option (e.g., 'A', 'B'). */
    label: string;
    /** Text content of the option. */
    text: string;
}

/** Represents a certification exam question. */
export interface Question {
    /** Unique identifier for the question. */
    id: string;
    /** Chapter name the question belongs to. */
    chapter: string;
    /** Question number within the chapter. */
    questionNumber: number;
    /** Text content of the question. */
    text: string;
    /** Optional code snippet associated with the question. */
    codeSnippet?: string;
    /** List of available answer options. */
    options: Option[];
    /** List of correct option labels. */
    correctAnswers?: string[];
    /** Explanation for the correct answer. */
    explanation?: string;
}

/** Represents an exam session. */
export interface Exam {
    /** Unique identifier for the exam. */
    id: string;
    /** ISO string representing the start time. */
    startTime: string;
    /** ISO string representing the end time. */
    endTime?: string;
    /** Total number of correct answers. */
    score?: number;
    /** Whether the user passed the exam. */
    passed?: boolean;
    /** List of questions included in this exam session. */
    questions: ExamQuestion[];
}

/** Represents a question instance within a specific exam session. */
export interface ExamQuestion {
    /** Unique identifier for the exam-question link. */
    id: string;
    /** The question details. */
    question: Question;
    /** List of option labels selected by the user. */
    selectedOptions: string[];
    /** Whether the user's selection was correct. */
    isCorrect?: boolean;
}
