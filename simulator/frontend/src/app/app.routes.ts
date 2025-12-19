import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home';
import { ExamComponent } from './components/exam/exam';
import { ResultsComponent } from './components/results/results';

export const routes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'exam', component: ExamComponent },
    { path: 'results/:id', component: ResultsComponent },
    { path: '**', redirectTo: '' }
];
