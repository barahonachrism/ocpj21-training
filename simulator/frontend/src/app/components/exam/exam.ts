import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription, map } from 'rxjs';
import { Exam, Question } from '../../models/exam.model';
import { ExamService } from '../../services/exam.service';
import { TimerService } from '../../services/timer.service';

import { MarkdownModule } from 'ngx-markdown';

/**
 * Component for taking an exam.
 * Manages the exam state, timer, and user answers.
 */
@Component({
  selector: 'app-exam',
  standalone: true,
  imports: [CommonModule, MarkdownModule],
  templateUrl: './exam.html',
  styleUrls: ['./exam.css'],
})
export class ExamComponent implements OnInit, OnDestroy {
  /** The current exam being taken. */
  exam: Exam | null = null;
  /** Loading state flag. */
  loading = true;
  /** Index of the current question being displayed. */
  currentQuestionIndex = 0;
  /** Map of question IDs to user-selected option labels. */
  answers: { [key: string]: string[] } = {};
  /** Formatted remaining time string. */
  remainingTime = '02:00:00';
  private timerSub?: Subscription;

  /**
   * Constructs the ExamComponent.
   * @param examService Service for exam-related API calls.
   * @param timerService Service for managing the exam timer.
   * @param router Router for navigation.
   * @param cdr ChangeDetectorRef for manual change detection.
   */
  constructor(
    private examService: ExamService,
    private timerService: TimerService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  /**
   * Initializes the component by starting a new exam and setting up the timer.
   */
  ngOnInit() {
    this.examService.startExam().subscribe({
      next: (exam) => {
        this.exam = exam;
        this.loading = false;
        this.startTimer();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to start exam', err);
        this.loading = false;
      }
    });

    this.timerSub = this.timerService.remainingTime$.subscribe((time) => {
      // Keep this for logic if needed, but UI will use async pipe
      if (time === 0 && this.exam && !this.loading) {
        // Could auto-submit here; currently just stops timer.
      }
    });
  }

  get formattedTime$() {
    return this.timerService.remainingTime$.pipe(
      map(time => this.timerService.formatTime(time))
    );
  }

  /**
   * Cleans up the component by stopping the timer and unsubscribing from observables.
   */
  ngOnDestroy() {
    this.timerService.stopTimer();
    if (this.timerSub) {
      this.timerSub.unsubscribe();
    }
  }

  /**
   * Starts the exam timer for 120 minutes.
   */
  private startTimer() {
    this.timerService.startTimer(120 * 60); // 120 minutes
  }

  /**
   * Gets the current question object.
   */
  get currentQuestion(): Question | null {
    if (!this.exam || this.exam.questions.length === 0) {
      return null;
    }
    return this.exam.questions[this.currentQuestionIndex].question;
  }

  /**
   * Checks if a question is multiple-choice (requires more than one correct answer).
   * @param question The question to check.
   */
  isMultipleChoice(question: Question): boolean {
    return (question.correctAnswers?.length || 0) > 1;
  }

  /**
   * Toggles an option selection for the current question.
   * @param optionLabel The label of the option (e.g., 'A').
   */
  toggleOption(optionLabel: string) {
    if (!this.currentQuestion) {
      return;
    }

    const qId = this.currentQuestion.id;
    const currentAnswers = this.answers[qId] || [];
    const isMulti = this.isMultipleChoice(this.currentQuestion);

    if (isMulti) {
      if (currentAnswers.includes(optionLabel)) {
        this.answers[qId] = currentAnswers.filter((a) => a !== optionLabel);
      } else {
        this.answers[qId] = [...currentAnswers, optionLabel];
      }
    } else {
      // Single choice: toggle off if already selected, otherwise replace selection
      if (currentAnswers.includes(optionLabel)) {
        this.answers[qId] = [];
      } else {
        this.answers[qId] = [optionLabel];
      }
    }
  }

  /**
   * Checks if an option is currently selected for the current question.
   * @param optionLabel The label of the option (e.g., 'A').
   */
  isSelected(optionLabel: string): boolean {
    if (!this.currentQuestion) {
      return false;
    }
    return (this.answers[this.currentQuestion.id] || []).includes(optionLabel);
  }

  /**
   * Navigates to the next question.
   */
  next() {
    if (this.exam && this.currentQuestionIndex < this.exam.questions.length - 1) {
      this.currentQuestionIndex++;
    }
  }

  /**
   * Navigates to the previous question.
   */
  prev() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
  }

  /**
   * Submits the exam and navigates to the results page.
   */
  submit() {
    if (!this.exam) {
      return;
    }

    console.log('Submit button clicked');
    // Removed confirm dialog to fix submit issue
    console.log('Submitting exam...', this.exam.id, this.answers);
    this.examService.submitExam(this.exam.id, this.answers).subscribe({
      next: (result) => {
        console.log('Exam submitted successfully', result);
        this.router.navigate(['/results', result.id]);
      },
      error: (err) => console.error('Failed to submit exam', err),
    });
  }
}
