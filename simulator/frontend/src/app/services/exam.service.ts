import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Exam } from '../models/exam.model';

/**
 * Service for managing exams via the backend API.
 */
@Injectable({
    providedIn: 'root'
})
export class ExamService {
    private apiUrl = 'http://127.0.0.1:8080/api/exam';

    /**
     * Constructs the ExamService.
     * @param http The HttpClient for making API requests.
     */
    constructor(private http: HttpClient) { }

    /**
     * Starts a new exam session.
     * @returns An Observable of the newly created Exam.
     */
    startExam(): Observable<Exam> {
        return this.http.post<Exam>(`${this.apiUrl}/start`, {});
    }

    /**
     * Submits the user's answers for an exam.
     * @param examId The ID of the exam.
     * @param answers A map of question IDs to selected option labels.
     * @returns An Observable of the updated Exam with results.
     */
    submitExam(examId: number, answers: { [key: number]: string[] }): Observable<Exam> {
        return this.http.post<Exam>(`${this.apiUrl}/${examId}/submit`, answers);
    }

    /**
     * Retrieves the results of a specific exam.
     * @param examId The ID of the exam.
     * @returns An Observable of the ExamResultDTO (mapped to Exam model).
     */
    getExam(examId: number): Observable<Exam> {
        return this.http.get<Exam>(`${this.apiUrl}/${examId}`);
    }
}
