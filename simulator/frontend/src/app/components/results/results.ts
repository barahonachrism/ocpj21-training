import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ExamService } from '../../services/exam.service';
import { MarkdownModule } from 'ngx-markdown';
import { Exam } from '../../models/exam.model';

/**
 * Component for displaying exam results.
 * Shows the score, pass/fail status, and detailed question feedback.
 */
@Component({
  selector: 'app-results',
  standalone: true,
  imports: [CommonModule, RouterLink, MarkdownModule],
  templateUrl: './results.html',
  styleUrls: ['./results.css']
})
export class ResultsComponent implements OnInit {
  /** The exam result data. */
  exam: Exam | null = null;
  /** Loading state flag. */
  loading = true;
  /** Map of question IDs to their expansion state in the UI. */
  expandedQuestions: { [key: number]: boolean } = {};
  /** Error message if loading fails. */
  error: string | null = null;

  /**
   * Constructs the ResultsComponent.
   * @param route ActivatedRoute for accessing URL parameters.
   * @param examService Service for fetching exam results.
   * @param cdr ChangeDetectorRef for manual change detection.
   */
  constructor(
    private route: ActivatedRoute,
    private examService: ExamService,
    private cdr: ChangeDetectorRef
  ) { }

  /**
   * Initializes the component by fetching the exam results based on the ID in the URL.
   */
  ngOnInit() {
    console.log('ResultsComponent: ngOnInit started');
    const id = this.route.snapshot.paramMap.get('id');
    console.log('ResultsComponent: Exam ID from route:', id);
    if (id) {
      console.log('ResultsComponent: Calling getExam for id:', id);
      this.examService.getExam(+id).subscribe({
        next: (exam) => {
          console.log('ResultsComponent: getExam success');
          console.log('ResultsComponent: Full exam object:', JSON.stringify(exam));
          if (exam.questions) {
            console.log('ResultsComponent: questions length:', exam.questions.length);
          } else {
            console.error('ResultsComponent: questions is missing or undefined');
          }
          this.exam = exam;
          this.loading = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('ResultsComponent: getExam error', err);
          this.error = 'Failed to load results. Please try again.';
          this.loading = false;
          this.cdr.detectChanges();
        }
      });
    } else {
      console.error('ResultsComponent: No exam ID found');
      this.error = 'No exam ID found.';
      this.loading = false;
    }
  }

  /**
   * Toggles the expansion state of a question's details.
   * @param qId The ID of the question.
   */
  toggleExpand(qId: number) {
    this.expandedQuestions[qId] = !this.expandedQuestions[qId];
  }

  /**
   * Checks if a question was answered correctly.
   * @param qId The ID of the question.
   */
  isCorrect(qId: number): boolean {
    const q = this.exam?.questions.find(eq => eq.question.id === qId);
    return q?.isCorrect || false;
  }
}
