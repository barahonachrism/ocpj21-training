import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Exam } from '../models/exam.model';

@Injectable({
    providedIn: 'root'
})
export class ExamService {
    private apiUrl = 'http://127.0.0.1:8080/api/exam';

    constructor(private http: HttpClient) { }

    startExam(): Observable<Exam> {
        return this.http.post<Exam>(`${this.apiUrl}/start`, {});
    }

    submitExam(examId: number, answers: { [key: number]: string[] }): Observable<Exam> {
        return this.http.post<Exam>(`${this.apiUrl}/${examId}/submit`, answers);
    }

    getExam(examId: number): Observable<Exam> {
        return this.http.get<Exam>(`${this.apiUrl}/${examId}`);
    }
}
