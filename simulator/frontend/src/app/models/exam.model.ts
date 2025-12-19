export interface Option {
    id: number;
    label: string;
    text: string;
}

export interface Question {
    id: number;
    chapter: string;
    questionNumber: number;
    text: string;
    codeSnippet?: string;
    options: Option[];
    correctAnswers?: string[];
    explanation?: string;
}

export interface Exam {
    id: number;
    startTime: string;
    endTime?: string;
    score?: number;
    passed?: boolean;
    questions: ExamQuestion[];
}

export interface ExamQuestion {
    id: number;
    question: Question;
    selectedOptions: string[];
    isCorrect?: boolean;
}
