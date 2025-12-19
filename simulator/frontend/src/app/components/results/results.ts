import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { ExamService } from '../../services/exam.service';
import { MarkdownModule } from 'ngx-markdown';
import { Exam } from '../../models/exam.model';

@Component({
  selector: 'app-results',
  standalone: true,
  imports: [CommonModule, RouterLink, MarkdownModule],
  templateUrl: './results.html',
  styleUrls: ['./results.css']
})
export class ResultsComponent implements OnInit {
  exam: Exam | null = null;
  loading = true;
  expandedQuestions: { [key: number]: boolean } = {};

  constructor(
    private route: ActivatedRoute,
    private examService: ExamService,
    private cdr: ChangeDetectorRef
  ) { }

  error: string | null = null;

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

  toggleExpand(qId: number) {
    this.expandedQuestions[qId] = !this.expandedQuestions[qId];
  }

  isCorrect(qId: number): boolean {
    const q = this.exam?.questions.find(eq => eq.question.id === qId);
    return q?.isCorrect || false;
  }
}
