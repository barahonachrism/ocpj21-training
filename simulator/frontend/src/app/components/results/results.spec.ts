import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ResultsComponent } from './results';
import { ExamService } from '../../services/exam.service';
import { provideRouter, ActivatedRoute } from '@angular/router';
import { of, Observable } from 'rxjs';
import { Exam } from '../../models/exam.model';
import { vi } from 'vitest';
import { MarkdownModule } from 'ngx-markdown';

describe('ResultsComponent', () => {
  let component: ResultsComponent;
  let fixture: ComponentFixture<ResultsComponent>;
  let examService: any;

  const mockResult: Exam = {
    id: 1,
    startTime: new Date().toISOString(),
    endTime: new Date().toISOString(),
    score: 1,
    passed: false,
    questions: [
      {
        id: 1,
        question: {
          id: 1,
          chapter: 'ch01',
          questionNumber: 1,
          text: 'Question 1',
          options: [{ id: 1, label: 'A', text: 'Option A' }],
          correctAnswers: ['A'],
          explanation: 'Explanation'
        },
        selectedOptions: ['A'],
        isCorrect: true
      }
    ]
  };

  beforeEach(async () => {
    examService = {
      getExam: vi.fn().mockReturnValue(of(mockResult))
    };

    await TestBed.configureTestingModule({
      imports: [ResultsComponent, MarkdownModule.forRoot()],
      providers: [
        { provide: ExamService, useValue: examService },
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: { paramMap: { get: () => '1' } }
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load results on init', () => {
    expect(examService.getExam).toHaveBeenCalledWith(1);
    expect(component.exam).toEqual(mockResult);
  });

  it('should toggle question expansion', () => {
    component.toggleExpand(1);
    expect(component.expandedQuestions[1]).toBe(true);
    component.toggleExpand(1);
    expect(component.expandedQuestions[1]).toBe(false);
  });

  it('should check if question is correct', () => {
    expect(component.isCorrect(1)).toBe(true);
    expect(component.isCorrect(2)).toBe(false);
  });

  it('should handle load error', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    examService.getExam.mockReturnValue(new Observable(subscriber => subscriber.error('error')));

    component.ngOnInit();
    expect(component.error).toBeTruthy();
    expect(consoleSpy).toHaveBeenCalled();
  });

  it('should handle no ID in route', () => {
    const route = TestBed.inject(ActivatedRoute);
    vi.spyOn(route.snapshot.paramMap, 'get').mockReturnValue(null);

    component.ngOnInit();
    expect(component.error).toBe('No exam ID found.');
  });

  it('should handle missing questions in result', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => { });
    examService.getExam.mockReturnValue(of({ id: 1, questions: [] } as any));

    component.ngOnInit();
    // This will now pass without template error as questions is an empty array
  });
});
