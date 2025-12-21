import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ExamComponent } from './exam';
import { ExamService } from '../../services/exam.service';
import { TimerService } from '../../services/timer.service';
import { provideRouter, Router } from '@angular/router';
import { of, Observable } from 'rxjs';
import { Exam } from '../../models/exam.model';
import { vi } from 'vitest';
import { MarkdownModule } from 'ngx-markdown';

describe('ExamComponent', () => {
  let component: ExamComponent;
  let fixture: ComponentFixture<ExamComponent>;
  let examService: any;
  let timerService: any;

  const mockExam: Exam = {
    id: 1,
    startTime: new Date().toISOString(),
    questions: [
      {
        id: 1,
        question: {
          id: 1,
          chapter: 'ch01',
          questionNumber: 1,
          text: 'Question 1',
          options: [{ id: 1, label: 'A', text: 'Option A' }]
        },
        selectedOptions: []
      }
    ]
  };

  beforeEach(async () => {
    examService = {
      startExam: vi.fn().mockReturnValue(of(mockExam)),
      submitExam: vi.fn()
    };
    timerService = {
      startTimer: vi.fn(),
      stopTimer: vi.fn(),
      remainingTime$: of(7200),
      formatTime: vi.fn().mockReturnValue('02:00:00')
    };

    await TestBed.configureTestingModule({
      imports: [ExamComponent, MarkdownModule.forRoot()],
      providers: [
        { provide: ExamService, useValue: examService },
        { provide: TimerService, useValue: timerService },
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ExamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load exam on init', () => {
    expect(examService.startExam).toHaveBeenCalled();
    expect(component.exam).toEqual(mockExam);
  });

  it('should navigate between questions', () => {
    component.exam = {
      ...mockExam,
      questions: [mockExam.questions[0], { ...mockExam.questions[0], id: 2 }]
    };
    component.next();
    expect(component.currentQuestionIndex).toBe(1);
    component.prev();
    expect(component.currentQuestionIndex).toBe(0);
    component.prev(); // Should stay at 0
    expect(component.currentQuestionIndex).toBe(0);
  });

  it('should select an option', () => {
    component.toggleOption('A');
    expect(component.answers[1]).toContain('A');
    component.toggleOption('A');
    expect(component.answers[1]).not.toContain('A');
  });

  it('should submit exam', () => {
    const router = TestBed.inject(Router) as any;
    const navigateSpy = vi.spyOn(router, 'navigate');
    examService.submitExam.mockReturnValue(of({ id: 1 }));

    component.submit();
    expect(examService.submitExam).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/results', 1]);
  });

  it('should handle submit error', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    examService.submitExam.mockReturnValue(new Observable(subscriber => subscriber.error('error')));

    component.submit();
    expect(consoleSpy).toHaveBeenCalled();
  });

  it('should check if option is selected', () => {
    expect(component.isSelected('A')).toBe(false);
    component.toggleOption('A');
    expect(component.isSelected('A')).toBe(true);

    component.exam = null;
    expect(component.isSelected('A')).toBe(false);
  });

  it('should handle multiple choice toggle', () => {
    const q = mockExam.questions[0].question;
    q.correctAnswers = ['A', 'B']; // Make it multiple choice

    component.toggleOption('A');
    component.toggleOption('B');
    expect(component.answers[1]).toEqual(['A', 'B']);

    component.toggleOption('A');
    expect(component.answers[1]).toEqual(['B']);
  });

  it('should not submit if no exam', () => {
    component.exam = null;
    component.submit();
    expect(examService.submitExam).not.toHaveBeenCalled();
  });
});
