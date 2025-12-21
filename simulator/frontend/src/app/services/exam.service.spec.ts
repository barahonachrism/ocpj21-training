import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ExamService } from './exam.service';
import { Exam } from '../models/exam.model';

describe('ExamService', () => {
    let service: ExamService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientTestingModule],
            providers: [ExamService]
        });
        service = TestBed.inject(ExamService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    afterEach(() => {
        httpMock.verify();
    });

    it('should be created', () => {
        expect(service).toBeTruthy();
    });

    it('should start exam', () => {
        const mockExam: Exam = { id: '1', questions: [], startTime: new Date().toISOString() };
        service.startExam().subscribe(exam => {
            expect(exam.id).toBe('1');
        });

        const req = httpMock.expectOne('http://127.0.0.1:8080/api/exam/start');
        expect(req.request.method).toBe('POST');
        req.flush(mockExam);
    });

    it('should submit exam', () => {
        const mockExam: Exam = { id: '1', questions: [], startTime: new Date().toISOString(), endTime: new Date().toISOString(), score: 50, passed: true };
        const answers = { '1': ['A'] };
        service.submitExam('1', answers).subscribe(exam => {
            expect(exam.passed).toBe(true);
        });

        const req = httpMock.expectOne('http://127.0.0.1:8080/api/exam/1/submit');
        expect(req.request.method).toBe('POST');
        req.flush(mockExam);
    });

    it('should get exam result', () => {
        const mockResult: Exam = { id: '1', questions: [], startTime: new Date().toISOString(), endTime: new Date().toISOString(), score: 50, passed: true };
        service.getExam('1').subscribe(result => {
            expect(result.id).toBe('1');
        });

        const req = httpMock.expectOne('http://127.0.0.1:8080/api/exam/1');
        expect(req.request.method).toBe('GET');
        req.flush(mockResult);
    });
});
