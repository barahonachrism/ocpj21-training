import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Exam } from '../models/exam.model';

/**
 * Service for managing exams via the backend API.
 */
@Injectable({
    providedIn: 'root'
})
export class ExamService {
    private apiUrl = environment.apiUrl;

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
    submitExam(examId: string, answers: { [key: string]: string[] }): Observable<Exam> {
        return this.http.post<Exam>(`${this.apiUrl}/${examId}/submit`, answers);
    }

    /**
     * Retrieves the results of a specific exam.
     * @param examId The ID of the exam.
     * @returns An Observable of the ExamResultDTO (mapped to Exam model).
     */
    getExam(examId: string): Observable<Exam> {
        return this.http.get<Exam>(`${this.apiUrl}/${examId}`);
    }
}
