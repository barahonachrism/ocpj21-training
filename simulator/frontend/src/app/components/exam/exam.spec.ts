import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { Exam } from '../../models/exam.model';
import { ExamService } from '../../services/exam.service';
import { TimerService } from '../../services/timer.service';
import { ExamComponent } from './exam';

describe('ExamComponent', () => {
  let component: ExamComponent;
  let fixture: ComponentFixture<ExamComponent>;

  beforeEach(async () => {
    const mockExam: Exam = {
      id: 1,
      startTime: new Date().toISOString(),
      questions: []
    };

    await TestBed.configureTestingModule({
      imports: [ExamComponent],
      providers: [
        { provide: ExamService, useValue: { startExam: () => of(mockExam), submitExam: () => of(mockExam) } },
        { provide: TimerService, useValue: { remainingTime$: of(0), startTimer: () => {}, stopTimer: () => {}, formatTime: () => '00:00:00' } },
        { provide: Router, useValue: { navigate: () => {} } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ExamComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
