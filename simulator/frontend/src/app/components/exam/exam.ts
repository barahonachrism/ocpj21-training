import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription, map } from 'rxjs';
import { Exam, Question } from '../../models/exam.model';
import { ExamService } from '../../services/exam.service';
import { TimerService } from '../../services/timer.service';

import { MarkdownModule } from 'ngx-markdown';

@Component({
  selector: 'app-exam',
  standalone: true,
  imports: [CommonModule, MarkdownModule],
  templateUrl: './exam.html',
  styleUrls: ['./exam.css'],
})
export class ExamComponent implements OnInit, OnDestroy {
  exam: Exam | null = null;
  loading = true;
  currentQuestionIndex = 0;
  answers: { [key: number]: string[] } = {};
  remainingTime = '02:00:00';
  private timerSub?: Subscription;

  constructor(
    private examService: ExamService,
    private timerService: TimerService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

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

  ngOnDestroy() {
    this.timerService.stopTimer();
    if (this.timerSub) {
      this.timerSub.unsubscribe();
    }
  }

  private startTimer() {
    this.timerService.startTimer(120 * 60); // 120 minutes
  }

  get currentQuestion(): Question | null {
    if (!this.exam || this.exam.questions.length === 0) {
      return null;
    }
    return this.exam.questions[this.currentQuestionIndex].question;
  }

  isMultipleChoice(question: Question): boolean {
    return (question.correctAnswers?.length || 0) > 1;
  }

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

  isSelected(optionLabel: string): boolean {
    if (!this.currentQuestion) {
      return false;
    }
    return (this.answers[this.currentQuestion.id] || []).includes(optionLabel);
  }

  next() {
    if (this.exam && this.currentQuestionIndex < this.exam.questions.length - 1) {
      this.currentQuestionIndex++;
    }
  }

  prev() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
  }

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
